package com.travelme.customer.fragments.home.destination_other_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.travelme.customer.models.Destination
import com.travelme.customer.models.Owner
import com.travelme.customer.repositories.DepartureRepository
import com.travelme.customer.repositories.OwnerRepository
import com.travelme.customer.utilities.ArrayResponse
import com.travelme.customer.utilities.SingleLiveEvent

class DestinationOtherViewModel (private val departureRepository: DepartureRepository) : ViewModel(){
    private val destinations = MutableLiveData<List<Destination>>()
    private var state: SingleLiveEvent<DestinationOtherState> = SingleLiveEvent()

    private fun setLoading() { state.value = DestinationOtherState.IsLoading(true) }
    private fun hideLoading() { state.value = DestinationOtherState.IsLoading(false) }
    private fun toast(message: String) { state.value = DestinationOtherState.ShowToast(message) }

    fun validate(date : String) : Boolean{
        if (date.isEmpty()){
            toast("tanggal harus di isi")
            return false
        }
        return true
    }

    fun domicile(){
        setLoading()
        departureRepository.fetchDestination(object : ArrayResponse<Destination>{
            override fun onSuccess(datas: List<Destination>?) {
                hideLoading()
                datas?.let { destinations.postValue(it) }
            }

            override fun onFailure(err: Error?) {
                hideLoading()
                err?.let { toast(it.message.toString()) }
            }

        })
    }

    fun listenToState() = state
    fun listenToDestinations() = destinations
}

sealed class DestinationOtherState{
    data class IsLoading(var state : Boolean = false) : DestinationOtherState()
    data class ShowToast(var message: String) : DestinationOtherState()
}