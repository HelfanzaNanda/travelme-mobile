package com.travelme.customer.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.travelme.customer.models.Departure
import com.travelme.customer.utilities.SingleLiveEvent
import com.travelme.customer.utilities.WrappedListResponse
import com.travelme.customer.webservices.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DepartureViewModel : ViewModel() {
    private var departures = MutableLiveData<List<Departure>>()
    private var state: SingleLiveEvent<DepartureState> = SingleLiveEvent()
    private var api = ApiClient.instance()


    fun getDestination(token : String){
        state.value = DepartureState.IsLoading(false)
        api.getDestination(token).enqueue(object : Callback<WrappedListResponse<Departure>>{
            override fun onFailure(call: Call<WrappedListResponse<Departure>>, t: Throwable) {
                println("onFailure : "+t.message)
            }
            override fun onResponse(call: Call<WrappedListResponse<Departure>>, response: Response<WrappedListResponse<Departure>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data
                        departures.postValue(data)
                    }else{
                        println("response is not successfull "+response.message())
                    }
                }else{
                    println("response is not successfull, Try Again! "+response.message())
                }
                state.value = DepartureState.IsLoading(false)
            }

        })
    }

    fun getDepartureByDest(token : String, destination : String){
        state.value = DepartureState.IsLoading(false)
        api.getDepartureByDest(token, destination).enqueue(object : Callback<WrappedListResponse<Departure>>{
            override fun onFailure(call: Call<WrappedListResponse<Departure>>, t: Throwable) {
                println("onFailure : "+t.message)
            }

            override fun onResponse(call: Call<WrappedListResponse<Departure>>, response: Response<WrappedListResponse<Departure>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data
                        departures.postValue(data)
                    }else{
                        println("response is not successfull "+response.message())
                    }
                }else{
                    println("response is not successfull, Try Again! "+response.message())
                }
                state.value = DepartureState.IsLoading(false)
            }

        })
    }

    fun searchDeparture(token: String, destination: String, date : String){
        println(date)
        println(destination)
        state.value = DepartureState.IsLoading(false)
        api.searchDeparture(token, destination, date).enqueue(object : Callback<WrappedListResponse<Departure>>{
            override fun onFailure(call: Call<WrappedListResponse<Departure>>, t: Throwable) {
                println("onFailure : "+t.message)
            }

            override fun onResponse(call: Call<WrappedListResponse<Departure>>, response: Response<WrappedListResponse<Departure>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data
                        departures.postValue(data)
                    }else{
                        println("response is not successfull "+response.message())
                    }
                }else{
                    println("response is not successfull, Try Again! "+response.message())
                }
                state.value = DepartureState.IsLoading(false)
            }

        })
    }

    fun getState() = state
    fun getDepartures() = departures
}

sealed class DepartureState {
    data class IsLoading(var state: Boolean = false) : DepartureState()
    data class ShowToast(var message: String) : DepartureState()
}