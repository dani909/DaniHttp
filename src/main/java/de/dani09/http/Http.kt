package de.dani09.http

/**
 * This Class is providing shortcuts to create HttpRequest objects
 * @see HttpRequest
 */
@Suppress("unused")
object Http {
    /**
     * Creates a HttpRequest with the given Url and sets the HttpMethod to "GET"
     * @param url provides the Url to the HttpRequest
     */
    @JvmStatic
    fun get(url: String): HttpRequest = HttpRequest(url, HttpMethod.GET)

    /**
     * Creates a HttpRequest with the given Url and sets the HttpMethod to "POST"
     * @param url provides the Url to the HttpRequest
     */
    @JvmStatic
    fun post(url: String): HttpRequest = HttpRequest(url, HttpMethod.POST)

    /**
     * Creates a HttpRequest with the given Url and sets the HttpMethod to "PUT"
     * @param url provides the Url to the HttpRequest
     */
    @JvmStatic
    fun put(url: String): HttpRequest = HttpRequest(url, HttpMethod.PUT)

    /**
     * Creates a HttpRequest with the given Url and sets the HttpMethod to "DELETE"
     * @param url provides the Url to the HttpRequest
     */
    @JvmStatic
    fun delete(url: String): HttpRequest = HttpRequest(url, HttpMethod.DELETE)
}