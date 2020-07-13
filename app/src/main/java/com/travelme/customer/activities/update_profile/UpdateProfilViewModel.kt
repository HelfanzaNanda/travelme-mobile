package com.travelme.customer.activities.update_profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.travelme.customer.models.User
import com.travelme.customer.repositories.UserRepository
import com.travelme.customer.utilities.SingleLiveEvent

class UpdateProfilViewModel (private val userRepository: UserRepository) : ViewModel(){
    private val state : SingleLiveEvent<UpdateProfilState> = SingleLiveEvent()
    private var user = MutableLiveData<User>()

    private fun setLoading() { state.value = UpdateProfilState.IsLoading(true) }
    private fun hideLoading() { state.value = UpdateProfilState.IsLoading(false) }
    private fun toast(message: String) { state.value = UpdateProfilState.ShowToast(message) }

    fun listenToState() = state
    fun listenToUser() = user
}

sealed class UpdateProfilState {
    data class IsLoading(var state : Boolean = false) : UpdateProfilState()
    data class ShowToast(var message : String) : UpdateProfilState()
}