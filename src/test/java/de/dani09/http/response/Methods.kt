package de.dani09.http.response

import de.dani09.TestUtil
import de.dani09.TestUtil.assert
import de.dani09.http.HttpResponse
import de.dani09.http.HttpResponseDummy
import org.junit.Test

class Methods {
    @Test
    fun responseHeaders() {
        val map = mapOf(
                "Header1" to "Value1",
                "header2" to TestUtil.generateRandomString(16),
                "header3" to "Value3"
        )

        val response = HttpResponse(0, byteArrayOf(), map)

        assert(map["Header1"].orEmpty(), response.getResponseHeader("Header1"), "ResponseHeader1")
        assert(map["header2"].orEmpty(), response.getResponseHeader("header2"), "ResponseHeader2")

        // Header name in lower case to test if case sensitivity works correctly
        assert("", response.getResponseHeader("Header3"), "ResponseHeader3")
    }

    @Test
    fun responseHeaderNonCaseSensitivity() {
        val map = mapOf(
                "HEADER1" to TestUtil.generateRandomString(16)
        )

        val response = HttpResponse(0, byteArrayOf(), map)

        assert(map["HEADER1"].orEmpty(), response.getResponseHeader("Header1", false), "ResponseHeaderNonCaseSensitivity1")
        assert(map["HEADER1"].orEmpty(), response.getResponseHeader("header1", false), "ResponseHeaderNonCaseSensitivity2")
        assert(map["HEADER1"].orEmpty(), response.getResponseHeader("HEADER1", false), "ResponseHeaderNonCaseSensitivity3")
        assert("", response.getResponseHeader("Header1", true), "ResponseHeaderNonCaseSensitivity4")
    }

    @Test
    fun contentType1() {
        val randomString = TestUtil.generateRandomString(16)
        val map = mapOf("Content-Type" to randomString)

        val response = HttpResponse(0, byteArrayOf(), map)

        assert(randomString, response.getContentType(), "ContentType1")
    }

    @Test
    fun contentType2() {
        val randomString = TestUtil.generateRandomString(16)
        val map = mapOf("content-TYPE" to randomString)

        val response = HttpResponse(0, byteArrayOf(), map)

        assert(randomString, response.getContentType(), "ContentType2")
    }

    @Test
    fun contentType3() {
        // With charset
        val actual = "text/html"
        val map = mapOf("Content-Type" to "text/html; charset=UTF-8")

        val response = HttpResponse(0, byteArrayOf(), map)

        assert(actual, response.getContentType(), "ContentType3")
    }

    @Test
    fun contentLength1() {
        val response = HttpResponseDummy(0)

        assert(-1L, response.getContentLength(), "ContentLength1")
    }

    @Test
    fun contentLength2() {
        val actual = 1449L

        val map = mapOf("Content-Length" to actual.toString())
        val response = HttpResponse(0, byteArrayOf(), map)

        assert(actual, response.getContentLength(), "ContentLength2")
    }

    @Test
    fun contentLength3() {
        val map = mapOf("Content-Length" to "SomeString")
        val response = HttpResponse(0, byteArrayOf(), map)

        assert(-1L, response.getContentLength(), "ContentLength2")
    }
}