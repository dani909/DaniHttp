package de.dani09.http

/**
 * These are the supported HttpMethods for the HttpRequest class
 * @see HttpRequest
 */
enum class HttpMethod {
    /** GET will be an HTTP "GET" */
    GET,
    /** POST will be an HTTP "POST" */
    POST,
    /** PUT will be an HTTP "PUT" */
    PUT,
    /** DELETE will be an HTTP "DELETE" */
    DELETE,
    /** HEAD will be an HTTP "HEAD" */
    HEAD;

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
        HEAD -> "HEAD"
    }
}