package de.dani09.http

import org.junit.Test
import java.util.*

class Tests {
    @Test
    fun validResponseCode() {
        val code = Http.get("https://httpbin.org/ip")
                .execute()
                .responseCode
        assert(code == 200)
    }

    @Test
    fun jsonObjectParsingWorks() {
        val json = Http.get("https://httpbin.org/ip")
                .execute()
                .jSONObject
        assert(json.getString("origin") != "")
    }

    @Test
    fun generateRandomStringTest() {
        for (x in 0..1000) {
            assert(generateRandomString(8).length == 8)
        }
    }

    @Test
    fun userAgentWorking() {
        val userAgent = generateRandomString(16)
        val json = Http.get("https://httpbin.org/get")
                .setUserAgent(userAgent)
                .execute()
                .jSONObject

        val receivedUserAgent = json.getJSONObject("headers").getString("User-Agent")
        assert(receivedUserAgent == userAgent)
    }

    @Test
    fun requestHeadersWorking() {
        val headerValue = generateRandomString(16)
        val json = Http.get("https://httpbin.org/get")
                .addRequestHeader("Test-Header", headerValue)
                .execute()
                .jSONObject

        val receivedHeaderValue = json.getJSONObject("headers").getString("Test-Header")
        assert(receivedHeaderValue == headerValue)
    }

    private fun generateRandomString(length: Int = 8): String {
        val small = "abcdefghijklmnopqrstuvwzyx"
        val big = small.toUpperCase()
        val both = small + big

        return (1..length).map {
            val index = Random().nextInt(both.length)
            both[index]
        }.map { it.toString() }.reduce { acc, c -> acc + c }
    }
}