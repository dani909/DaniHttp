package de.dani09.http.request

import de.dani09.TestUtil
import de.dani09.TestUtil.assert
import de.dani09.http.Http
import org.junit.Test

private val httpBin = TestUtil.getHttpBinUrl()

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

}