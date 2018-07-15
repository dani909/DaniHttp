package de.dani09

import org.junit.Test

class TestUtilTest {
    class GenerateRandomString {
        @Test
        fun length0() {
            println("Expected: \"\" ")
            val result = TestUtil.generateRandomString(0)
            println("Actual: \"$result\"")
            assert(result == "")
        }

        @Test
        fun length8() {
            println("Expected Length: 8")
            val result = TestUtil.generateRandomString(8)
            println("Actual Length: ${result.length}")
            assert(result.length == 8)
        }

        @Test
        fun length16() {
            println("Expected Length: 16")
            val result = TestUtil.generateRandomString(16)
            println("Actual Length: ${result.length}")
            assert(result.length == 16)
        }

        @Test
        fun onlyLowerCase() {
            val result = TestUtil.generateRandomString(16, true)
            println("Expected: ${result.toLowerCase()}")
            println("Actual: $result")
            assert(result == result.toLowerCase())
        }

        @Test
        fun run1000Times() {
            for (x in 1..1000) {
                val result = TestUtil.generateRandomString(16)

                if (result.length != 16)
                    println("Wrong Length! Expected: 16 Actual: ${result.length}")

                assert(result.length == 16)
            }
        }
    }

    class Assert {
        @Test
        fun is1EqualTo1() {
            TestUtil.assert(1, 1)
        }

        @Test
        fun strings() {
            TestUtil.assert("Hello", "Hello")
        }

        @Test
        fun name() {
            TestUtil.assert(2, 1 + 1, "canAdd")
        }
    }

    class RunMultipleTimes {
        @Test
        fun countCalledTimes() {
            val goal = 1000
            var actual = 0

            TestUtil.runMultipleTimes(goal) {
                actual++
            }

            assert(goal == actual)
        }

        @Test
        fun callZeroTimes() {
            TestUtil.runMultipleTimes(0) {
                assert(false)
            }
        }

        @Test
        fun callOneTime() {
            var result = false

            TestUtil.runMultipleTimes(1) {
                result = true
            }

            assert(result)
        }

        @Test
        fun negative() {
            TestUtil.runMultipleTimes(-1) {
                assert(false)
            }
        }
    }

    @Test
    fun getHttpBinUrl() {
        assert(TestUtil.getHttpBinUrl().matches(Regex(
                "https?://.+\\.[a-z]{2,3}"
        )))
    }

    @Test
    fun getDani09DeUrl() {
        assert(TestUtil.getDani09DeUrl().matches(Regex(
                "https?://.+\\.[a-z]{2,3}"
        )))
    }
}
