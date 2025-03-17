package com.example.walmartassessment.handler

import android.content.Context
import com.example.walmartassessment.model.CountryModel
import com.example.walmartassessment.services.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CountryHandler {
    interface ResultListener<S> {
        fun onSuccess(response: S)
        fun onFail(any: String?)
        fun onError(e: Throwable?)
    }

    fun getCountries(resultListener: ResultListener<List<CountryModel>>){
        val call = ApiClient.countryService.getCountries()
        call.enqueue(object : Callback<List<CountryModel>> {
            override fun onResponse(call: Call<List<CountryModel>>, response: Response<List<CountryModel>>) {
                if (response.isSuccessful) {
                    val countryArray = response.body()
                    if (countryArray != null) {
                        resultListener.onSuccess(countryArray)
                    } else {
                        resultListener.onFail("Something went wrong-->${response.code()}: ${response.message()}")
                    }
                } else {
                    resultListener.onFail("Was not successful-->${response.code()}: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<CountryModel>>, t: Throwable) {
                resultListener.onError(t)
            }
        })
    }
}