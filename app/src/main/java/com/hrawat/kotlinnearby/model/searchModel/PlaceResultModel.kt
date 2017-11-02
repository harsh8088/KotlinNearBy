package com.hrawat.kotlinnearby.model.searchModel

import java.util.*

/**
 * Created by hrawat on 11/2/2017.
 */
data class PlaceResultModel(val geometry: GeometryModel,
                            val icon: String,
                            val id: String,
                            val name: String,
                            val opening_hours: OpeningHours,
                            val photos: ArrayList<PhotosModel>,
                            val place_id: String,
                            val rating: String,
                            val reference: String,
                            val scope: String,
                            val types: ArrayList<String>,
                            val vicinity: String)