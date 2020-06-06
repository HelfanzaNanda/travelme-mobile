package com.travelme.customer.activities.departure_by_dest_activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.travelme.customer.models.Departure
import com.travelme.customer.repositories.DepartureRepository
import com.travelme.customer.utilities.SingleLiveEvent

class DepartureByDestViewModel (private val departuerRepository: DepartureRepository) : ViewModel() {
    private var departures = MutableLiveData<List<Departure>>()
    private var state: SingleLiveEvent<DepartureByDestState> = SingleLiveEvent()
    
    private fun setLoading() { state.value = DepartureByDestState.IsLoading(true) }
    private fun hideLoading() { state.value = DepartureByDestState.IsLoading(false) }
    private fun toast(message: String) { state.value = DepartureByDestState.ShowToast(message) }

    fun getDepartureByDest(token: String, destination : String){
        setLoading()
        departuerRepository.getDepartureByDest(token, destination){listDeparture, error ->
            hideLoading()
            error?.let { it.message?.let { message ->
                toast(message)
            }}
            listDeparture?.let { departures.postValue(it) }
        }
    }

    fun searchDeparture(token: String, destination: String, date : String){
        setLoading()
        departuerRepository.searchDeparture(token, destination, date){listDeparture, error->
            hideLoading()
            error?.let { it.message?.let { message ->
                toast(message)
            }}
            listDeparture?.let { departures.postValue(it) }
        }
    }

    fun listenToState() = state
    fun listenToDepartures() = departures
}

sealed class DepartureByDestState {
    data class IsLoading(var state: Boolean = false) : DepartureByDestState()
    data class ShowToast(var message: String) : DepartureByDestState()
}