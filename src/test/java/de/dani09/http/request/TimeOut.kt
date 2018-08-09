package de.dani09.http.request

import de.dani09.TestUtil
import de.dani09.TestUtil.assert
import de.dani09.dani09de
import de.dani09.http.Http
import org.junit.Test

class TimeOut {
    @Test
    fun timeOut1() {
        // Not so good but other wise the tests would be so slow...
        val timeOut = 1000
        val overDeltaOk = 500

        val timeBefore = System.currentTimeMillis()

        Http.get("$dani09de:81")
                .setTimeOut(timeOut)
                .execute()

        val timeAfter = System.currentTimeMillis()

        val delta = timeAfter - timeBefore

        println("Selected TimeOut: $timeOut. Delta: $delta. Max allowed Delta: ${timeOut + overDeltaOk}")

        assert(delta < timeOut + overDeltaOk)
    }

    @Test
    fun timeOutResponseCode() {
        val result = Http.get("$dani09de:81")
                .setTimeOut(1)
                .execute()

        assert(0, result.responseCode, "TimeOutResponseCode")
    }
}