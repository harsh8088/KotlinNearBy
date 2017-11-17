package com.hrawat.kotlinnearby

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import com.hrawat.kotlinnearby.HomeActivity.Companion.LOCATION_LONGITUTE
import com.hrawat.kotlinnearby.activity.DetailsActivity
import com.hrawat.kotlinnearby.activity.DetailsActivity.Companion.BUNDLE_EXTRA_PLACE
import com.hrawat.kotlinnearby.adapter.ListAdapter
import com.hrawat.kotlinnearby.model.FilterModel
import com.hrawat.kotlinnearby.model.ListModel
import com.hrawat.kotlinnearby.model.searchModel.PlaceResultModel
import com.hrawat.kotlinnearby.model.searchModel.SearchResults
import com.hrawat.kotlinnearby.network.ApiClient
import com.hrawat.kotlinnearby.network.ApiInterface
import com.orhanobut.hawk.Hawk
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ListActivity : AppCompatActivity() {
    companion object {
        val BUNDLE_EXTRA_CATEGORY_NAME: String = "BUNDLE_EXTRA_CATEGORY_NAME"
    }


    private val TAG = this.javaClass.simpleName
    private lateinit var listAdapter: ListAdapter
    private lateinit var etSearch: EditText
    private var categoryName = ""
    lateinit var recyclerViewList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val bundle = intent.extras
        if (bundle?.get(BUNDLE_EXTRA_CATEGORY_NAME) != null) {
            categoryName = bundle.get(BUNDLE_EXTRA_CATEGORY_NAME) as String
        }
        initView()
        if (bundle != null && bundle.containsKey(BUNDLE_EXTRA_CATEGORY_NAME)) {
            etSearch.setText(categoryName)
        }
        toolbar.setNavigationOnClickListener { finish() }
    }


    private fun initView() {

        recyclerViewList = findViewById(R.id.recycler_view)
        listAdapter = ListAdapter(this@ListActivity)
        val mLayoutManager = LinearLayoutManager(this)
        recyclerViewList.layoutManager = mLayoutManager
        recyclerViewList.adapter = listAdapter
        etSearch = findViewById(R.id.et_action_search)
        val imageFilter = findViewById<ImageView>(R.id.iv_filter_search)
        imageFilter.setOnClickListener { showFilterDialog() }
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.isEmpty())
                    listAdapter.clearAll()
            }

            override fun afterTextChanged(editable: Editable) {
                Handler().postDelayed({
                    if (editable.length >= 3) {
                        val searchfor = editable.toString()
                        searchNearby(searchfor, searchfor, "5000")
                    }
                }, 1500)
            }
        })
        listAdapter.setClickListener(object : ListAdapter.ClickListener {
            override fun onListClick(adapter: ListAdapter, position: Int) {
                val intent = Intent(this@ListActivity, DetailsActivity::class.java)
                if (listAdapter.getPlace(position) != null)
                    intent.putExtra(BUNDLE_EXTRA_PLACE, listAdapter.getPlace(position))
                startActivity(intent)
            }
        })

    }


    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        val context = recyclerView.context
        val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)
        recyclerView.layoutAnimation = controller
        recyclerView.adapter.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }

    private fun showFilterDialog() {
        val dialog = Dialog(this)
        dialog.setTitle("Filter by Distance")
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.filter_dialog)
        val textSeekBar = dialog.findViewById<TextView>(R.id.tv_seek_bar_max)
        val btnApply = dialog.findViewById<Button>(R.id.btn_apply)
        val btnCancel = dialog.findViewById<Button>(R.id.btn_cancel)
        val seekBar = dialog.findViewById<SeekBar>(R.id.seekbar_distance)
        dialog.show()
        if (Hawk.contains("FILTER")) {
            val filterModel = Hawk.get<FilterModel>("FILTER")
            if (filterModel.isApplied) {
                var distance = Integer.valueOf(filterModel.distance)
                distance /= 1000
                textSeekBar.setText(distance.toString())
                seekBar.setProgress(distance)
            } else {
                textSeekBar.setText("5")
                seekBar.setProgress(5)
            }
        }
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (i >= 1)
                    textSeekBar.setText(i.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        btnApply.setOnClickListener(View.OnClickListener {
            var distance = Integer.valueOf(textSeekBar.getText().toString())!!
            distance = distance!! * 1000
            val filterModel = FilterModel(distance.toString(), true)
            Hawk.put<FilterModel>("FILTER", filterModel)
            searchNearby(etSearch.text.toString(), etSearch.text.toString(),
                    distance.toString())
            dialog.dismiss()
        })
        btnCancel.setOnClickListener(View.OnClickListener { dialog.dismiss() })
    }

    private fun searchNearby(searchfor: String, keyword: String, searchWithin: String) {
        var searchWithin = searchWithin
        if (Hawk.contains("FILTER")) {
            val filterModel = Hawk.get<FilterModel>("FILTER")
            if (filterModel.isApplied) {
                val distance = Integer.valueOf(filterModel.distance)
                searchWithin = distance.toString()
            }
        }
        listAdapter.startLoading()
        val LatLongString = String.format("%s,%s", Hawk.get(HomeActivity.LOCATION_LATITUDE),
                Hawk.get(LOCATION_LONGITUTE))
        val apiService = ApiClient.getPlacesClient().create(ApiInterface::class.java)
        val call = apiService.getNearByPlaces(LatLongString, searchWithin,
                searchfor, keyword, "AIzaSyChQ0n-vud41n-_pz-nXBiDJTQrG7F0CJs")
        call.enqueue(object : Callback<SearchResults> {
            override fun onResponse(call: Call<SearchResults>, response: Response<SearchResults>) {
                val status = response.body()!!.status
                when (status) {
                    "OK" -> {
                        val places = response.body()!!.results
                        val listModels = ArrayList<ListModel>()
                        for (placeResultModel in places) {
                            listModels.add(ListModel(placeResultModel.name,
                                    placeResultModel.vicinity))
                        }
                        listAdapter.replaceAll(listModels, places)
                        runLayoutAnimation(recyclerViewList)
                        Log.d(TAG, "Number of Places : " + places.size)
                    }
                    "ZERO_RESULTS" -> {
                        listAdapter.clearAll()
                        Toast.makeText(this@ListActivity, "No such results!!!",
                                Toast.LENGTH_SHORT).show()
                    }
                    "REQUEST_DENIED" -> {
                        listAdapter.clearAll()
                        Log.d(TAG, "Access Denied : " + response.body()!!.errorMessage)
                    }
                    else -> {
                        listAdapter.clearAll()
                        Log.d(TAG, "Access Denied : " + response.body()!!.errorMessage)
                    }
                }
            }

            override fun onFailure(call: Call<SearchResults>, t: Throwable) {
                listAdapter.replaceAll(ArrayList<ListModel>(), ArrayList<PlaceResultModel>())
                runLayoutAnimation(recyclerViewList)
                Log.d(TAG, "Error : " + t.toString())
            }
        })
    }

}
