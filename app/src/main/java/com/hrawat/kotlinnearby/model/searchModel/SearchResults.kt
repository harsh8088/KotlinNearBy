package com.hrawat.kotlinnearby.model.searchModel

import java.util.*

/**
 * Created by hrawat on 11/2/2017.
 */
data class SearchResults(
    val status: String,
    val results: ArrayList<PlaceResultModel>,
    val errorMessage: String
)