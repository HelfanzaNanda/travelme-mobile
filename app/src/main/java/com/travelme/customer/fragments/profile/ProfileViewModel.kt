package com.travelme.customer.fragments.profile


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.travelme.customer.models.User
import com.travelme.customer.repositories.UserRepository
import com.travelme.customer.utilities.SingleLiveEvent

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel(){
    private var user = MutableLiveData<User>()
    private var state : SingleLiveEvent<ProfileState> = SingleLiveEvent()

    private fun setLoading() { state.value = ProfileState.IsLoading(true) }
    private fun hideLoading() { state.value = ProfileState.IsLoading(false) }
    private fun toast(message: String) { state.value = ProfileState.ShowToast(message) }

    fun profile(token: String){
        setLoading()
        userRepository.profile(token){resultUser, error ->
            hideLoading()
            error?.let { it.message?.let { message-> toast(message) }}
            resultUser?.let { user.postValue(it) }
        }
    }

    fun listenToState() = state
    fun listenToUser() = user
}

sealed class ProfileState{
    data class IsLoading (var state : Boolean = false) : ProfileState()
    data class ShowToast(var message : String) : ProfileState()
}