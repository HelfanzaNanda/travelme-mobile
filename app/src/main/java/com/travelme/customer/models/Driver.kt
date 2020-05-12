package com.travelme.customer.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Driver(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("nik") var nik: String? = null,
    @SerializedName("sim") var sim: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("avatar") var avatar: String? = null,
    @SerializedName("address") var address: String? = null,
    @SerializedName("telephone") var telp: String? = null
) : Parcelable