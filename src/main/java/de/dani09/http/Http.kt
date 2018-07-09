package de.dani09.http

@Suppress("unused")
/**
 * Created by dani909 on 09.07.18.
 */
object Http {
    @JvmStatic
    fun get(url: String): HttpRequest = HttpRequest(url, HttpMethod.GET)

    @JvmStatic
    fun post(url: String): HttpRequest = HttpRequest(url, HttpMethod.POST)

    @JvmStatic
    fun put(url: String): HttpRequest = HttpRequest(url, HttpMethod.PUT)

    @JvmStatic
    fun delete(url: String): HttpRequest = HttpRequest(url, HttpMethod.DELETE)
}