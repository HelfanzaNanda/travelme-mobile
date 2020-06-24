package com.travelme.customer.activities.owner_activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.travelme.customer.models.HourOfDeparture
import com.travelme.customer.repositories.HourRepository
import com.travelme.customer.utilities.SingleLiveEvent


class OwnerViewModel (private val hourRepository: HourRepository) : ViewModel() {
    private var hours = MutableLiveData<List<HourOfDeparture>>()
    private var state: SingleLiveEvent<OwnerState> = SingleLiveEvent()

    private fun setLoading() { state.value = OwnerState.IsLoading(true) }
    private fun hideLoading() { state.value = OwnerState.IsLoading(false) }
    private fun toast(message: String) { state.value = OwnerState.ShowToast(message) }

    fun getOwners(from : String, destination : String, date : String, hour : String){
        setLoading()
        hourRepository.searchOwner(from, destination, date, hour){listOwner, error->
            hideLoading()
            error?.let { it.message?.let { message ->
                toast(message)
            }}
            listOwner.let {
                hours.postValue(it)
                println(it)
            }
        }
    }


    fun listenToState() = state
    fun listenToOwners() = hours
}

sealed class OwnerState {
    data class IsLoading(var state: Boolean = false) : OwnerState()
    data class ShowToast(var message: String) : OwnerState()
}