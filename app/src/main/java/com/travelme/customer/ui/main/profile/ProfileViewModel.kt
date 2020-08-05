package com.travelme.customer.ui.main.profile


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.travelme.customer.models.User
import com.travelme.customer.repositories.UserRepository
import com.travelme.customer.utilities.SingleLiveEvent
import com.travelme.customer.utilities.SingleResponse

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel(){
    private var user = MutableLiveData<User>()
    private var state : SingleLiveEvent<ProfileState> = SingleLiveEvent()

    private fun setLoading() { state.value = ProfileState.IsLoading(true) }
    private fun hideLoading() { state.value = ProfileState.IsLoading(false) }
    private fun toast(message: String) { state.value = ProfileState.ShowToast(message) }

    fun profile(token: String){
        setLoading()
        userRepository.profile(token, object : SingleResponse<User>{
            override fun onSuccess(data: User?) {
                hideLoading()
                data?.let { user.postValue(it) }
            }

            override fun onFailure(err: Error?) {
                hideLoading()
                err?.let { toast(it.message.toString()) }
            }
        })
    }

    fun listenToState() = state
    fun listenToUser() = user
}

sealed class ProfileState{
    data class IsLoading (var state : Boolean = false) : ProfileState()
    data class ShowToast(var message : String) : ProfileState()
}