package com.travelme.customer.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    @SerializedName("id") var id : Int?= null,
    @SerializedName("user") var user : User,
    @SerializedName("owner") var owner : Owner,
    @SerializedName("departure") var departure : Departure,
    @SerializedName("date") var date : String? = null,
    @SerializedName("hour") var hour : String? = null,
    @SerializedName("price") var price : Int? = null,
    @SerializedName("total_seat") var total_seat : Int? = null,
    @SerializedName("pickup_location") var pickup_location : String? = null,
    @SerializedName("destination_location") var destination_location : String? = null
) : Parcelable