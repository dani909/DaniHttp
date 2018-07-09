package de.dani09.http

import org.json.JSONArray
import org.json.JSONObject

@Suppress("unused", "UNUSED_PARAMETER", "MemberVisibilityCanBePrivate")
/**
 * Created by dani909 on 09.07.18.
 */
class HttpResponse(responseCode: Int,
                   val response: ByteArray,
                   val responseHeaders: Map<String, String>) {

    fun getResponseString() = String(response)
    fun getJSONObject() = JSONObject(getResponseString())
    fun getJSONArray() = JSONArray(getResponseString())
    fun getResponseHeader(key: String, caseSensitive: Boolean): String? {
        return if (caseSensitive) {
            responseHeaders[key]
        } else {
            responseHeaders
                    .map { it.key.toLowerCase() to it.value.toLowerCase() }
                    .toMap()[key]
        }
    }
}