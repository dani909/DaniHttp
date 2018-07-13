package de.dani09.http

import org.apache.commons.io.IOUtils
import org.json.JSONObject
import java.io.FileNotFoundException
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

/**
 * With this class you can make Http requests to an Server
 * to do that instance it add some Header etc. depending on what you need
 * and then call the execute method to actually execute it
 *
 * You can make HttpRequests in an Builder like pattern
 * because each method is returning the current instance
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class HttpRequest(private val url: String,
                  private var httpMethod: HttpMethod) {

    private var requestHeaders: MutableMap<String, String> = mutableMapOf()
    private var userAgent: String = "Mozilla/5.0"
    private var body: ByteArray? = null

    /**
     * sets the UserAgent for the Request
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
    fun setRequestBody(b: JSONObject, charset: Charset = Charsets.UTF_8): HttpRequest = apply { body = b.toString().toByteArray(charset) }

    /**
     * Sets the Content-Type Header for this HttpRequest
     * @param contentType the Content-Type you wish to set
     */
    fun setContentType(contentType: String) = apply { addRequestHeader("Content-Type", contentType) }

    /**
     * Executes the Http Request and returns the Response
     */
    fun execute() = Executor(this).executeHttpRequest()

    private class Executor(private val request: HttpRequest) {
        fun executeHttpRequest(): HttpResponse {
            val result: HttpResponse?
            val connection: HttpURLConnection = URL(request.url).openConnection() as HttpURLConnection

            try {
                // Adding props to connection
                connection.requestMethod = request.httpMethod.toString()

                addRequestHeaders(connection)
                setRequestBody(connection)

                // Creating Response from connection
                result = HttpResponse(
                        responseCode = connection.responseCode,
                        response = IOUtils.toByteArray(connection.inputStream),
                        responseHeaders = processResponseHeaders(connection)
                )
            } catch (e: Exception) {
                when (e::class) {
                    FileNotFoundException::class -> {
                        return HttpResponseDummy(404)
                    }

                    else -> throw e
                }
            } finally {
                connection.disconnect()
            }

            return result!!
        }

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
                    .filter { it.value != null && it.value.count { it == null } == 0 }
                    .filter { it.value.size > 0 }
                    .map { it.key to it.value.reduce { acc, s -> acc + s } }
                    .toMap()
        }
    }
}

