package com.travelme.customer.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    @SerializedName("id") var id : Int?= null,
    @SerializedName("order_id") var order_id : String?= null,
    @SerializedName("user") var user : User,
    @SerializedName("owner") var owner : Owner,
    @SerializedName("departure") var departure : Departure,
    @SerializedName("date") var date : String? = null,
    @SerializedName("hour") var hour : String? = null,
    @SerializedName("price") var price : Int? = null,
    @SerializedName("additional_price") var additional_price : Int? = null,
    @SerializedName("total_seat") var total_seat : Int? = null,
    @SerializedName("total_price") var total_price : Int? = null,
    @SerializedName("lat_pickup_point") var lat_pickup_point : String? = null,
    @SerializedName("lng_pickup_point") var lng_pickup_point : String? = null,
    @SerializedName("destination_point") var destination_location : String? = null,
    @SerializedName("lat_destination_point") var lat_destination_point : String? = null,
    @SerializedName("lng_destination_point") var lng_destination_point : String? = null,
    @SerializedName("reason_for_refusing") var reason_for_refusing : String? = null,
    @SerializedName("verify") var verify : String? = null,
    @SerializedName("status") var status : String? = null,
    @SerializedName("arrived") var arrived : Boolean? = false,
    @SerializedName("done") var done : Boolean? = false,
    @SerializedName("snap_token") var snap : String? = null
) : Parcelable

@Parcelize
data class CreateOrder(
    @SerializedName("id") var id : Int?= null,
    @SerializedName("owner_id") var owner_id : Int? = null,
    @SerializedName("departure_id") var departure_id : Int? = null,
    @SerializedName("car_id") var car_id : Int? = null,
    @SerializedName("date") var date : String? = null,
    @SerializedName("hour") var hour : String? = null,
    @SerializedName("pickup_point") var pickup_point : String? = null,
    @SerializedName("destination_point") var destination_point : String? = null,
    @SerializedName("payment") var payment : Boolean? = null,
    @SerializedName("seats") var seats : MutableList<Seat> = mutableListOf()
) : Parcelable