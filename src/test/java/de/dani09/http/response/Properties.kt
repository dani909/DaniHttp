package de.dani09.http.response

import de.dani09.TestUtil
import de.dani09.TestUtil.assert
import de.dani09.http.HttpResponse
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Test
import java.util.*

class Properties {
    @Test
    fun responseCode() {
        val responseCode = Random().nextInt(100)
        val response = HttpResponse(responseCode, byteArrayOf(), mapOf())

        assert(responseCode, response.responseCode, "ResponseCode")
    }

    @Test
    fun responseBytes() {
        var bytes = byteArrayOf()
        val random = Random()

        for (x in 0..100) {
            bytes += random.nextInt(255).toByte()
        }

        val response = HttpResponse(0, bytes, mapOf())

        assert(bytes, response.response, "ResponseBytes")
    }

    @Test
    fun responseString() {
        val string = TestUtil.generateRandomString(16)
        val stringBytes = string.toByteArray(Charsets.UTF_8)

        val response = HttpResponse(0, stringBytes, mapOf())

        assert(string, response.responseString, "ResponseString")
    }

    @Test
    fun responseJSONObject() {
        val randomString = TestUtil.generateRandomString(16)
        val stringBytes = JSONObject().put("key", randomString).toString().toByteArray(Charsets.UTF_8)

        val response = HttpResponse(0, stringBytes, mapOf())

        assert(randomString, response.jSONObject.getString("key"), "ResponseJsonObject")
    }

    @Test
    fun responseJSONArray() {
        val jsonArray = JSONArray()
                .put(2)
                .put(5)
                .put(9)
                .put(6)
                .put(3)

        val bytes = jsonArray.toString().toByteArray(Charsets.UTF_8)

        val response = HttpResponse(0, bytes, mapOf())

        assert(5, response.jSONArray.length(), "ResponseJsonArray")
    }
}