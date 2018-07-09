package de.dani09.http

/**
 * Created by dani909 on 09.07.18.
 */
enum class HttpMethod {
    GET,
    POST,
    PUT,
    DELETE;

    override fun toString() = when (this) {
        GET -> "GET"
        POST -> "POST"
        PUT -> "PUT"
        DELETE -> "DELETE"
    }
}