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


    private fun setLoading() { state.value = DepartureState.IsLoading(true) }
    private fun hideLoading() { state.value = DepartureState.IsLoading(false) }
    //private fun toast(message: String) { state.value = DepartureState.ShowToast(message) }

    fun getDestination(token : String){
        setLoading()
        api.getDestination(token).enqueue(object : Callback<WrappedListResponse<Departure>>{
            override fun onFailure(call: Call<WrappedListResponse<Departure>>, t: Throwable) {
                hideLoading()
            }
            override fun onResponse(call: Call<WrappedListResponse<Departure>>, response: Response<WrappedListResponse<Departure>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data
                        departures.postValue(data)
                    }
                }
                hideLoading()
            }

        })
    }

    fun getDepartureByDest(token : String, destination : String){
        setLoading()
        api.getDepartureByDest(token, destination).enqueue(object : Callback<WrappedListResponse<Departure>>{
            override fun onFailure(call: Call<WrappedListResponse<Departure>>, t: Throwable) {
                hideLoading()
            }

            override fun onResponse(call: Call<WrappedListResponse<Departure>>, response: Response<WrappedListResponse<Departure>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data
                        departures.postValue(data)
                    }
                }
                hideLoading()
            }

        })
    }

    fun searchDeparture(token: String, destination: String, date : String){
        setLoading()
        api.searchDeparture(token, destination, date).enqueue(object : Callback<WrappedListResponse<Departure>>{
            override fun onFailure(call: Call<WrappedListResponse<Departure>>, t: Throwable) {
                hideLoading()
            }

            override fun onResponse(call: Call<WrappedListResponse<Departure>>, response: Response<WrappedListResponse<Departure>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data
                        departures.postValue(data)
                    }
                }
                hideLoading()
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