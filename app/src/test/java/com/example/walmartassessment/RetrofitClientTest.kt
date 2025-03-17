package com.example.walmartassessment

import com.example.walmartassessment.services.ApiClient
import org.junit.Test
import retrofit2.Retrofit
import com.example.walmartassessment.services.RetrofitClient
class RetrofitClientTest {
    @Test
    fun testRetrofitInstance() {
        //Get an instance of Retrofit
        val instance: Retrofit = RetrofitClient.retrofit
        //Assert that, Retrofit's base url matches to our BASE_URL
        assert(instance.baseUrl().url().toString() == "https://gist.githubusercontent.com/")
    }

    @Test
    fun testCountryService() {
        val service = ApiClient.countryService

    }
}