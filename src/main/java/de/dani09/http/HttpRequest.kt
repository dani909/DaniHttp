package de.dani09.http

@Suppress("unused")
/**
 * Created by dani909 on 09.07.18.
 */
class HttpRequest(internal val url: String, internal var httpMethod: HttpMethod) {
    internal var requestHeaders: MutableMap<String, String> = mutableMapOf()
    internal var userAgent: String = "Mozilla/5.0"

    fun setHttpMethod(method: HttpMethod): HttpRequest {
        httpMethod = method
        return this
    }

    fun addRequestHeader(headerName: String, headerValue: String): HttpRequest {
        requestHeaders[headerName] = headerValue
        return this
    }

    fun execute() = HttpRequestExecutor(this).executeHttpRequest()
}