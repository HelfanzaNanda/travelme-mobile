package com.travelme.customer.fragments.home_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.travelme.customer.models.Departure
import com.travelme.customer.repositories.DepartureRepository
import com.travelme.customer.utilities.SingleLiveEvent

class HomeViewModel(private val departuerRepository: DepartureRepository) : ViewModel() {
    private var departures = MutableLiveData<List<Departure>>()
    private var state: SingleLiveEvent<HomeState> = SingleLiveEvent()

    private fun setLoading() { state.value = HomeState.IsLoading(true) }
    private fun hideLoading() { state.value = HomeState.IsLoading(false) }
    private fun toast(message: String) { state.value = HomeState.ShowToast(message) }

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

    fun listenToState() = state
    fun listenToDepartures() = departures
}

sealed class HomeState {
    data class IsLoading(var state: Boolean = false) : HomeState()
    data class ShowToast(var message: String) : HomeState()
}