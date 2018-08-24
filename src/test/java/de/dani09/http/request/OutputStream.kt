package de.dani09.http.request

import de.dani09.TestUtil.assert
import de.dani09.http.Http
import de.dani09.httpBin
import org.junit.Test
import java.io.ByteArrayOutputStream

class OutputStream {
    @Test
    fun outputStreamContentIsCorrect() {
        val expected = Http.get("$httpBin/get")
                .execute()
                .response

        val stream = ByteArrayOutputStream()

        Http.get("$httpBin/get")
                .setOutputStream(stream)
                .execute()

        assert(expected.toList(), stream.toByteArray().toList(), "OutputStreamContentIsCorrect")
    }

    @Test
    fun outputStreamResponseIsEmpty() {
        val responseArray = Http.get("$httpBin/get")
                .setOutputStream(ByteArrayOutputStream())
                .execute()
                .response

        assert(byteArrayOf().toList(), responseArray.toList(), "OutputStreamResponseIsEmpty")
    }
}