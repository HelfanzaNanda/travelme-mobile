package com.travelme.customer.ui.update_profile

import androidx.lifecycle.ViewModel
import com.travelme.customer.models.User
import com.travelme.customer.repositories.UserRepository
import com.travelme.customer.utilities.SingleLiveEvent
import com.travelme.customer.utilities.SingleResponse

class UpdateProfilViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val state: SingleLiveEvent<UpdateProfilState> = SingleLiveEvent()
    private fun setLoading() { state.value = UpdateProfilState.Loading(true) }
    private fun hideLoading() { state.value = UpdateProfilState.Loading(false) }
    private fun toast(message: String) { state.value = UpdateProfilState.ShowToast(message) }
    private fun success() { state.value = UpdateProfilState.Success }

    fun updateProfile(token: String, name : String, pass : String, pathImage: String) {
        setLoading()
        if (name.isEmpty()){
            updatePhotoProfile(token, pathImage)
        }else {
            userRepository.updateProfile(token, name, pass, object : SingleResponse<User>{

                override fun onFailure(err: Error?) {
                    err?.let { toast(it.message.toString()) }
                }

                override fun onSuccess(data: User?) {
                    hideLoading()
                    if (pathImage.isNotEmpty()) {
                        updatePhotoProfile(token, pathImage)
                    } else {
                        success()
                    }
                }
            })
        }
    }

    private fun updatePhotoProfile(token: String, pathImage: String) {
        userRepository.updatePhotoProfile(token, pathImage, object : SingleResponse<User>{
            override fun onSuccess(data: User?) {
                hideLoading()
                data?.let { success() }
            }

            override fun onFailure(err: Error?) {
                hideLoading()
                err?.let { toast(it.message.toString()) }
            }

        })
    }

    fun listenToState() = state
}

sealed class UpdateProfilState {
    data class Loading(var state: Boolean = false) : UpdateProfilState()
    data class ShowToast(var message: String) : UpdateProfilState()
    object Success : UpdateProfilState()
    data class Validate(
        var name : String? = null,
        var pass: String? = null
    ) : UpdateProfilState()
    object Reset : UpdateProfilState()
}