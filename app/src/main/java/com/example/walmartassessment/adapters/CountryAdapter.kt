package com.example.walmartassessment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.walmartassessment.R
import com.example.walmartassessment.model.CountryModel

class CountryAdapter(): RecyclerView.Adapter<CountryAdapter.CountryViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.country_item,parent,false)

        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = asyncListDiffer.currentList[position]
        holder.bind(country)

    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }

    private val diffUtil = object : DiffUtil.ItemCallback<CountryModel>() {
        override fun areItemsTheSame(oldItem: CountryModel, newItem: CountryModel): Boolean {
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(oldItem: CountryModel, newItem: CountryModel): Boolean {
            return oldItem == newItem
        }

    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    fun saveData( dataResponse: List<CountryModel>){
        asyncListDiffer.submitList(dataResponse)
    }

    class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById(R.id.name)
        val code : TextView = itemView.findViewById(R.id.code)
        val capital : TextView = itemView.findViewById(R.id.capital)

        fun bind(country: CountryModel){
            val nameTxt = "${country.name}, ${country.region}"
            name.text = nameTxt
            code.text = country.code
            capital.text = country.capital
        }
    }
}