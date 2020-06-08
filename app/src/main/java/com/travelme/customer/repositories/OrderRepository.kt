package com.travelme.customer.repositories

import com.travelme.customer.models.Order
import com.travelme.customer.utilities.WrappedListResponse
import com.travelme.customer.utilities.WrappedResponse
import com.travelme.customer.webservices.ApiClient
import com.travelme.customer.webservices.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderRepository (private val api : ApiService){

    fun createorder(token : String, owner_id : Int, departure_id : Int, date : String, hour : String, price : Int,
                    total_seat: Int, pickup_location: String, destination_location: String, result : (Boolean, Error?)-> Unit){

        api.storeOrder(token, owner_id, departure_id, date, hour, price, total_seat, pickup_location, destination_location)
            .enqueue(object : Callback<WrappedResponse<Order>> {
                override fun onFailure(call: Call<WrappedResponse<Order>>, t: Throwable) {
                    result(false, Error(t.message))
                }

                override fun onResponse(call: Call<WrappedResponse<Order>>, response: Response<WrappedResponse<Order>>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        if (body?.status!!){
                            result(true, null)
                        }else{
                            result(false, Error(body.message))
                        }
                    }else{
                        result(false, Error(response.message()))
                    }
                }

            })
    }

    fun getMyOrders(token: String, result: (List<Order>?, Error?)-> Unit){

        api.getMyOrders(token).enqueue(object : Callback<WrappedListResponse<Order>> {
            override fun onFailure(call: Call<WrappedListResponse<Order>>, t: Throwable) = result(null, Error(t.message))

            override fun onResponse(call: Call<WrappedListResponse<Order>>, response: Response<WrappedListResponse<Order>>) {
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

    fun canceloroder(token: String, id : String, result: (Boolean, Error?) -> Unit){
        api.cancelorder(token, id.toInt()).enqueue(object : Callback<WrappedResponse<Order>> {
            override fun onFailure(call: Call<WrappedResponse<Order>>, t: Throwable) = result(false, Error(t.message))

            override fun onResponse(call: Call<WrappedResponse<Order>>, response: Response<WrappedResponse<Order>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        result(true, null)
                    }else{
                        result(false, Error(body.message))
                    }
                }else{
                    result(false, Error(response.message()))
                }
            }
        })
    }

    fun updatestatus(token: String, id: String, status : String,result: (Boolean, Error?) -> Unit){
        api.updatestatusorder(token, id.toInt(), status).enqueue(object : Callback<WrappedResponse<Order>>{
            override fun onFailure(call: Call<WrappedResponse<Order>>, t: Throwable) = result(false, Error(t.message))

            override fun onResponse(call: Call<WrappedResponse<Order>>, response: Response<WrappedResponse<Order>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        result(true, null)
                    }else{
                        result(true, Error(body.message))
                    }
                }else{
                    result(true, Error(response.message()))
                }
            }

        })
    }
}