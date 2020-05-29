package com.travelme.customer.webservices

import com.travelme.customer.models.Car
import com.travelme.customer.models.Departure
import com.travelme.customer.models.Order
import com.travelme.customer.models.User
import com.travelme.customer.utilities.Constants
import com.travelme.customer.utilities.WrappedListResponse
import com.travelme.customer.utilities.WrappedResponse
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

    @GET("user/{api_token}")
    fun getUserIsLogin(
        @Path("api_token") api_token: String
    ) : Call<WrappedResponse<User>>

    @FormUrlEncoded
    @POST("user/register")
    fun register(
        @Field("name") name : String,
        @Field("email") email : String,
        @Field("password") password : String,
        @Field("telp") telp : String
    ) : Call<WrappedResponse<User>>


    @FormUrlEncoded
    @POST("user/login")
    fun login(
        @Field("email") email : String,
        @Field("password") password : String
    ) : Call<WrappedResponse<User>>

    @GET("destination")
    fun getDestination(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Departure>>


    @GET("departure/{destination}")
    fun getDepartureByDest(
        @Header("Authorization") token : String,
        @Path("destination") destination: String
    ) : Call<WrappedListResponse<Departure>>

    @FormUrlEncoded
    @POST("departure/search")
    fun searchDeparture(
        @Header("Authorization") token: String,
        @Field("destination") destination : String,
        @Field("date") date : String
    ) : Call<WrappedListResponse<Departure>>

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
        @Field("pickup_location") pickup_location : String,
        @Field("destination_location") destination_location : String
    ) : Call<WrappedResponse<Order>>

    @GET("order/confirmed")
    fun getMyOrder(
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