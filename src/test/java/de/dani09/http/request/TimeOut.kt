package de.dani09.http.request

import de.dani09.TestUtil
import de.dani09.http.Http
import org.junit.Test

private val dani09de = TestUtil.getDani09DeUrl()

class TimeOut {
    @Test
    fun timeOut1() {
        // Not so good but other wise the tests would be so slow...
        val timeOut = 1000
        val overDeltaOk = 250

        val timeBefore = System.currentTimeMillis()

        Http.get("$dani09de:81")
                .setTimeOut(timeOut)
                .execute()

        val timeAfter = System.currentTimeMillis()

        val delta = timeAfter - timeBefore

        println("Delta: $delta. Max allowed Delta: ${timeOut + overDeltaOk}")

        assert(delta < timeOut + overDeltaOk)
    }
}