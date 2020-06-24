package com.travelme.customer.repositories

import com.travelme.customer.models.HourOfDeparture
import com.travelme.customer.utilities.WrappedListResponse
import com.travelme.customer.webservices.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HourRepository (private val api: ApiService){
    fun searchHours(from : String, destination : String, date : String, result : (List<HourOfDeparture>?, Error?)->Unit){
        api.searchHours(from, destination, date).enqueue(object : Callback<WrappedListResponse<HourOfDeparture>>{
            override fun onFailure(call: Call<WrappedListResponse<HourOfDeparture>>, t: Throwable) {
                result(null, Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<HourOfDeparture>>, response: Response<WrappedListResponse<HourOfDeparture>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data
                        result(data, null)
                    }else{
                        result(null, Error())
                    }
                }else{
                    result(null, Error(response.message()))
                }
            }

        })
    }

    fun searchOwner(from : String, destination : String, date : String, hour : String, result : (List<HourOfDeparture>?, Error?)->Unit){
        api.searchOwners(from, destination, date, hour).enqueue(object : Callback<WrappedListResponse<HourOfDeparture>>{
            override fun onFailure(call: Call<WrappedListResponse<HourOfDeparture>>, t: Throwable) {
                result(null, Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<HourOfDeparture>>, response: Response<WrappedListResponse<HourOfDeparture>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data
                        result(data, null)
                    }else{
                        result(null, Error())
                    }
                }else{
                    result(null, Error(response.message()))
                }
            }

        })
    }
}