package de.dani09.http

import org.json.JSONArray
import org.json.JSONObject

@Suppress("unused", "UNUSED_PARAMETER", "MemberVisibilityCanBePrivate")
/**
 * Created by dani909 on 09.07.18.
 */
class HttpResponse(val responseCode: Int,
                   val response: ByteArray,
                   private val responseHeaders: Map<String, String>) {

    val responseString
        get() = String(response)
    val jSONObject
        get() = JSONObject(responseString)
    val jSONArray
        get() = JSONArray(responseString)

    fun getResponseHeader(key: String, caseSensitive: Boolean = true): String? {
        return if (caseSensitive) {
            responseHeaders[key]
        } else {
            responseHeaders
                    .map { it.key.toLowerCase() to it.value.toLowerCase() }
                    .toMap()[key.toLowerCase()]
        }
    }
}