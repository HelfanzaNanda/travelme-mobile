package com.travelme.customer.repositories

import com.travelme.customer.models.Departure
import com.travelme.customer.utilities.WrappedListResponse
import com.travelme.customer.webservices.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DepartureRepository (private val api : ApiService){


    fun getDestination(token: String, result : (List<Departure>?, Error?) -> Unit){
        api.getDestination(token).enqueue(object : Callback<WrappedListResponse<Departure>>{
            override fun onFailure(call: Call<WrappedListResponse<Departure>>, t: Throwable) {
                result(null, Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Departure>>, response: Response<WrappedListResponse<Departure>>) {
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

    fun getDepartureByDest(token: String, destination: String, result: (List<Departure>?, Error?) -> Unit){
        api.getDepartureByDest(token, destination).enqueue(object : Callback<WrappedListResponse<Departure>>{
            override fun onFailure(call: Call<WrappedListResponse<Departure>>, t: Throwable) {
                result(null, Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Departure>>, response: Response<WrappedListResponse<Departure>>) {
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

    fun searchDeparture(token: String, destination: String, date : String, result: (List<Departure>?, Error?) -> Unit){
        api.searchDeparture(token, destination, date).enqueue(object : Callback<WrappedListResponse<Departure>>{
            override fun onFailure(call: Call<WrappedListResponse<Departure>>, t: Throwable) {
                result(null, Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Departure>>, response: Response<WrappedListResponse<Departure>>) {
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