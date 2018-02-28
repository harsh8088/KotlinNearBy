package com.hrawat.kotlinnearby.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.gson.Gson
import com.hrawat.kotlinnearby.R
import com.hrawat.kotlinnearby.model.ListModel
import com.hrawat.kotlinnearby.model.searchModel.PlaceResultModel
import java.util.*

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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is MyViewHolder) {
            val details = items[position]
            //            PlaceResultModel placeResultModel = placeResultModelList.get(position);
            when (placeResultModelList[position].types.get(0)) {
                "restaurant", "cafe" -> holder.relativeLayout.background = context.getResources()
                        .getDrawable(R.drawable.category_image)
                "lodging" -> holder.relativeLayout.background = context.getResources()
                        .getDrawable(R.drawable.category_image)
                "night_club", "bar" -> holder.relativeLayout.background = context.getResources()
                        .getDrawable(R.drawable.category_image)
                "grocery_or_supermarket", "store" -> holder.relativeLayout.background = context.getResources()
                        .getDrawable(R.drawable.category_image)
                "hospital" -> holder.relativeLayout.background = context.getResources()
                        .getDrawable(R.drawable.category_image)
                "car_repair" -> holder.relativeLayout.background = context.getResources()
                        .getDrawable(R.drawable.category_image)
                "gym", "health" -> holder.relativeLayout.background = context.getResources()
                        .getDrawable(R.drawable.category_image)
                "transit_station", "bus_station" -> holder.relativeLayout.background = context.getResources()
                        .getDrawable(R.drawable.category_image)
                "gas_station" -> holder.relativeLayout.background = context.getResources()
                        .getDrawable(R.drawable.category_image)
                "police" -> holder.relativeLayout.background = context.getResources()
                        .getDrawable(R.drawable.category_image)
                "school" -> holder.relativeLayout.background = context.getResources()
                        .getDrawable(R.drawable.category_image)
                "shopping_mall" -> holder.relativeLayout.background = context.getResources()
                        .getDrawable(R.drawable.category_image)
                else -> holder.relativeLayout.background = context.getResources()
                        .getDrawable(R.drawable.orangebox)
            }
            holder.tvName.setText(details.name)
            holder.tvAddress.setText(details.address)
            holder.relativeLayout.setOnClickListener {
                clickListener?.
                        onListClick(this@ListAdapter, holder.getPosition())
            }
        } else if (holder is EmptyViewHolder) {
            val emptyViewHolder = holder
        } else if (holder is LoadingViewHolder) {
            val loadingViewHolder = holder
        }
    }

    override fun getItemCount(): Int {
        return if (isLoading)
            1
        else
            if (items.size == 0) 1 else items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_LIST -> return MyViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.item_list, parent, false))
            TYPE_LOADING -> return LoadingViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.item_loading, parent, false))
            TYPE_EMPTY -> return EmptyViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.item_empty, parent, false))
            else -> return EmptyViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.item_empty, parent, false))
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
        if (placeResultModelList != null) {
            val gson = Gson()
            return gson.toJson(placeResultModelList[position])
        }
        return null
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