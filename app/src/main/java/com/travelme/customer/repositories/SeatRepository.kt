package com.travelme.customer.repositories

import com.travelme.customer.models.Seat
import com.travelme.customer.utilities.ArrayResponse
import com.travelme.customer.utilities.WrappedListResponse
import com.travelme.customer.webservices.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface SeatContract{
    fun checkSeat(car_id : String, date : String, hour : String, listener : ArrayResponse<Seat>)
}

class SeatRepository (private val api : ApiService) : SeatContract{
    override fun checkSeat(car_id: String, date: String, hour: String, listener: ArrayResponse<Seat>) {
        api.checkSeat(car_id.toInt(), date, hour).enqueue(object : Callback<WrappedListResponse<Seat>>{
            override fun onFailure(call: Call<WrappedListResponse<Seat>>, t: Throwable) = listener.onFailure(
                Error(t.message)
            )

            override fun onResponse(call: Call<WrappedListResponse<Seat>>, response: Response<WrappedListResponse<Seat>>) {
                when{
                    response.isSuccessful -> {
                        listener.onSuccess(response.body()!!.data)
                        println(response.body()!!.data)
                    }
                    !response.isSuccessful-> listener.onFailure(Error(response.message()))
                }
            }
        })
    }

}