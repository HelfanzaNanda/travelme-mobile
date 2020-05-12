package com.travelme.customer.webservices

import com.travelme.customer.models.Car
import com.travelme.customer.models.Departure
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

    @FormUrlEncoded
    @POST("user/register")
    fun register(
        @Field("name") name : String,
        @Field("email") email : String,
        @Field("password") password : String
    ) : Call<WrappedResponse<User>>


    @FormUrlEncoded
    @POST("user/login")
    fun login(
        @Field("email") email : String,
        @Field("password") password : String
    ) : Call<WrappedResponse<User>>

    @GET("user/destination")
    fun getDestination(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Departure>>


    @GET("user/departure/{destination}")
    fun getDepartureByDest(
        @Header("Authorization") token : String,
        @Path("destination") destination: String
    ) : Call<WrappedListResponse<Departure>>

    @FormUrlEncoded
    @POST("user/departure/search")
    fun searchDeparture(
        @Header("Authorization") token: String,
        @Field("destination") destination : String,
        @Field("date") date : String
    ) : Call<WrappedListResponse<Departure>>

}