package com.hrawat.kotlinnearby.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.hrawat.kotlinnearby.R
import com.hrawat.kotlinnearby.model.ListModel
import com.hrawat.kotlinnearby.model.searchModel.PlaceResultModel

/**
 * Created by hrawat on 11/2/2017.
 */
class ListAdapter(private var context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = ArrayList<ListModel>()
    private var placeResultModelList = ArrayList<PlaceResultModel>()
    private val TYPE_LOADING = 1
    private val TYPE_LIST = TYPE_LOADING + 1
    private val TYPE_EMPTY = TYPE_LIST + 1
    private var isLoading: Boolean = false
    private var clickListener: ClickListener? = null

    interface ClickListener {
        fun onListClick(adapter: ListAdapter, position: Int)
    }

    fun setClickListener(listener: ClickListener) {
        this.clickListener = listener
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MyViewHolder -> {
                val details = items[position]
                //            PlaceResultModel placeResultModel = placeResultModelList.get(position);
                when (placeResultModelList[position].types[0]) {
                    "restaurant", "cafe" -> holder.relativeLayout.background =
                        ContextCompat.getDrawable(context, R.drawable.category_image)
                    "lodging" -> holder.relativeLayout.background =
                        ContextCompat.getDrawable(context, R.drawable.category_image)
                    "night_club", "bar" -> holder.relativeLayout.background =
                        ContextCompat.getDrawable(context, R.drawable.category_image)
                    "grocery_or_supermarket", "store" -> holder.relativeLayout.background =
                        ContextCompat.getDrawable(context, R.drawable.category_image)
                    "hospital" -> holder.relativeLayout.background =
                        ContextCompat.getDrawable(context, R.drawable.category_image)
                    "car_repair" -> holder.relativeLayout.background =
                        ContextCompat.getDrawable(context, R.drawable.category_image)
                    "gym", "health" -> holder.relativeLayout.background =
                        ContextCompat.getDrawable(context, R.drawable.category_image)
                    "transit_station", "bus_station" -> holder.relativeLayout.background =
                        ContextCompat.getDrawable(context, R.drawable.category_image)
                    "gas_station" -> holder.relativeLayout.background =
                        ContextCompat.getDrawable(context, R.drawable.category_image)
                    "police" -> holder.relativeLayout.background =
                        ContextCompat.getDrawable(context, R.drawable.category_image)
                    "school" -> holder.relativeLayout.background =
                        ContextCompat.getDrawable(context, R.drawable.category_image)
                    "shopping_mall" -> holder.relativeLayout.background =
                        ContextCompat.getDrawable(context, R.drawable.category_image)
                    else -> holder.relativeLayout.background =
                        ContextCompat.getDrawable(context, R.drawable.orangebox)
                }
                holder.tvName.text = details.name
                holder.tvAddress.text = details.address
                holder.relativeLayout.setOnClickListener {
                    clickListener?.onListClick(this@ListAdapter, holder.adapterPosition)
                }
            }
            is EmptyViewHolder -> {
                val emptyViewHolder = holder
            }
            is LoadingViewHolder -> {
                val loadingViewHolder = holder
            }
        }
    }

    override fun getItemCount(): Int {
        return if (isLoading)
            1
        else
            if (items.size == 0) 1 else items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_LIST -> MyViewHolder(
                LayoutInflater.from(context)
                    .inflate(R.layout.item_list, parent, false)
            )
            TYPE_LOADING -> LoadingViewHolder(
                LayoutInflater.from(context)
                    .inflate(R.layout.item_loading, parent, false)
            )
            TYPE_EMPTY -> EmptyViewHolder(
                LayoutInflater.from(context)
                    .inflate(R.layout.item_empty, parent, false)
            )
            else -> EmptyViewHolder(
                LayoutInflater.from(context)
                    .inflate(R.layout.item_empty, parent, false)
            )
        }
    }


    fun replaceAll(listModels: ArrayList<ListModel>, places: ArrayList<PlaceResultModel>) {
        this.items.clear()
        this.items.addAll(listModels)
        this.placeResultModelList.clear()
        this.placeResultModelList.addAll(places)
        this.isLoading = false
        //        this.notifyDataSetChanged();
    }

    fun clearAll() {
        this.isLoading = false
        this.items.clear()
        this.notifyDataSetChanged()
    }


    override fun getItemViewType(position: Int): Int {
        if (isLoading)
            return TYPE_LOADING
        return if (items.size == 0)
            TYPE_EMPTY
        else
            TYPE_LIST
    }

    fun getPlace(position: Int): String? {
        val gson = Gson()
        return gson.toJson(placeResultModelList[position])
    }

    fun startLoading() {
        this.isLoading = true
        notifyDataSetChanged()
    }

    private inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView
        val tvAddress: TextView
        val relativeLayout: RelativeLayout

        init {
            tvName = itemView.findViewById(R.id.title)
            tvAddress = itemView.findViewById(R.id.address)
            relativeLayout = itemView.findViewById(R.id.rl_item)
        }
    }

    private inner class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


}