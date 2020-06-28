package com.travelme.customer.repositories

import com.travelme.customer.models.HourOfDeparture
import com.travelme.customer.models.Owner
import com.travelme.customer.utilities.WrappedListResponse
import com.travelme.customer.webservices.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OwnerRepository (private val api: ApiService){

    fun domicileForDestinationOther(result: (List<Owner>?, Error?)->Unit){
        api.domicile().enqueue(object : Callback<WrappedListResponse<Owner>>{
            override fun onFailure(call: Call<WrappedListResponse<Owner>>, t: Throwable) {
                result(null, Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Owner>>, response: Response<WrappedListResponse<Owner>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data
                        result(data, null)
                    }else{
                        result(null, Error())
                    }
                }else{
                    result(null, Error("r : ${response.message()}"))
                }
            }

        })
    }

    fun domicileForDestinationTegal(result: (List<Owner>?, Error?) -> Unit){
        api.domicile().enqueue(object : Callback<WrappedListResponse<Owner>>{
            override fun onFailure(call: Call<WrappedListResponse<Owner>>, t: Throwable) {
                result(null, Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Owner>>, response: Response<WrappedListResponse<Owner>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data
                        result(data, null)
                    }else{
                        result(null, Error())
                    }
                }else{
                    result(null, Error("r : ${response.message()}"))
                }
            }
        })
    }
}