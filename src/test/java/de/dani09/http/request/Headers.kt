package de.dani09.http.request

import de.dani09.TestUtil
import de.dani09.TestUtil.assert
import de.dani09.http.Http
import de.dani09.httpBin
import org.junit.Test

class Headers {

    @Test
    fun userAgent() {
        val expected = TestUtil.generateRandomString(16)

        val actual = Http.get("$httpBin/user-agent")
                .setUserAgent(expected)
                .execute()
                .jSONObject
                .getString("user-agent")

        assert(expected, actual, "UserAgent")
    }

    @Test
    fun header1() {
        val randomString = TestUtil.generateRandomString(16)

        val result = Http.get("$httpBin/get")
                .addRequestHeader("Test-Header", randomString)
                .execute()
                .jSONObject
                .getJSONObject("headers")
                .getString("Test-Header")

        assert(randomString, result, "TestHeader1")
    }

    @Test
    fun header2() {
        val randomString = TestUtil.generateRandomString(32)

        val result = Http.get("$httpBin/get")
                .addRequestHeader("Test-Header2", randomString)
                .execute()
                .jSONObject
                .getJSONObject("headers")
                .getString("Test-Header2")

        assert(randomString, result, "TestHeader2")
    }

    @Test
    fun setContentType() {
        val expected = TestUtil.generateRandomString(16)

        val actual = Http.get("$httpBin/get")
                .setContentType(expected)
                .execute()
                .jSONObject
                .getJSONObject("headers")
                .getString("Content-Type")

        assert(expected, actual, "SetContentType")
    }

    @Test
    fun getContentType() {
        val expected = "application/json"

        val actual = Http.get("$httpBin/get")
                .setContentType(expected)
                .execute()
                .getContentType()

        assert(expected, actual, "GetContentType")
    }

    @Test
    fun noKeepAlive() {
        val expected = "close"

        val actual = Http.get("$httpBin/get")
                .noKeepAlive()
                .execute()
                .jSONObject
                .getJSONObject("headers")
                .getString("Connection")
                .toLowerCase()

        assert(expected, actual, "NoKeepAliveConnectionHeader")
    }
}