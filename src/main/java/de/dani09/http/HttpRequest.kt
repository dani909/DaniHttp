package de.dani09.http

import org.apache.commons.io.IOUtils
import org.json.JSONObject
import java.io.FileNotFoundException
import java.io.OutputStream
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.HttpURLConnection.HTTP_SEE_OTHER
import java.net.SocketTimeoutException
import java.net.URL
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
import javax.net.ssl.HttpsURLConnection

/**
 * With this class you can make Http requests to an Server
 * to do that instance it add some Header etc. depending on what you need
 * and then call the execute method to actually execute it
 *
 * You can make HttpRequests in an Builder like pattern
 * because each method is returning the current instance
 *
 * @property url the Url on which the Request will performed on
 * @property httpMethod the Http method that will be used to perform the Request
 */
@Suppress("unused")
class HttpRequest(private var url: String,
                  private var httpMethod: HttpMethod) {

    private var requestHeaders: MutableMap<String, String> = mutableMapOf()
    private var userAgent: String = "Mozilla/5.0"
    private var body: ByteArray? = null
    private var timeout: Int = 10000
    private var readTimeout: Int = 10000
    private var maxRedirects: Int = 0
    private var progressListeners: MutableList<HttpProgressListener> = mutableListOf()
    private var outputStream: OutputStream? = null

    /**
     * sets the UserAgent for the Request
     * Default Value is "Mozilla/5.0"
     */
    fun setUserAgent(userAgent: String): HttpRequest = apply { this.userAgent = userAgent }

    /**
     * adds an Request Header to the Request
     */
    fun addRequestHeader(headerName: String, headerValue: String): HttpRequest = apply { this.requestHeaders[headerName] = headerValue }

    /**
     * Sets the body for the HttpRequest
     * @param b the Body in a String
     * @param charset the used Charset for the conversion to Bytes
     */
    @JvmOverloads
    fun setRequestBody(b: String, charset: Charset = Charsets.UTF_8): HttpRequest = apply { body = b.toByteArray(charset) }

    /**
     * Sets the body for the HttpRequest
     * @param b the Body in a Byte Array
     */
    fun setRequestBody(b: ByteArray): HttpRequest = apply { body = b }

    /**
     * Sets the body for the HttpRequest
     * @param b the Body in a JSONObject
     * @param charset the used Charset for the conversion to Bytes
     * @see setContentType you may want to set Json as a ContentType
     */
    @JvmOverloads
    fun setRequestBody(b: JSONObject, charset: Charset = Charsets.UTF_8): HttpRequest = apply { body = b.toString().toByteArray(charset) }

    /**
     * Sets the Content-Type Header for this HttpRequest
     * @param contentType the Content-Type you wish to set
     */
    fun setContentType(contentType: String) = apply { addRequestHeader("Content-Type", contentType) }

    /**
     * Sets the maximal Socket Connect Timeout in milliseconds for this HttpRequest
     * Default value is 10000
     * 0 stands for infinity
     */
    fun setTimeOut(timeout: Int) = apply { this.timeout = timeout }

    /**
     * Sets the maximal Socket Read Timeout in milliseconds for this HttpRequest
     * Default value is 10000
     * 0 stands for infinity
     */
    fun setReadTimeOut(readTimeout: Int) = apply { this.readTimeout = readTimeout }

    /**
     * Sets the max allowed Redirects the HttpRequest will follow
     * Default param value is 1 to follow 1 redirect
     * Do not set to like Integer.MAX_VALUE because you may block your Thread for ever if you get into an Ring Redirect
     * @param maxRedirects maximum allowed Redirects to follow
     */
    @JvmOverloads
    fun handleRedirects(maxRedirects: Int = 1) = apply { this.maxRedirects = maxRedirects }

    /**
     * Executes the Http Request and returns the Response
     * ResponseCode will be 0 if the Connection Timed Out
     * @return will return the HttpResponse of the executed HttpRequest
     */
    fun execute() = Executor(this).executeHttpRequest(true)!!

    /**
     * Executes the Http Request and will not throw any Exception
     * but will return null in case of an error instead
     * @see execute for more details
     */
    fun executeWithoutExceptions() = Executor(this).executeHttpRequest(false)

    /**
     * Executes the Http Request as a Future
     * @see execute for more details
     */
    fun executeAsFuture(): Future<HttpResponse> = Executors
            .newSingleThreadExecutor()
            .submit(Callable<HttpResponse> {
                Executor(this).executeHttpRequest(true)
            })

    /**
     * adds and ProgressListener to the HttpRequest to get the current status when the HttpRequest is executing
     * @param listener the listener you want to add
     * @see HttpProgressListener
     */
    fun addProgressListener(listener: HttpProgressListener) = apply { this.progressListeners.add(listener) }

    /**
     * set an OutputStream as an output of the HttpRequest
     * if provided it will write the responseBody into this OutputStream instead of providing it to the HttpResponse
     * Note that HttpResponse.getResponse will return an empty byteArray if an OutputStream is used!
     * @param stream the OutputStream that should be written the http body to
     */
    fun setOutputStream(stream: OutputStream) = apply { this.outputStream = stream }

    private class Executor(private val request: HttpRequest) {

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

            val response: ByteArray = readInputStream(connection, request)

            return HttpResponse(
                    responseCode = responseCode,
                    response = response,
                    responseHeaders = responseHeaders
            )
        }

        fun readInputStream(connection: HttpURLConnection, request: HttpRequest): ByteArray {
            val length = connection.contentLengthLong
            request.progressListeners.forEach { it.onStart(length) }

            val stream = connection.inputStream
            var response: ByteArray = byteArrayOf()
            val buffer = ByteArray(2048)
            var byteCount = 0
            var bytesRead = 0L
            while (byteCount != -1) {
                byteCount = stream.read(buffer, 0, 2048)

                if (byteCount != -1) {
                    bytesRead += byteCount

                    response = processBytes(buffer, byteCount, response, request)

                    request.progressListeners.forEach { it.onProgress(bytesRead, length) }
                    if (length > 0)
                        request.progressListeners.forEach { it.onProgress(bytesRead / length.toDouble() * 100) }
                }
            }

            request.progressListeners.forEach { it.onFinish() }
            return response
        }

        fun processBytes(bytes: ByteArray, byteCount: Int, response: ByteArray, request: HttpRequest): ByteArray {
            if (request.outputStream != null)
                request.outputStream!!.write(bytes, 0, byteCount)
            else
                return response + bytes

            return response
        }

        private fun followRedirect(response: HttpResponse, redirectCount: Int, exceptions: Boolean): HttpResponse? {
            val r = request.also {} // make copy

            // 303 See Other
            if (response.responseCode == HTTP_SEE_OTHER)
                r.httpMethod = HttpMethod.GET // as specified in RFC7231 6.4.4.

            val location = response.getResponseHeader("location", false)

            r.url = if (location.startsWith("/")) {
                // Relative Location
                val u = URL(r.url)
                "${u.protocol}://${u.authority}$location"
            } else
                location // Absolute Location

            return Executor(r).executeHttpRequest(exceptions, redirectCount + 1)
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

    /**
     * Checks if this instance is the same as the passed Parameter
     * @param other the other instance that you want to check
     * @return returns true if it has the same values
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HttpRequest) return false

        if (url != other.url) return false
        if (httpMethod != other.httpMethod) return false
        if (requestHeaders != other.requestHeaders) return false
        if (userAgent != other.userAgent) return false
        if (!Arrays.equals(body, other.body)) return false
        if (timeout != other.timeout) return false

        return true
    }

    /**
     * Calculates the HashCode of this instance
     * @return returns the calculated hashCode
     */
    override fun hashCode(): Int {
        var result = url.hashCode()
        result = 31 * result + httpMethod.hashCode()
        result = 31 * result + requestHeaders.hashCode()
        result = 31 * result + userAgent.hashCode()
        result = 31 * result + (body?.let { Arrays.hashCode(it) } ?: 0)
        result = 31 * result + timeout
        return result
    }
}

