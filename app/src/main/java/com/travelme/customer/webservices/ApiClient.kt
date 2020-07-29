package com.travelme.customer.webservices

import com.travelme.customer.models.*
import com.travelme.customer.utilities.Constants
import com.travelme.customer.utilities.WrappedListResponse
import com.travelme.customer.utilities.WrappedResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

class ApiClient {
    companion object {
        private var retrofit: Retrofit? = null

        private val opt = OkHttpClient.Builder().apply {
            connectTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
        }.build()

        private fun getClient(): Retrofit {
            return if (retrofit == null) {
                retrofit = Retrofit.Builder().apply {
                    client(opt)
                    baseUrl(Constants.END_POINT)
                    addConverterFactory(GsonConverterFactory.create())
                }.build()
                retrofit!!
            } else {
                retrofit!!
            }
        }

        fun instance() = getClient().create(ApiService::class.java)
    }
}

interface ApiService{

    @GET("user/profile")
    fun profile(@Header("Authorization") token : String) : Call<WrappedResponse<User>>

    @FormUrlEncoded
    @POST("user/register")
    fun register(
        @Field("name") name : String,
        @Field("email") email : String,
        @Field("password") password : String,
        @Field("telp") telp : String,
        @Field("fcm_token") fcmToken : String
    ) : Call<WrappedResponse<User>>

    @FormUrlEncoded
    @POST("user/login")
    fun login(
        @Field("email") email : String,
        @Field("password") password : String
    ) : Call<WrappedResponse<User>>

    @FormUrlEncoded
    @POST("user/profile/update")
    fun updateProfile(
        @Header("Authorization") token : String,
        @Field("name") name : String,
        @Field("password") password : String
    ) : Call<WrappedResponse<User>>

    @Multipart
    @POST("user/profile/update/photo")
    fun updatePhotoProfile(
        @Header("Authorization") token : String,
        @Part image : MultipartBody.Part
    ) :Call<WrappedResponse<User>>

    @FormUrlEncoded
    @POST("user/password/email")
    fun forgotPassword(
        @Field("email") email : String
    ) :Call<WrappedResponse<User>>

    @GET("domicile")
    fun destination() : Call<WrappedListResponse<Destination>>


    @FormUrlEncoded
    @POST("hour")
    fun searchHours(
        @Field("from") from : String,
        @Field("destination") destination : String,
        @Field("date") date : String
    ) : Call<WrappedListResponse<HourOfDeparture>>

    @FormUrlEncoded
    @POST("owner")
    fun searchOwners(
        @Field("from") from : String,
        @Field("destination") destination : String,
        @Field("date") date : String,
        @Field("hour") hour : String
    ) : Call<WrappedListResponse<HourOfDeparture>>


    @FormUrlEncoded
    @POST("order/store")
    fun storeOrder(
        @Header("Authorization") token : String,
        @Field("owner_id") owner_id : Int,
        @Field("departure_id") departure_id : Int,
        @Field("date") date : String,
        @Field("hour") hour : String,
        @Field("price") price : Int,
        @Field("total_seat") total_seat : Int,
        @Field("pickup_point") pickup_point : String,
        @Field("lat_pickup_point") lat_pickup_point : String,
        @Field("lng_pickup_point") lng_pickup_point : String,
        @Field("destination_point") destination_point : String,
        @Field("lat_destination_point") lat_destination_point : String,
        @Field("lng_destination_point") lng_destination_point : String
    ) : Call<WrappedResponse<Order>>

    @GET("order")
    fun getMyOrders(@Header("Authorization") token : String) : Call<WrappedListResponse<Order>>

    @GET("order/{id}/cancel")
    fun cancelorder(
        @Header("Authorization") token : String,
        @Path("id") id : Int
    ) : Call<WrappedResponse<Order>>

    @GET("order/{id}/confirm")
    fun confirmOrder(
        @Header("Authorization") token : String,
        @Path("id") id : Int
    ) : Call<WrappedResponse<Order>>

    @FormUrlEncoded
    @POST("order/{id}/update")
    fun updatestatusorder(
        @Header("Authorization") token : String,
        @Path("id") id : Int,
        @Field("status") status : String
    ) : Call<WrappedResponse<Order>>

    @GET("order/arrived")
    fun driverArriver(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Order>>

    @GET("order/verify")
    fun orderVerify(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Order>>

    @FormUrlEncoded
    @POST("snap")
    fun snap(
        @Header("Authorization") token : String,
        @Field("departure_id") departure_id : String,
        @Field("price") price : Int,
        @Field("total_seat") total_seat : Int,
        @Field("date") date : String
    ) : Call<WrappedResponse<Order>>
}
