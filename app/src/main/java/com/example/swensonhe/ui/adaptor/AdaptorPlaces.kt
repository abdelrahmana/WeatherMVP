package com.example.swensonhe.ui.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.swensonhe.databinding.AdaptorOneItemPlacesBinding

class AdaptorPlaces(
    val arrayList: ArrayList<String>,
    val  callBackSelectionItem: (String,String) -> Unit) : RecyclerView.Adapter<AdaptorPlaces.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            AdaptorOneItemPlacesBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])

    }
    fun updateCurrentList(arrayList: ArrayList<String>) {
        this.arrayList.clear()
        this.arrayList.addAll(arrayList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
    inner class ViewHolder(
        val itemBinding: AdaptorOneItemPlacesBinding
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {


        fun bindItems(
            itemData: String
        ) {
            itemBinding.placesText.text = itemData
            itemBinding.placesText.setOnClickListener {
                callBackSelectionItem.invoke(itemData,"3")
            }

        }
    }

}