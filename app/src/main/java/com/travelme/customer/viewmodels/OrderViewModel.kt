package com.travelme.customer.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.travelme.customer.models.Order
import com.travelme.customer.utilities.SingleLiveEvent
import com.travelme.customer.utilities.WrappedListResponse
import com.travelme.customer.utilities.WrappedResponse
import com.travelme.customer.webservices.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field

class OrderViewModel : ViewModel(){
    private var order = MutableLiveData<Order>()
    private var orders = MutableLiveData<List<Order>>()
    private var state : SingleLiveEvent<OrderState> = SingleLiveEvent()
    private var api = ApiClient.instance()

    private fun setLoading() { state.value = OrderState.IsLoading(true) }
    private fun hideLoading() { state.value = OrderState.IsLoading(false) }
    private fun toast(message: String) { state.value = OrderState.ShowToast(message) }
    private fun successDelete() { state.value = OrderState.SuccessDelete }
    private fun success() { state.value = OrderState.Success }

    fun validate(pickup_location: String, destination_location: String): Boolean{
        state.value = OrderState.Reset
        if (pickup_location.isEmpty()){
            state.value = OrderState.ShowToast("lokasi penjemputan silahkan di isi")
            return false
        }
        if (destination_location.isEmpty()){
            state.value = OrderState.ShowToast("lokasi tujuan silahkan di isi")
            return false
        }
        return true
    }

    fun storeOrder(token : String, owner_id : Int, departure_id : Int, date : String,
                   hour : String, price : Int, total_seat: Int, pickup_location: String, destination_location: String){
        setLoading()
        api.storeOrder(token, owner_id, departure_id, date, hour, price, total_seat, pickup_location, destination_location)
            .enqueue(object : Callback<WrappedResponse<Order>>{
                override fun onFailure(call: Call<WrappedResponse<Order>>, t: Throwable) {
                    hideLoading()
                }

                override fun onResponse(call: Call<WrappedResponse<Order>>, response: Response<WrappedResponse<Order>>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        if (body?.status!!){
                            toast("berhasil memesan travel")
                            success()
                        }
                    }
                    hideLoading()
                }

            })
    }

    fun getMyOrders(token: String){
        setLoading()
        api.getMyOrders(token).enqueue(object : Callback<WrappedListResponse<Order>>{
            override fun onFailure(call: Call<WrappedListResponse<Order>>, t: Throwable) {
                hideLoading()
            }

            override fun onResponse(call: Call<WrappedListResponse<Order>>, response: Response<WrappedListResponse<Order>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data
                        orders.postValue(data)
                    }
                }
                hideLoading()
            }

        })
    }

    fun cancelroder(token: String, id : String){
        setLoading()
        api.cancelorder(token, id.toInt()).enqueue(object : Callback<WrappedResponse<Order>>{
            override fun onFailure(call: Call<WrappedResponse<Order>>, t: Throwable) {
                println(t.message)
                hideLoading()
            }

            override fun onResponse(call: Call<WrappedResponse<Order>>, response: Response<WrappedResponse<Order>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        toast("berhasil membatalkan pesanan")
                        successDelete()
                    }else{
                        println(body.message)
                    }
                }else{
                    println(response.message())
                }
                hideLoading()
            }

        })
    }

    fun getState() = state
    fun getOrder() = order
    fun getOrders() = orders
}

sealed class OrderState{
    object Reset : OrderState()
    data class IsLoading (var state : Boolean = false) : OrderState()
    data class ShowToast(var message : String) : OrderState()
    data class Validate(
        var pickup_location : String? = null,
        var destination_location : String? = null
    ) : OrderState()
    object Success: OrderState()
    object SuccessDelete : OrderState()
    //data class Success(var snap_token : String) : OrderState()

}