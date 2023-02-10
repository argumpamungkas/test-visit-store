package com.argump.visitstore.model

import java.io.Serializable

data class ResponseStore(
    val stores: List<DataStore>,
    val status: String,
    val message: String
)

data class DataStore(
    val store_id: String,
    val store_code: String,
    val store_name: String,
    val address: String,
    val dc_id: String,
    val dc_name: String,
    val account_id: String,
    val account_name: String,
    val subchannel_id: String,
    val subchannel_name: String,
    val channel_id: String,
    val channel_name: String,
    val area_id: String,
    val area_name: String,
    val region_id: String,
    val region_name: String,
    val latitude: String,
    val longitude: String,
    var isVisit: Boolean = false,
) : Serializable