package com.example.walmartassessment

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.walmartassessment.adapters.CountryAdapter
import com.example.walmartassessment.databinding.ActivityMainBinding
import com.example.walmartassessment.handler.CountryHandler
import com.example.walmartassessment.model.CountryModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        val layoutManager = LinearLayoutManager(this)

        recyclerView.layoutManager = layoutManager
        recyclerView.setItemViewCacheSize(10)

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                baseContext,
                layoutManager.orientation
            )
        )
    }

    override fun onResume() {
        super.onResume()

        val countryHandler = CountryHandler()
        countryHandler.getCountries(baseContext, resultListener)

    }

    private val resultListener = object : CountryHandler.ResultListener<List<CountryModel>>{
        override fun onSuccess(response: List<CountryModel>) {
            val recyclerView: RecyclerView = findViewById(R.id.recycler_view)

            if(recyclerView.adapter == null){
                recyclerView.adapter = CountryAdapter()
            }

            val adapter: CountryAdapter = recyclerView.adapter as CountryAdapter
            adapter.saveData(response)


            adapter.saveData(response)
        }

        override fun onFail(any: String?) {
            Log.e("MainActivity", any.toString())
        }

        override fun onError(e: Throwable?) {
          Log.e("MainActivity", e?.message.toString())
        }

    }

}