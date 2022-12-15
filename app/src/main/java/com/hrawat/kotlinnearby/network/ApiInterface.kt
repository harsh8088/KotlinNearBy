package com.hrawat.kotlinnearby.network

import com.hrawat.kotlinnearby.model.searchModel.SearchResults
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by hrawat on 11/2/2017.
 */
interface ApiInterface {

    @POST("json")
    abstract fun getNearByPlaces(
        @Query("location") location: String,
        @Query("radius") radius: String,
        @Query("type") type: String,
        @Query("keyword") keyword: String,
        @Query("key") apiServerKey: String
    ): Call<SearchResults>
}