package de.dani09.http

/**
 * These are the supported HttpMethods for the HttpRequest class
 * @see HttpRequest
 */
enum class HttpMethod {
    GET,
    POST,
    PUT,
    DELETE;

    /**
     * Converts Enum to a String
     * The String will be equal to the Enum Name
     * E.g. HttpMethod.GET will become "GET"
     */
    override fun toString() = when (this) {
        GET -> "GET"
        POST -> "POST"
        PUT -> "PUT"
        DELETE -> "DELETE"
    }
}