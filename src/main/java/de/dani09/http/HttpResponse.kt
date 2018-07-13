package de.dani09.http

import org.json.JSONArray
import org.json.JSONObject

/**
 * This Class is the Output of the HttpRequest class
 * @property response Provides the Response as an ByteArray
 * @property responseCode Provides the ResponseCode of the HttpRequest
 * @property responseString Provides the Response as a String
 * @property jSONObject Provides the Response as a JSONObject
 * @property jSONArray Provides the Response as a JSONArray
 * @see HttpRequest
 */
@Suppress("unused", "UNUSED_PARAMETER", "MemberVisibilityCanBePrivate")
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
                    .map { it.key.toLowerCase() to it.value.toLowerCase() }
                    .toMap()[key.toLowerCase()]
                    .orEmpty()
        }
    }

    /**
     * Returns the ContentType the Server responded
     * Will return an empty String if no ContentType Header is present
     */
    fun getContentType(): String = getResponseHeader("Content-Type", false)
}