package de.dani09.http.request

import de.dani09.TestUtil
import de.dani09.TestUtil.assert
import de.dani09.http.Http
import de.dani09.httpBin
import org.junit.Test

class Futures {
    @Test
    fun futureTest1() {
        val origin = Http.get("$httpBin/ip")
                .executeAsFuture()
                .get()
                .jSONObject
                .getString("origin")

        assert(false, origin.isNullOrEmpty(), "Futures1")
    }

    @Test
    fun futureTest2() {
        val expected = TestUtil.generateRandomString(8, true)

        val headerResult = Http.get("$httpBin/get")
                .addRequestHeader("Test-Header", expected)
                .executeAsFuture()
                .get()
                .jSONObject
                .getJSONObject("headers")
                .getString("Test-Header")

        assert(expected, headerResult, "Futures2")
    }
}