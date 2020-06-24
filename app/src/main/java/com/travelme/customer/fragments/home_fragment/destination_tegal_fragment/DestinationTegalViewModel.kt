package com.travelme.customer.fragments.home_fragment.destination_tegal_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.travelme.customer.models.Owner
import com.travelme.customer.repositories.OwnerRepository
import com.travelme.customer.utilities.SingleLiveEvent

class DestinationTegalViewModel (private val ownerRepository: OwnerRepository) : ViewModel(){
    private val owners = MutableLiveData<List<Owner>>()
    private var state: SingleLiveEvent<DestinationTegalState> = SingleLiveEvent()

    private fun setLoading() { state.value = DestinationTegalState.IsLoading(true) }
    private fun hideLoading() { state.value = DestinationTegalState.IsLoading(false) }
    private fun toast(message: String) { state.value = DestinationTegalState.ShowToast(message) }

    fun validate(date : String) : Boolean{
        if (date.isEmpty()){
            toast("tanggal harus di isi")
            return false
        }
        return true
    }

    fun domicile(){
        setLoading()
        ownerRepository.domicile(){listOwner, error->
            hideLoading()
            error?.let { it.message?.let { message->toast(message) } }
            listOwner?.let { owners.postValue(it) }
        }
    }

    fun listenToState() = state
    fun listenToOwners() = owners
}

sealed class DestinationTegalState{
    data class IsLoading(var state: Boolean = false) : DestinationTegalState()
    data class ShowToast(var message: String) : DestinationTegalState()
}