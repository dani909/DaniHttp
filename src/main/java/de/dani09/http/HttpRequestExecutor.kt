package de.dani09.http

import org.apache.commons.io.IOUtils
import java.net.URL
import javax.net.ssl.HttpsURLConnection

/**
 * Created by dani909 on 09.07.18.
 */
internal class HttpRequestExecutor(private val request: HttpRequest) {
    fun executeHttpRequest(): HttpResponse {
        val result: HttpResponse?
        val connection: HttpsURLConnection = URL(request.url).openConnection() as HttpsURLConnection

        try {
            // Adding props to connection
            connection.requestMethod = request.httpMethod.toString()
            addRequestHeaders(connection)

            // Creating Response from connection
            result = HttpResponse(
                    responseCode = connection.responseCode,
                    response = IOUtils.toByteArray(connection.inputStream),
                    responseHeaders = processResponseHeaders(connection)
            )
        } catch (e: Exception) {
            throw e
        } finally {
            println("!finally!")
            connection.disconnect()
        }

        return result!!
    }

    private fun addRequestHeaders(c: HttpsURLConnection) {
        c.setRequestProperty("User-Agent", request.userAgent)

        for ((k, v) in request.requestHeaders) {
            c.setRequestProperty(k, v)
        }
    }

    private fun processResponseHeaders(connection: HttpsURLConnection): Map<String, String> {
        return connection.headerFields
                .filter { it.key != null }
                .filter { it.value != null && it.value.count { it == null } == 0 }
                .filter { it.value.size > 0 }
                .map { it.key to it.value.reduce { acc, s -> acc + s } }
                .toMap()
    }
}