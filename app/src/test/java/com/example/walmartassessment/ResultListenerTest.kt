package com.example.walmartassessment

import org.junit.Test
import org.mockito.Mockito.*
import org.junit.Assert.assertEquals
import android.content.Context
import com.example.walmartassessment.handler.CountryHandler
import com.example.walmartassessment.model.CountryModel
import com.example.walmartassessment.services.ApiClient
import com.example.walmartassessment.services.CountryService
import org.junit.Before
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.invocation.InvocationOnMock
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultListenerTest {

    @Before
    fun setUp(){
        MockitoAnnotations.openMocks(this)
    }


    @Test
    fun getCountries_onSuccess_withValidResponse() {
        val context = mock(Context::class.java)
        val resultListener = mock(CountryHandler.ResultListener::class.java) as CountryHandler.ResultListener<List<CountryModel>>
        val countryService = mock(CountryService::class.java)
        val apiClient = mock(ApiClient::class.java)
        val call = mock(Call::class.java) as Call<List<CountryModel>>
        val response = mock(Response::class.java) as Response<List<CountryModel>>

        val countryList = listOf(CountryModel("United States of America","NA","US", "Washington D.C."), CountryModel("Canada","NA","CA", "Ottawa"))

        `when`(apiClient.countryService).thenReturn(countryService)
        `when`(countryService.getCountries()).thenReturn(call)
        `when`(call.enqueue(any())).thenAnswer { invocation ->
            val callback = invocation.arguments[0] as Callback<List<CountryModel>>
            `when`(response.isSuccessful).thenReturn(true)
            `when`(response.body()).thenReturn(countryList)
            callback.onResponse(call, response)
            null
        }

        val handler = CountryHandler()
        handler.getCountries(resultListener)

        verify(resultListener).onSuccess(countryList)
        verify(resultListener, never()).onFail(any())
        verify(resultListener, never()).onError(any())
    }

    @Test
    fun getCountries_onFail_withNullBody() {
        val context = mock(Context::class.java)
        val resultListener = mock(CountryHandler.ResultListener::class.java) as CountryHandler.ResultListener<List<CountryModel>>
        val apiClient = mock(ApiClient::class.java)
        val countryService = mock(CountryService::class.java)
        val call = mock(Call::class.java) as Call<List<CountryModel>>
        val response = mock(Response::class.java) as Response<List<CountryModel>>

        `when`(apiClient.countryService).thenReturn(countryService)
        `when`(countryService.getCountries()).thenReturn(call)
        `when`(call.enqueue(any())).thenAnswer { invocation ->
            val callback = invocation.arguments[0] as Callback<List<CountryModel>>
            `when`(response.isSuccessful).thenReturn(true)
            `when`(response.body()).thenReturn(null)
            `when`(response.code()).thenReturn(404)
            `when`(response.message()).thenReturn("Not Found")
            callback.onResponse(call, response)
            null
        }

        val handler = CountryHandler()
        handler.getCountries(resultListener)

        verify(resultListener).onFail(anyString())
        verify(resultListener, never()).onSuccess(any())
        verify(resultListener, never()).onError(any())
    }

    @Test
    fun getCountries_onFail_withUnsuccessfulResponse() {
        val context = mock(Context::class.java)
        val resultListener = mock(CountryHandler.ResultListener::class.java) as CountryHandler.ResultListener<List<CountryModel>>
        val apiClient = mock(ApiClient::class.java)
        val countryService = mock(CountryService::class.java)
        val call = mock(Call::class.java) as Call<List<CountryModel>>
        val response = mock(Response::class.java) as Response<List<CountryModel>>

        `when`(apiClient.countryService).thenReturn(countryService)
        `when`(countryService.getCountries()).thenReturn(call)
        `when`(call.enqueue(any())).thenAnswer { invocation ->
            val callback = invocation.arguments[0] as Callback<List<CountryModel>>
            `when`(response.isSuccessful).thenReturn(false)
            `when`(response.code()).thenReturn(500)
            `when`(response.message()).thenReturn("Internal Server Error")
            callback.onResponse(call, response)
            null
        }

        val handler = CountryHandler()
        handler.getCountries(resultListener)

        verify(resultListener).onFail(anyString())
        verify(resultListener, never()).onSuccess(any())
        verify(resultListener, never()).onError(any())
    }

    @Test
    fun getCountries_onError_withFailure() {
        val context = mock(Context::class.java)
        val resultListener = mock(CountryHandler.ResultListener::class.java) as CountryHandler.ResultListener<List<CountryModel>>
        val apiClient = mock(ApiClient::class.java)
        val countryService = mock(CountryService::class.java)
        val call = mock(Call::class.java) as Call<List<CountryModel>>
        val throwable = Throwable("Network Error")


        `when`(apiClient.countryService).thenReturn(countryService)
        `when`(countryService.getCountries()).thenReturn(call)
        `when`(call.enqueue(any())).thenAnswer { (it.getArgument(0) as Callback<List<CountryModel>>).onFailure(call,throwable) }

        val handler = CountryHandler()
        handler.getCountries(resultListener)

        verify(resultListener).onError(throwable) // Verify with the same throwable
        verify(resultListener, never()).onSuccess(any())
        verify(resultListener, never()).onFail(any())
    }
}