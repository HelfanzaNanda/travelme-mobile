package com.travelme.customer.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.travelme.customer.models.Order
import com.travelme.customer.utilities.SingleLiveEvent
import com.travelme.customer.utilities.WrappedResponse
import com.travelme.customer.webservices.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field

class OrderViewModel : ViewModel(){
    private var order = MutableLiveData<Order>()
    private var state : SingleLiveEvent<OrderState> = SingleLiveEvent()
    private var api = ApiClient.instance()


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
        println("token : "+token)
        println("owner_id : "+owner_id)
        println("departure_id : "+departure_id)
        println("date : "+date)
        println("hour : "+hour)
        println("price : "+price)
        println("seat : "+total_seat)
        println("pickup : "+pickup_location)
        println("destination : "+destination_location)
        state.value = OrderState.IsLoading(true)
        api.storeOrder(token, owner_id, departure_id, date, hour, price, total_seat, pickup_location, destination_location)
            .enqueue(object : Callback<WrappedResponse<Order>>{
                override fun onFailure(call: Call<WrappedResponse<Order>>, t: Throwable) {
                    println("OnFailure : "+t.message)
                }

                override fun onResponse(call: Call<WrappedResponse<Order>>, response: Response<WrappedResponse<Order>>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        if (body?.status!!){
                            state.value = OrderState.ShowToast("Berhasil Memesan Travel")
                            state.value = OrderState.Success
                        }else{
                            println("failed order travel : "+response.message())
                            state.value = OrderState.ShowToast("gagal Memesan Travel")
                        }
                    }else{
                        state.value = OrderState.ShowToast("gagal Memesan")
                    }
                    state.value = OrderState.IsLoading(false)
                }

            })
    }

    fun getState() = state
    fun getOrder() = order
}

sealed class OrderState{
    object Reset : OrderState()
    data class IsLoading (var state : Boolean = false) : OrderState()
    data class ShowToast(var message : String) : OrderState()
    data class Validate(
        var pickup_location : String? = null,
        var destination_location : String? = null
    ) : OrderState()
    object Success : OrderState()

}