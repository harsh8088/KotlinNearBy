package com.hrawat.kotlinnearby.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by hrawat on 11/2/2017.
 */
class ApiClient {
    companion object {
        val BASE_URL = "http://api.themoviedb.org/3/"
        val PLACE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/"

        lateinit var retrofit: Retrofit

        fun getClient(): Retrofit {
            retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            return retrofit
        }

        fun getPlacesClient(): Retrofit {
            retrofit = Retrofit.Builder()
                    .baseUrl(PLACE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            return retrofit
        }
    }


}