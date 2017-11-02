package com.hrawat.kotlinnearby.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.hrawat.kotlinnearby.HomeActivity.Companion.LOCATION_LATITUDE
import com.hrawat.kotlinnearby.HomeActivity.Companion.LOCATION_LONGITUTE
import com.hrawat.kotlinnearby.R
import com.hrawat.kotlinnearby.model.searchModel.PlaceResultModel
import com.orhanobut.hawk.Hawk

class DetailsActivity : AppCompatActivity(), OnMapReadyCallback {


    companion object {
        val BUNDLE_EXTRA_PLACE = "BUNDLE_EXTRA_PLACE"
    }

    private lateinit var placeResultModel: PlaceResultModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        val bundle = intent.extras
        if (bundle != null && bundle.containsKey(BUNDLE_EXTRA_PLACE)) {
            val gson = Gson()
            placeResultModel = gson.fromJson(bundle.getString(BUNDLE_EXTRA_PLACE), PlaceResultModel::class.java)
        }
        init()
    }

    private fun init() {
        var mapFragment = getSupportFragmentManager()
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val textViewName = findViewById<TextView>(R.id.tv_location_name)
        val textViewRating = findViewById<TextView>(R.id.tv_rating)
        val imageViewStatus = findViewById<ImageView>(R.id.iv_status)

        textViewName.text = placeResultModel.name
        textViewRating.text = placeResultModel.rating
        if (placeResultModel.opening_hours != null) {
            if (placeResultModel?.opening_hours?.open_now)
                imageViewStatus.background = resources.getDrawable(R.drawable.ic_open)
            else
                imageViewStatus.background = resources.getDrawable(R.drawable.ic_closed)
        }


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap?) {
        val currentLocation = LatLng(Hawk.get(LOCATION_LATITUDE),
                Hawk.get(LOCATION_LONGITUTE))
        val lat = placeResultModel.geometry.location.lat.toDouble()
        val lng = placeResultModel.geometry.location.lng.toDouble()
        val latLng = LatLng(lat, lng)
        googleMap?.addMarker(MarkerOptions().position(latLng).title("Destination"))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }
}
