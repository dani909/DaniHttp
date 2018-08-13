package de.dani09.http

import org.json.JSONArray
import org.json.JSONObject
import java.util.*

/**
 * This Class is the Output of the HttpRequest class
 * @property responseCode Provides the ResponseCode of the HttpRequest will be 0 if the Request Timed Out
 * @property response Provides the Response as an ByteArray
 * @property responseHeaders a map of all Headers from the Response
 * @property responseString Provides the Response as a String
 * @property jSONObject Provides the Response as a JSONObject
 * @property jSONArray Provides the Response as a JSONArray
 * @see HttpRequest
 */
open class HttpResponse(val responseCode: Int,
                        val response: ByteArray,
                        private val responseHeaders: Map<String, String>) {

    val responseString
        get() = String(response)
    val jSONObject
        get() = JSONObject(responseString)
    val jSONArray
        get() = JSONArray(responseString)

    /**
     * Returns the ResponseHeader from the Http Response identified by the Key
     * Will return an empty String if the Header was not found
     * @param key The Key or Name that the Header has
     * @param caseSensitive Should the Header search be Case sensitive or not
     */
    @JvmOverloads
    fun getResponseHeader(key: String, caseSensitive: Boolean = true): String {
        return if (caseSensitive) {
            responseHeaders[key].orEmpty()
        } else {
            responseHeaders
                    .map { it.key.toLowerCase() to it.value }
                    .toMap()[key.toLowerCase()]
                    .orEmpty()
        }
    }

    /**
     * Returns the ContentType the Server responded
     * Will return an empty String if no ContentType Header is present
     */
    fun getContentType(): String {
        return getResponseHeader("Content-Type", false)
                .split(";")
                .map { it.replace("\\s".toRegex(), "") }
                .filter { !it.toLowerCase().startsWith("charset=") }
                .getOrElse(0) { "" }
    }

    /**
     * Returns the ContentLength that the Server responded
     * @return will return -1 if no ContentLength Header is present or is not an Long
     */
    fun getContentLength(): Long {
        return getResponseHeader("Content-Length", false).toLongOrNull() ?: -1
    }

    /**
     * Checks if responseCode is an redirect code and if an location header is present
     */
    fun isRedirect(): Boolean {
        return listOf(301, 302, 303, 307, 308).contains(responseCode) &&
                getResponseHeader("location", false) != ""
    }

    /**
     * Checks if this instance is the same as the passed Parameter
     * @param other the other instance that you want to check
     * @return returns true if it has the same values
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HttpResponse) return false

        if (responseCode != other.responseCode) return false
        if (response.size != other.response.size) return false
        if (!Arrays.equals(response, other.response)) return false
        if (responseHeaders.map { it.key.toLowerCase() to it.value } != other.responseHeaders.map { it.key.toLowerCase() to it.value }) return false

        return true
    }

    /**
     * Calculates the HashCode of this instance
     * @return returns the calculated hashCode
     */
    override fun hashCode(): Int {
        var result = responseCode
        result = 31 * result + Arrays.hashCode(response)
        result = 31 * result + responseHeaders.map { it.key.toLowerCase() to it.value }.hashCode()
        return result
    }
}