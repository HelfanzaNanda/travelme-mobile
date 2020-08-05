package com.travelme.customer.repositories

import com.travelme.customer.models.HourOfDeparture
import com.travelme.customer.utilities.ArrayResponse
import com.travelme.customer.utilities.WrappedListResponse
import com.travelme.customer.webservices.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface HourContract{
    fun searchHours(from : String, destination : String, date : String, listener : ArrayResponse<HourOfDeparture>)
    fun searchOwners(from : String, destination : String, date : String, hour : String, listener: ArrayResponse<HourOfDeparture>)
}

class HourRepository (private val api: ApiService) : HourContract{

    override fun searchHours(from: String, destination: String, date: String, listener: ArrayResponse<HourOfDeparture>) {
        api.searchHours(from, destination, date).enqueue(object : Callback<WrappedListResponse<HourOfDeparture>>{
            override fun onFailure(call: Call<WrappedListResponse<HourOfDeparture>>, t: Throwable) = listener.onFailure(Error(t.message))

            override fun onResponse(call: Call<WrappedListResponse<HourOfDeparture>>, response: Response<WrappedListResponse<HourOfDeparture>>) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!){
                            listener.onSuccess(body.data)
                        }else{
                            listener.onFailure(Error(body.message))
                        }
                    }
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun searchOwners(from: String, destination: String, date: String, hour: String, listener: ArrayResponse<HourOfDeparture>) {
        api.searchOwners(from, destination, date, hour).enqueue(object : Callback<WrappedListResponse<HourOfDeparture>>{
            override fun onFailure(call: Call<WrappedListResponse<HourOfDeparture>>, t: Throwable) = listener.onFailure(Error(t.message))

            override fun onResponse(call: Call<WrappedListResponse<HourOfDeparture>>, response: Response<WrappedListResponse<HourOfDeparture>>) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!){
                            listener.onSuccess(body.data)
                        }else{
                            listener.onFailure(Error(body.message))
                        }
                    }
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }
        })
    }
}