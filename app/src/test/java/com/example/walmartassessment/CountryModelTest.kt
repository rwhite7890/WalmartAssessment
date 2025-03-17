package com.example.walmartassessment

import com.example.walmartassessment.model.CountryModel
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.BeforeTest

class CountryModelTest {

    private lateinit var model: CountryModel

    @BeforeTest
    fun setup() {
        model = CountryModel("United States of America","NA","US","Washington D.C.")
    }

    @Test
    fun testCountryCode() {
        assertEquals("US", model.code)
    }

    @Test
    fun testCountryName() {
        assertEquals("United States of America", model.name)
    }

    @Test
    fun testCountryRegion() {
        assertEquals("NA", model.region)
    }

    @Test
    fun testCountryCapital() {
        assertEquals("Washington D.C.", model.capital)
    }

    
}