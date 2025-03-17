package com.example.walmartassessment

import com.example.walmartassessment.model.CountryModel
import com.example.walmartassessment.services.CountryService
import org.junit.Test
import org.junit.After
import org.junit.Before
import org.junit.Assert.assertEquals
import retrofit2.Retrofit
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class RetrofitClientTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var countryService: CountryService
    private val TIMEOUT = 5L
    private val successResponse = """
  {
    "capital": "Kabul",
    "code": "AF",
    "currency": {
      "code": "AFN",
      "name": "Afghan afghani",
      "symbol": "؋"
    },
    "flag": "https://restcountries.eu/data/afg.svg",
    "language": {
      "code": "ps",
      "name": "Pashto"
    },
    "name": "Afghanistan",
    "region": "AS"
  },
  {
    "capital": "Mariehamn",
    "code": "AX",
    "currency": {
      "code": "EUR",
      "name": "Euro",
      "symbol": "€"
    },
    "flag": "https://restcountries.eu/data/ala.svg",
    "language": {
      "code": "sv",
      "name": "Swedish"
    },
    "name": "Åland Islands",
    "region": "EU"
  }
    """.trimIndent()

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        countryService = retrofit.create(CountryService::class.java)

    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testApiSuccess() {
        mockWebServer.enqueue(MockResponse().setBody(successResponse).setResponseCode(200))

        val latch = CountDownLatch(1)
        var result: List<CountryModel> = mutableListOf()

        countryService.getCountries().enqueue(object : Callback<List<CountryModel>> {
            override fun onResponse(
                call: Call<List<CountryModel>>,
                response: Response<List<CountryModel>>
            ) {
                if (response.isSuccessful) {
                    result = response.body()!!
                }
                latch.countDown()
            }

            override fun onFailure(call: Call<List<CountryModel>?>, t: Throwable) {
                latch.countDown()
            }
        })

        latch.await(TIMEOUT, TimeUnit.SECONDS)
        assertEquals(CountryModel("Afghanistan", "AS","AF", "Kabul"), result.first())
    }

    @Test
    fun testApiFailure(){
        mockWebServer.enqueue(MockResponse().setResponseCode(404))

        val latch = CountDownLatch(1)
        var isFailureCalled = false

        countryService.getCountries().enqueue(object : Callback<List<CountryModel>> {
            override fun onResponse(
                call: Call<List<CountryModel>>,
                response: Response<List<CountryModel>>
            ) {
                latch.countDown()
            }

            override fun onFailure(call: Call<List<CountryModel>?>, t: Throwable) {
                isFailureCalled = true
                latch.countDown()
            }
        })

        latch.await(TIMEOUT, TimeUnit.SECONDS)
        assertEquals(true, isFailureCalled)
    }

    @Test
    fun testApiErrorBody(){
        mockWebServer.enqueue(MockResponse().setResponseCode(400).setBody("""{"error": "Bad Request"}"""))

        val latch = CountDownLatch(1)
        var responseCode = 0

        countryService.getCountries().enqueue(object : Callback<List<CountryModel>> {
            override fun onResponse(
                call: Call<List<CountryModel>>,
                response: Response<List<CountryModel>>
            ) {
                responseCode = response.code()
                latch.countDown()
            }

            override fun onFailure(call: Call<List<CountryModel>?>, t: Throwable) {
                latch.countDown()
            }
        })

        latch.await(TIMEOUT, TimeUnit.SECONDS)
        assertEquals(400, responseCode);
    }

}
