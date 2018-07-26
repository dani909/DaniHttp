package de.dani09.http.response

import de.dani09.TestUtil
import de.dani09.TestUtil.assert
import de.dani09.http.HttpResponse
import org.junit.Test

class EqualsHashCode {
    @Test
    fun hashCode1() {
        val randomValue1 = TestUtil.generateRandomString(16, false)
        val randomValue2 = TestUtil.generateRandomString(16, false)

        val response1 = HttpResponse(50, byteArrayOf(24, 20, -35, 121), mapOf("Test-Header1" to randomValue1, "Test-header2" to randomValue2))
        val response2 = HttpResponse(50, byteArrayOf(24, 20, -35, 121), mapOf("test-HEADER1" to randomValue1, "test-HeadEr2" to randomValue2))

        assert(response1.hashCode(), response2.hashCode(), "HashCode1")
    }

    @Test
    fun hashCode2() {
        val response1 = HttpResponse(50, byteArrayOf(24, 20, -35, 121), mapOf())
        val response2 = HttpResponse(49, byteArrayOf(24, 20, -35, 121), mapOf())

        assert(response1.hashCode(), response2.hashCode(), "HashCode2", true)
    }

    @Test
    fun equals1() {
        val randomValue1 = TestUtil.generateRandomString(16, false)
        val randomValue2 = TestUtil.generateRandomString(16, false)

        val response1 = HttpResponse(30, byteArrayOf(-95, 20, -35, 65), mapOf("Test-Header1" to randomValue1, "Test-header2" to randomValue2))
        val response2 = HttpResponse(30, byteArrayOf(-95, 20, -35, 65), mapOf("test-HEADER1" to randomValue1, "test-HeadEr2" to randomValue2))

        assert(response1.equals(response2))
    }
}