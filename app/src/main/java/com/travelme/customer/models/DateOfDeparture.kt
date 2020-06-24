package com.travelme.customer.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DateOfDeparture(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("date") var date: String? = null,
    @SerializedName("departure") var departure : Departure
) : Parcelable