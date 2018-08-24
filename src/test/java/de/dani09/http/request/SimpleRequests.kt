package de.dani09.http.request

import de.dani09.TestUtil.assert
import de.dani09.dani09de
import de.dani09.http.Http
import de.dani09.httpBin
import org.junit.Test

class SimpleRequests {
    @Test
    fun simpleRequest() {
        val response = Http.get("$httpBin/ip")
                .execute()


        println(response.responseString)

        assert(200, response.responseCode, "ResponseCode")
    }

    @Test
    fun simple404() {
        val response = Http.get("$httpBin/ip2")
                .execute()

        assert(404, response.responseCode, "404 ResponseCode")
    }

    @Test
    fun testFile1MB() {
        val response = Http.get("$dani09de/testFiles/1MB.txt")
                .handleRedirects(10)
                .execute()

        assert(1 * 1024 * 1024, response.response.size, "1MBTestFileSize")
    }
}