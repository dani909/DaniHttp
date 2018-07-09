package de.dani09.http

/**
 * Created by dani909 on 09.07.18.
 */
object Http {
    fun get(url: String): HttpRequest = HttpRequest(url, HttpMethod.GET)
    fun post(url: String): HttpRequest = HttpRequest(url, HttpMethod.POST)
    fun put(url: String): HttpRequest = HttpRequest(url, HttpMethod.PUT)
    fun delete(url: String): HttpRequest = HttpRequest(url, HttpMethod.DELETE)
}