package de.dani09

import java.util.*

val dani09de = TestUtil.getDani09DeUrl()
val httpBin = TestUtil.getHttpBinUrl()

object TestUtil {
    private const val httpBinEnvName = "HTTP_BIN_URL"

    @JvmStatic
    fun generateRandomString(length: Int = 16, onlyLowerCase: Boolean = false): String {
        val smallLetters = "abcdefghijklmnopqrstuvwxyz"
        val bigLetters = smallLetters.toUpperCase()
        val numbers = "0123456789"

        val everything = numbers + smallLetters + if (!onlyLowerCase) bigLetters else ""

        var result = ""
        val random = Random()
        for (x in 0 until length) {
            result += everything[random.nextInt(everything.length)]
        }

        return result
    }

    @JvmStatic
    @JvmOverloads
    fun assert(expected: Any, actual: Any, name: String = "", invertAssertion: Boolean = false) {
        if (name != "")
            print("$name: ")


        if (invertAssertion) {
            print("Not Allowed: $expected Actual: $actual\n")
            assert(expected != actual)
        } else {
            print("Expected: $expected Actual: $actual\n")
            assert(expected == actual)
        }
    }

    @JvmStatic
    fun runMultipleTimes(times: Int, f: () -> Unit) {
        for (x in 0 until times) {
            f()
        }
    }

    @JvmStatic
    fun getHttpBinUrl(): String {
        // Checking if http bin override is provided
        // This is used to be able to run it in an docker container to improve Test Speed
        return if (System.getenv(httpBinEnvName) == null) {
            // no url defined and therefor return normal url
            "https://httpbin.org"
        } else {
            // url is defined. grab it from the environment variable
            System.getenv(httpBinEnvName)
        }
    }

    @JvmStatic
    fun getDani09DeUrl() = "https://dani09.de"
}

