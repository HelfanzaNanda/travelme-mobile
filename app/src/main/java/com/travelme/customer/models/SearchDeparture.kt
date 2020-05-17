package com.travelme.customer.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchDeparture(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("from") var from: String? = null,
    @SerializedName("destination") var destination: String? = null,
    @SerializedName("photo_destination") var photo_destination: String? = null,
    @SerializedName("price") var price: Int? = null,
    @SerializedName("owner") var owner: Owner,
    @SerializedName("date") var date: DateOfDeparture
) : Parcelable