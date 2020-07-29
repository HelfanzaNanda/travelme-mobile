package com.travelme.customer.activities.forgot_password

import androidx.lifecycle.ViewModel
import com.travelme.customer.models.User
import com.travelme.customer.repositories.UserRepository
import com.travelme.customer.utilities.Constants
import com.travelme.customer.utilities.SingleLiveEvent
import com.travelme.customer.utilities.SingleResponse

class ForgotPasswordViewModel (private val userRepository: UserRepository) : ViewModel(){
    private val state : SingleLiveEvent<ForgotPasswordState> = SingleLiveEvent()

    private fun setLoading(){ state.value = ForgotPasswordState.Loading(true) }
    private fun hideLoading(){ state.value = ForgotPasswordState.Loading(false) }
    private fun toast(message: String) { state.value = ForgotPasswordState.ShowToast(message) }
    private fun success() { state.value = ForgotPasswordState.Success }


    fun forgotPassword(email : String){
        setLoading()
        userRepository.forgotPassword(email, object : SingleResponse<User>{
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

    fun validate(email: String) : Boolean {
        if (email.isEmpty()){
            state.value = ForgotPasswordState.Validate(email = "email tidak boleh kosong")
            return false
        }
        if (!Constants.isValidEmail(email)){
            state.value = ForgotPasswordState.Validate(email = "email tidak valid")
            return false
        }
        return true
    }

    fun listenToState() = state
}

sealed class ForgotPasswordState {
    data class Loading(var state : Boolean = false) : ForgotPasswordState()
    data class ShowToast(var message : String) : ForgotPasswordState()
    object Success : ForgotPasswordState()
    data class Validate(var email: String? = null) : ForgotPasswordState()
    object Reset : ForgotPasswordState()
}