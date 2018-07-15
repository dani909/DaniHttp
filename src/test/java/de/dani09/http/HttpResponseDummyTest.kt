package de.dani09.http

import de.dani09.TestUtil.assert
import org.junit.Test

class HttpResponseDummyTest {
    @Test
    fun createsCorrectly() {
        val res = HttpResponseDummy(8)

        assert(8, res.responseCode, "ResponseCode")
        assert(HttpResponse(8, byteArrayOf(), mapOf()), res, "ResponseObject")
    }
}