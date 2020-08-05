package com.travelme.customer.repositories

import com.travelme.customer.models.Order
import com.travelme.customer.utilities.ArrayResponse
import com.travelme.customer.utilities.SingleResponse
import com.travelme.customer.utilities.WrappedListResponse
import com.travelme.customer.utilities.WrappedResponse
import com.travelme.customer.webservices.ApiClient
import com.travelme.customer.webservices.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


interface OrderContract {
    fun createOrder(token: String,order: Order , listener : SingleResponse<Order>)
    fun fetchMyOrders(token: String, listener: ArrayResponse<Order>)
    fun cancelOrder(token: String, id: String, listener: SingleResponse<Order>)
    fun confirmOrder(token: String, id: String, listener: SingleResponse<Order>)
    fun updateStatus(token: String, id: String, status: String, listener: SingleResponse<Order>)
}

class OrderRepository (private val api : ApiService) : OrderContract{

    fun createorder(token : String, owner_id : Int, departure_id : Int, date : String, hour : String, price : Int,
                    total_seat: Int, pickup_point: String, lat_pickup_point : String, lng_pickup_point : String,
                    destination_point: String, lat_destination_point : String, lng_destination_point : String,
                    result : (Boolean, Error?)-> Unit){

        api.storeOrder(token, owner_id, departure_id, date, hour, price, total_seat, pickup_point, lat_pickup_point, lng_pickup_point,
            destination_point, lat_destination_point, lng_destination_point)
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

    override fun createOrder(token: String, order: Order, listener: SingleResponse<Order>) {
        TODO("Not yet implemented")
    }

    override fun fetchMyOrders(token: String, listener: ArrayResponse<Order>) {
        api.fetchMyOrders(token).enqueue(object : Callback<WrappedListResponse<Order>> {
            override fun onFailure(call: Call<WrappedListResponse<Order>>, t: Throwable) = listener.onFailure(
                Error(t.message)
            )

            override fun onResponse(call: Call<WrappedListResponse<Order>>, response: Response<WrappedListResponse<Order>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }
        })
    }

    override fun cancelOrder(token: String, id: String, listener: SingleResponse<Order>) {
        api.cancelOrder(token, id.toInt()).enqueue(object : Callback<WrappedResponse<Order>> {
            override fun onFailure(call: Call<WrappedResponse<Order>>, t: Throwable) = listener.onFailure(Error(t.message))

            override fun onResponse(call: Call<WrappedResponse<Order>>, response: Response<WrappedResponse<Order>>) {
                when{
                    response.isSuccessful ->{
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

    override fun confirmOrder(token: String, id: String, listener: SingleResponse<Order>) {
        api.confirmOrder(token, id.toInt()).enqueue(object : Callback<WrappedResponse<Order>> {
            override fun onFailure(call: Call<WrappedResponse<Order>>, t: Throwable) = listener.onFailure(
                Error(t.message)
            )

            override fun onResponse(call: Call<WrappedResponse<Order>>, response: Response<WrappedResponse<Order>>) {
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

    override fun updateStatus(token: String, id: String, status : String, listener: SingleResponse<Order>) {
        api.updateStatusOrder(token, id.toInt(), status).enqueue(object : Callback<WrappedResponse<Order>>{
            override fun onFailure(call: Call<WrappedResponse<Order>>, t: Throwable) = listener.onFailure(Error(t.message))

            override fun onResponse(call: Call<WrappedResponse<Order>>, response: Response<WrappedResponse<Order>>) {
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