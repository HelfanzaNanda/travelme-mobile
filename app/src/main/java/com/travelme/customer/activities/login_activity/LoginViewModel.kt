package com.travelme.customer.activities.login_activity

import androidx.lifecycle.ViewModel
import com.travelme.customer.repositories.UserRepository
import com.travelme.customer.utilities.Constants
import com.travelme.customer.utilities.SingleLiveEvent

class LoginViewModel (private val userRepository: UserRepository) : ViewModel(){

    private var state : SingleLiveEvent<LoginState> = SingleLiveEvent()
    private fun setLoading() { state.value = LoginState.IsLoading(true) }
    private fun hideLoading() { state.value = LoginState.IsLoading(false) }
    private fun toast(message: String) { state.value = LoginState.ShowToast(message) }
    private fun success(message: String) { state.value = LoginState.Success(message) }


    fun login(email: String, password: String){
        setLoading()
        userRepository.login(email, password){token, error ->
            hideLoading()
            error?.let { it.message?.let { message ->
                toast(message)
            }}
            token?.let { success(it) }
        }
    }

    fun validate(email: String, password: String): Boolean{
        state.value = LoginState.Reset

        if (email.isEmpty() || password.isEmpty()){
            state.value = LoginState.ShowToast("mohon isi semua form")
            return false
        }
        if (!Constants.isValidEmail(email)){
            state.value = LoginState.Validate(email = "email tidak valid")
            return false
        }
        if (!Constants.isValidPassword(password)){
            state.value = LoginState.Validate(password = "password tidak valid")
            return false
        }

        return true
    }

    fun listenToState() = state
}

sealed class LoginState{
    object Reset : LoginState()
    data class IsLoading (var state : Boolean = false) : LoginState()
    data class ShowToast(var message : String) : LoginState()
    data class Validate(
        var name : String? = null,
        var email : String? = null,
        var password : String? = null,
        var confirmPassword : String? = null,
        var telp : String? = null
    ) : LoginState()
    data class Success(var message : String) :LoginState()
}