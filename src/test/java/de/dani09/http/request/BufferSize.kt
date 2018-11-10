package de.dani09.http.request

import de.dani09.TestUtil.assert
import de.dani09.http.Http
import de.dani09.http.HttpProgressListener
import de.dani09.httpBin
import org.junit.Test

class BufferSize {
    @Test
    fun oneByteBuffer() {
        var timesOnProgressCalled = 0L
        var lastBytesRead = 0L

        val response = Http
                .get("$httpBin/get")
                .setReadBufferSize(1)
                .addProgressListener(object : HttpProgressListener {
                    override fun onProgress(bytesRead: Long, fullLength: Long) {
                        if (bytesRead != lastBytesRead) { // only if new bytes were received
                            timesOnProgressCalled++
                            lastBytesRead = bytesRead
                        }
                    }
                })
                .execute()

        val bodyLength = response.getContentLength()

        assert(bodyLength, timesOnProgressCalled, "1ByteBuffer")
    }
}