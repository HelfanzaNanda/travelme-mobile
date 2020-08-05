package com.travelme.customer.ui.hour

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.travelme.customer.models.HourOfDeparture
import com.travelme.customer.repositories.HourRepository
import com.travelme.customer.utilities.ArrayResponse
import com.travelme.customer.utilities.SingleLiveEvent
import com.travelme.customer.utilities.SingleResponse

class HourViewModel (private var hourRepository: HourRepository) : ViewModel(){
    private val hours = MutableLiveData<List<HourOfDeparture>>()
    private val state : SingleLiveEvent<HourState> = SingleLiveEvent()
    
    private fun setLoading() { state.value = HourState.IsLoading(true) } 
    private fun hideLoading() { state.value = HourState.IsLoading(false) }
    private fun toast(message: String) { state.value = HourState.ShowToast(message) }
    
    fun searchHours(from : String, destination : String, date : String){
        setLoading()
        hourRepository.searchHours(from, destination, date, object : ArrayResponse<HourOfDeparture>{
            override fun onSuccess(datas: List<HourOfDeparture>?) {
                hideLoading()
                datas?.let { hours.postValue(it) }
            }

            override fun onFailure(err: Error?) {
                hideLoading()
                err?.let { toast(it.message.toString()) }
            }
        })
    }
    
    fun listenToState() = state
    fun listenToHours() = hours
}

sealed class HourState{
    data class IsLoading(var state : Boolean = false) : HourState()
    data class ShowToast(var message : String) : HourState()
}