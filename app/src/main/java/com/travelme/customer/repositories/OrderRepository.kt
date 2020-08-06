package com.travelme.customer.repositories

import com.google.gson.GsonBuilder
import com.travelme.customer.models.CreateOrder
import com.travelme.customer.models.Order
import com.travelme.customer.utilities.ArrayResponse
import com.travelme.customer.utilities.SingleResponse
import com.travelme.customer.utilities.WrappedListResponse
import com.travelme.customer.utilities.WrappedResponse
import com.travelme.customer.webservices.ApiService
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


interface OrderContract {
    fun createOrder(token: String, createOrder: CreateOrder , listener : SingleResponse<CreateOrder>)
    fun fetchMyOrders(token: String, listener: ArrayResponse<Order>)
    fun cancelOrder(token: String, id: String, listener: SingleResponse<Order>)
    fun confirmOrder(token: String, id: String, listener: SingleResponse<Order>)
    fun updateStatus(token: String, id: String, status: String, listener: SingleResponse<Order>)
}

class OrderRepository (private val api : ApiService) : OrderContract{

    override fun createOrder(token: String, createOrder: CreateOrder, listener: SingleResponse<CreateOrder>) {
        val g = GsonBuilder().create()
        val json = g.toJson(createOrder)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        api.storeOrder(token, body).enqueue(object : Callback<WrappedResponse<CreateOrder>>{
            override fun onFailure(call: Call<WrappedResponse<CreateOrder>>, t: Throwable) = listener.onFailure(
                Error(t.message)
            )

            override fun onResponse(call: Call<WrappedResponse<CreateOrder>>, response: Response<WrappedResponse<CreateOrder>>) {
                when{
                    response.isSuccessful -> {
                        val b = response.body()
                        if (b?.status!!){
                            listener.onSuccess(b.data)
                        }else{
                            listener.onFailure(Error(b.message))
                        }
                    }
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }
        })
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