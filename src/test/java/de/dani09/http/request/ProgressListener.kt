package de.dani09.http.request

import de.dani09.TestUtil.assert
import de.dani09.http.Http
import de.dani09.http.HttpProgressListener
import de.dani09.httpBin
import org.junit.Test

class ProgressListener {
    @Test
    fun onStart() {
        var gotCalled = false

        Http.get("$httpBin/get")
                .addProgressListener(object : HttpProgressListener {
                    override fun onStart(length: Long) {
                        gotCalled = true
                    }
                })
                .execute()
        assert(true, gotCalled, "OnStartGetsCalled")
    }

    @Test
    fun onFinish() {
        var gotCalled = false

        Http.get("$httpBin/get")
                .addProgressListener(object : HttpProgressListener {
                    override fun onFinish() {
                        gotCalled = true
                    }
                })
                .execute()
        assert(true, gotCalled, "OnFinishGetsCalled")
    }

    @Test
    fun onProgressPercentage() {
        Http.get("$httpBin/get")
                .addProgressListener(object : HttpProgressListener {
                    override fun onProgress(percentage: Double) {
                        assert(true, percentage <= 100.0 && percentage > 0.0, "onProgressPercentage between 0 and 100")
                    }
                })
                .execute()
    }

    @Test
    fun onProgressBytes() {
        var lastCallBytesRead = 0L
        var maxLength = 0L

        Http.get("$httpBin/get")
                .addProgressListener(object : HttpProgressListener {
                    override fun onProgress(bytesRead: Long, fullLength: Long) {
                        lastCallBytesRead = bytesRead
                        maxLength = fullLength
                    }
                })
                .execute()

        assert(maxLength, lastCallBytesRead, "onProgressBytesReadEqualToFullLength")
    }

    @Test
    fun redirects() {
        var startGotCalled = false
        var progressGotCalled = false
        var finishGotCalled = false

        val response = Http.get("$httpBin/redirect/2")
                .addProgressListener(object : HttpProgressListener {
                    override fun onStart(length: Long) {
                        startGotCalled = true
                    }

                    override fun onProgress(bytesRead: Long, fullLength: Long) {
                        progressGotCalled = true
                    }

                    override fun onFinish() {
                        finishGotCalled = true
                    }
                })
                .handleRedirects(2)
                .execute()

        assert(200, response.responseCode, "redirectsResponseCode")
        assert(true, startGotCalled, "redirectsStart")
        assert(true, progressGotCalled, "redirectsProgress")
        assert(true, finishGotCalled, "redirectsFinish")
    }

    @Test
    fun noRedirects() {
        var startGotCalled = false
        var progressGotCalled = false
        var finishGotCalled = false

        val response = Http.get("$httpBin/redirect/2")
                .addProgressListener(object : HttpProgressListener {
                    override fun onStart(length: Long) {
                        startGotCalled = true
                    }

                    override fun onProgress(bytesRead: Long, fullLength: Long) {
                        progressGotCalled = true
                    }

                    override fun onFinish() {
                        finishGotCalled = true
                    }
                })
                .handleRedirects(0)
                .execute()

        assert(302, response.responseCode, "redirectsResponseCode")
        assert(true, startGotCalled, "redirectsStart")
        assert(true, progressGotCalled, "redirectsProgress")
        assert(true, finishGotCalled, "redirectsFinish")
    }
}