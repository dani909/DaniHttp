package de.dani09.http

import de.dani09.TestUtil
import de.dani09.TestUtil.assert
import org.junit.Test

class HttpShortcutTest {
    @Test
    fun get() {
        val randomString = TestUtil.generateRandomString(16)
        val actual = Http.get(randomString)
        val expected = HttpRequest(randomString, HttpMethod.GET)

        assert(expected, actual, "HttpShortcutGet")
    }

    @Test
    fun post() {
        val randomString = TestUtil.generateRandomString(16)
        val actual = Http.post(randomString)
        val expected = HttpRequest(randomString, HttpMethod.POST)

        assert(expected, actual, "HttpShortcutPost")
    }

    @Test
    fun put() {
        val randomString = TestUtil.generateRandomString(16)
        val actual = Http.put(randomString)
        val expected = HttpRequest(randomString, HttpMethod.PUT)

        assert(expected, actual, "HttpShortcutPut")
    }

    @Test
    fun delete() {
        val randomString = TestUtil.generateRandomString(16)
        val actual = Http.delete(randomString)
        val expected = HttpRequest(randomString, HttpMethod.DELETE)

        assert(expected, actual, "HttpShortcutDelte")
    }
}