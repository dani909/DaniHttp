package de.dani09.http.request

import de.dani09.TestUtil
import de.dani09.TestUtil.assert
import de.dani09.http.Http
import de.dani09.httpBin
import org.junit.Test
import java.net.URLEncoder

class Redirects {

    @Test
    fun noRedirect() {
        val randomString = TestUtil.generateRandomString(8, true)

        val result = Http.get("$httpBin/redirect-to?url=${URLEncoder.encode(randomString, "utf8")}")
                .execute()

        assert(302, result.responseCode, "NoRedirectResponseCode")
        assert(randomString, result.getResponseHeader("location", false), "NoRedirectLocationHeader")
    }

    @Test
    fun redirectToMainHttpBinPage() {
        val actualSize = Http.get(httpBin)
                .execute()
                .getContentLength()

        val redirectedResult = Http.get("$httpBin/redirect-to?url=${URLEncoder.encode(httpBin, "utf8")}")
                .handleRedirects()
                .execute()

        assert(200, redirectedResult.responseCode, "redirectToHttpBinResponseCode")
        assert(actualSize, redirectedResult.getContentLength(), "redirectToHttpBinLength")
    }
}