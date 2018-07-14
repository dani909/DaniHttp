package de.dani09.http

import java.util.*

object TestUtil {
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

    @JvmOverloads
    fun assert(expected: Any, actual: Any, name: String = "") {
        if (name == "")
            println("Expected: $expected Actual: $actual")
        else
            println("$name Expected: $expected Actual: $actual")
        assert(expected == actual)
    }

    fun runMultipleTimes(times: Int, f: () -> Unit) {
        for (x in 0 until times) {
            f()
        }
    }
}

