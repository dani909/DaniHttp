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

    @Test
    fun relativeRedirect() {
        val nonRedirectResponseCode = Http.get("$httpBin/relative-redirect/1")
                .execute()
                .responseCode

        val redirectResponseCode = Http.get("$httpBin/relative-redirect/1")
                .handleRedirects()
                .execute()
                .responseCode

        assert(302, nonRedirectResponseCode, "relativeRedirectNonRedirect")
        assert(200, redirectResponseCode, "relativeRedirectWithRedirect")
    }

    @Test
    fun redirectOnlyOnce() {
        val redirectResponseCode = Http.get("$httpBin/relative-redirect/2")
                .handleRedirects(1)
                .execute()
                .responseCode

        assert(302, redirectResponseCode, "redirectOnlyOnce")
    }
}