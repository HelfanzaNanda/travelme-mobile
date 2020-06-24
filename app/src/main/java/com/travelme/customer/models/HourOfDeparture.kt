package com.travelme.customer.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HourOfDeparture(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("hour") var hour: String? = null,
    @SerializedName("seat") var seat: Int? = null,
    @SerializedName("remaining_seat") var remaining_seat: Int? = null,
    @SerializedName("date") var date: DateOfDeparture
) : Parcelable