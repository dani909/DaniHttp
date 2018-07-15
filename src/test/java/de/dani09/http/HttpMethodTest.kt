package de.dani09.http

import de.dani09.TestUtil.assert
import org.junit.Test

class HttpMethodTest {
    @Test
    fun toStringTest() {
        assert("GET", HttpMethod.GET.toString(), "HttpMethodGet")
        assert("POST", HttpMethod.POST.toString(), "HttpMethodPost")
        assert("PUT", HttpMethod.PUT.toString(), "HttpMethodPut")
        assert("DELETE", HttpMethod.DELETE.toString(), "HttpMethodDelete")
    }
}