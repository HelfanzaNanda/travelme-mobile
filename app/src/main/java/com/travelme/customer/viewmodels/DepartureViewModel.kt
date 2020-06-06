package com.travelme.customer.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.travelme.customer.models.Departure
import com.travelme.customer.repositories.DepartureRepository
import com.travelme.customer.utilities.SingleLiveEvent

class DepartureViewModel(private val departuerRepository: DepartureRepository) : ViewModel() {
    private var departures = MutableLiveData<List<Departure>>()
    private var state: SingleLiveEvent<DepartureState> = SingleLiveEvent()


    private fun setLoading() { state.value = DepartureState.IsLoading(true) }
    private fun hideLoading() { state.value = DepartureState.IsLoading(false) }
    private fun toast(message: String) { state.value = DepartureState.ShowToast(message) }


    fun getDestination(token: String){
        setLoading()
        departuerRepository.getDestination(token){listDeparture, error ->
            hideLoading()
            error?.let { it.message?.let { message ->
                toast(message)
            }}
            listDeparture?.let { departures.postValue(it) }
        }
    }

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

sealed class DepartureState {
    data class IsLoading(var state: Boolean = false) : DepartureState()
    data class ShowToast(var message: String) : DepartureState()
}