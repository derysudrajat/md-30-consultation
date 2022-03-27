package com.example.githubuser

import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val cities = listOf(
            "Jeddah",
            "Bengaluru",
            "Shenzhen",
            "Abu Dhabi",
            "Mountain View",
            "Tripoli",
            "Bengaluru",
            "Lima",
            "Mandalay",
            "Tripoli"
        )
        val oneWordCities = cities.toSet().toList().filter { !it.contains(" ") }.sorted()
        println(oneWordCities[1])
    }
}