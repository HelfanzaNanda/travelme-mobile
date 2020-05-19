package com.travelme.customer.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @SerializedName("id") var id : Int? =null,
    @SerializedName("name") var name : String? =null,
    @SerializedName("email") var email : String? =null,
    @SerializedName("password") var password : String? =null,
    @SerializedName("photo") var photo : String? =null,
    @SerializedName("telp") var telp : String? =null,
    @SerializedName("api_token") var token : String? =null
) : Parcelable