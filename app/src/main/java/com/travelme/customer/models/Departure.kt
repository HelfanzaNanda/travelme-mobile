package com.travelme.customer.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Departure(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("from") var from: String? = null,
    @SerializedName("destination") var destination: String? = null,
    @SerializedName("logo") var logo: String? = null,
    @SerializedName("price") var price: Int? = null,
    @SerializedName("owner") var owner: Owner
) : Parcelable

@Parcelize
data class Destination(
    @SerializedName("destination") var destination: String? = null
) : Parcelable

