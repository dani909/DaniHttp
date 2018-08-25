package de.dani09.http.request

import de.dani09.TestUtil.assert
import de.dani09.dani09de
import de.dani09.http.Http
import de.dani09.http.HttpMethod
import de.dani09.http.HttpRequest
import org.junit.Test

class Head {
    @Test
    fun headOf1MBFile() {
        val response = Http.head("$dani09de/testFiles/1MB.txt")
                .handleRedirects(10)
                .execute()

        assert(0, response.response.size, "1MBTestFileHeadResponseSize")
    }

    @Test
    fun headIsFasterThanGet1MB() {
        fun getRequestTime(method: HttpMethod): Long {
            val before = System.currentTimeMillis()

            HttpRequest("$dani09de/testFiles/1MB.txt", method)
                    .handleRedirects(10)
                    .execute()

            val after = System.currentTimeMillis()
            return after - before
        }

        val get = getRequestTime(HttpMethod.GET)
        val head = getRequestTime(HttpMethod.HEAD)

        println("time get: $get ms. time head: $head ms")
        assert(true, head < get, "HeadIsFasterThanGet1MB")
    }
}