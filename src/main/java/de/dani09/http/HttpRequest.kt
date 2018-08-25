package de.dani09.http

import org.json.JSONObject
import java.io.OutputStream
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future

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
class HttpRequest(internal var url: String,
                  internal var httpMethod: HttpMethod) {

    internal var requestHeaders: MutableMap<String, String> = mutableMapOf()
    internal var userAgent: String = "Mozilla/5.0"
    internal var body: ByteArray? = null
    internal var timeout: Int = 10000
    internal var readTimeout: Int = 10000
    internal var maxRedirects: Int = 0
    internal var progressListeners: MutableList<HttpProgressListener> = mutableListOf()
    internal var outputStream: OutputStream? = null

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
     * Sets the Content-Type Header for this HttpRequest
     * @param contentType the Content-Type you wish to set
     */
    fun setContentType(contentType: String) = apply { addRequestHeader("Content-Type", contentType) }

    /**
     * Sets the "Connection" Header to "close" to disable KeepAlive
     */
    fun noKeepAlive() = addRequestHeader("Connection", "close")

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


    /**
     * Executes the Http Request and returns the Response
     * ResponseCode will be 0 if the Connection Timed Out
     * @return will return the HttpResponse of the executed HttpRequest
     */
    fun execute() = HttpRequestExecutor(this).executeHttpRequest(true)!!

    /**
     * Executes the Http Request and will not throw any Exception
     * but will return null in case of an error instead
     * @see execute for more details
     */
    fun executeWithoutExceptions() = HttpRequestExecutor(this).executeHttpRequest(false)

    /**
     * Executes the Http Request as a Future
     * @see execute for more details
     */
    fun executeAsFuture(): Future<HttpResponse> = Executors
            .newSingleThreadExecutor()
            .submit(Callable<HttpResponse> {
                HttpRequestExecutor(this).executeHttpRequest(true)
            })

    // equals and hashCode

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

