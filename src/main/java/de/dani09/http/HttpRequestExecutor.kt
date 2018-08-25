package de.dani09.http

import org.apache.commons.io.IOUtils
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.OutputStream
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

internal class HttpRequestExecutor(private val request: HttpRequest) {

    /**
     * Will execute the given HttpRequest and return the Response
     * @param exceptions should Exceptions be thrown? if false it will return null if there was any exception
     */
    fun executeHttpRequest(exceptions: Boolean = true, redirectCount: Int = 0): HttpResponse? {
        var result: HttpResponse?
        val connection: HttpURLConnection = getConnection()

        try {
            // Adding props to connection
            connection.requestMethod = request.httpMethod.toString()

            addRequestHeaders(connection)
            setRequestBody(connection)

            connection.instanceFollowRedirects = false
            connection.connectTimeout = request.timeout
            connection.readTimeout = request.readTimeout

            connection.connect()

            result = getHttpResponse(connection, request, redirectCount)

            // Handle redirects if wanted
            if (isRedirect(result, redirectCount))
                result = followRedirect(result, redirectCount, exceptions)

        } catch (e: Exception) {
            return when (e::class) {
                FileNotFoundException::class -> {
                    HttpResponseDummy(HttpURLConnection.HTTP_NOT_FOUND)
                }

                SocketTimeoutException::class -> {
                    HttpResponseDummy(0)
                }

                ConnectException::class -> {
                    HttpResponseDummy(0)
                }

                else -> {
                    if (!exceptions) null
                    else throw e
                }
            }
        } finally {
            connection.disconnect()
        }

        return result
    }

    private fun getHttpResponse(connection: HttpURLConnection, request: HttpRequest, redirectCount: Int): HttpResponse {
        val responseHeaders = processResponseHeaders(connection)
        val responseCode = connection.responseCode

        val smallResponse = HttpResponse(
                responseCode = responseCode,
                response = byteArrayOf(),
                responseHeaders = responseHeaders
        )

        if (isRedirect(smallResponse, redirectCount))
            return HttpResponse(
                    responseCode = responseCode,
                    response = connection.inputStream.use { IOUtils.toByteArray(it) },
                    responseHeaders = responseHeaders
            )

        return if (request.outputStream == null) {
            val stream = ByteArrayOutputStream()
            readInToOutputStream(connection, request.progressListeners, stream)

            HttpResponse(
                    responseCode = responseCode,
                    response = stream.toByteArray(),
                    responseHeaders = responseHeaders
            )
        } else {
            readInToOutputStream(connection, request.progressListeners, request.outputStream!!)
            smallResponse
        }
    }

    private fun readInToOutputStream(connection: HttpURLConnection, progressListeners: List<HttpProgressListener>, outputStream: OutputStream) {
        val length = connection.contentLengthLong
        progressListeners.forEach { it.onStart(length) }

        val stream = connection.inputStream
        val buffer = ByteArray(2048)
        var byteCount = 0
        var bytesRead = 0L
        while (byteCount != -1) {
            byteCount = stream.read(buffer, 0, 2048)

            if (byteCount != -1) {
                bytesRead += byteCount

                outputStream.write(buffer, 0, byteCount)

                progressListeners.forEach { it.onProgress(bytesRead, length) }
                if (length > 0)
                    progressListeners.forEach { it.onProgress(bytesRead / length.toDouble() * 100) }
            }
        }

        progressListeners.forEach { it.onFinish() }
    }

    private fun followRedirect(response: HttpResponse, redirectCount: Int, exceptions: Boolean): HttpResponse? {
        val r = request.also {} // make copy

        // 303 See Other
        if (response.responseCode == HttpURLConnection.HTTP_SEE_OTHER)
            r.httpMethod = HttpMethod.GET // as specified in RFC7231 6.4.4.

        val location = response.getResponseHeader("location", false)

        r.url = if (location.startsWith("/")) {
            // Relative Location
            val u = URL(r.url)
            "${u.protocol}://${u.authority}$location"
        } else
            location // Absolute Location

        return HttpRequestExecutor(r).executeHttpRequest(exceptions, redirectCount + 1)
    }

    private fun isRedirect(response: HttpResponse, redirectCount: Int): Boolean {
        return redirectCount < request.maxRedirects &&
                response.isRedirect()
    }

    private fun getConnection(): HttpURLConnection {
        val url = URL(request.url)
        val urlConnection = url.openConnection()

        return if (isUrlHttps(url))
            urlConnection as HttpsURLConnection
        else
            urlConnection as HttpURLConnection

    }

    private fun isUrlHttps(url: URL) = url.protocol == "https"

    private fun setRequestBody(connection: HttpURLConnection) {
        if (request.body != null && request.body!!.isNotEmpty()) {
            connection.doOutput = true
            IOUtils.write(request.body, connection.outputStream)
            connection.outputStream.close()
        }
    }

    private fun addRequestHeaders(c: HttpURLConnection) {
        c.setRequestProperty("User-Agent", request.userAgent)

        for ((k, v) in request.requestHeaders) {
            c.setRequestProperty(k, v)
        }
    }

    private fun processResponseHeaders(connection: HttpURLConnection): Map<String, String> {
        return connection.headerFields
                .filter { it.key != null }
                .filter { it.value != null && it.value.count { str -> str == null } == 0 }
                .filter { it.value.size > 0 }
                .map { it.key to it.value.reduce { acc, s -> acc + s } }
                .toMap()
    }
}