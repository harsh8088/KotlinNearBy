package com.hrawat.kotlinnearby.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hrawat.kotlinnearby.R
import com.hrawat.kotlinnearby.model.NearByCategory
import java.util.*

/**
 * Created by hrawat on 11/1/2017.
 */
class CategoryAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var categoryListener: CategoryListener? = null
    private  var nearByCategoryList= ArrayList<NearByCategory>()

    interface CategoryListener {

        fun onCategoryClick(categoryAdapter: CategoryAdapter, categoryName: String)
    }

    fun setCategoryListener(listener: CategoryListener) {
        this.categoryListener = listener
    }


    override fun getItemCount(): Int {
        return nearByCategoryList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val details = nearByCategoryList[position]
        val viewHolder = holder as CategoryViewHolder
//        viewHolder.icon.(details.getIcon());
//        viewHolder.background
        viewHolder.categoryName.text=details.name
        viewHolder.background.setOnClickListener(View.OnClickListener
        { categoryListener?.onCategoryClick(this@CategoryAdapter, details.name) })
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_nearby_category_list, parent,false)
        return CategoryViewHolder(view)
    }

    fun addAllCategories(nearByCategories: ArrayList<NearByCategory>) {
        this.nearByCategoryList.clear()
        this.nearByCategoryList.addAll(nearByCategories)
        this.notifyDataSetChanged()
    }


    private inner class CategoryViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {

        val background: ImageView = view.findViewById<ImageView>(R.id.iv_category_background)
        val icon: ImageView = view.findViewById<ImageView>(R.id.iv_category_icon)
        val categoryName: TextView = view.findViewById<TextView>(R.id.tv_category_name)

    }

}