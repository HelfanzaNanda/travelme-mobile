package com.travelme.customer.repositories

import com.travelme.customer.models.Destination
import com.travelme.customer.utilities.ArrayResponse
import com.travelme.customer.utilities.WrappedListResponse
import com.travelme.customer.webservices.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface DepartureContract {
    fun fetchDestination(listener : ArrayResponse<Destination>)
}
class DepartureRepository (private val api : ApiService) : DepartureContract {
    override fun fetchDestination(listener: ArrayResponse<Destination>) {
        api.destination().enqueue(object : Callback<WrappedListResponse<Destination>>{
            override fun onFailure(call: Call<WrappedListResponse<Destination>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Destination>>, response: Response<WrappedListResponse<Destination>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

}