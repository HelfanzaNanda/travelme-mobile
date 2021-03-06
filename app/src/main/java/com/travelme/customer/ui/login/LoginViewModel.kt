package com.travelme.customer.ui.login

import androidx.lifecycle.ViewModel
import com.travelme.customer.models.User
import com.travelme.customer.repositories.UserRepository
import com.travelme.customer.utilities.Constants
import com.travelme.customer.utilities.SingleLiveEvent
import com.travelme.customer.utilities.SingleResponse

class LoginViewModel (private val userRepository: UserRepository) : ViewModel(){

    private var state : SingleLiveEvent<LoginState> = SingleLiveEvent()
    private fun setLoading() { state.value = LoginState.IsLoading(true) }
    private fun hideLoading() { state.value = LoginState.IsLoading(false) }
    private fun toast(message: String) { state.value = LoginState.ShowToast(message) }
    private fun success(message: String) { state.value = LoginState.Success(message) }


    fun login(email: String, password: String){
        setLoading()
        userRepository.login(email, password, object : SingleResponse<User>{
            override fun onSuccess(data: User?) {
                hideLoading()
                data?.let { success(it.token!!) }
            }

            override fun onFailure(err: Error?) {
                hideLoading()
                err?.let { toast(it.message.toString()) }
            }
        })
    }

    fun validate(email: String, password: String): Boolean{
        state.value = LoginState.Reset

        if(email.isEmpty() ){
            state.value = LoginState.Validate(email = "email tidak boleh kosong")
            return false
        }
        if (!Constants.isValidEmail(email)){
            state.value = LoginState.Validate(email = "email tidak valid")
            return false
        }

        if (password.isEmpty()){
            state.value = LoginState.Validate(password = "password tidak boleh kosong")
            return false
        }

        if (!Constants.isValidPassword(password)){
            state.value = LoginState.Validate(password = "password minimal 8 karakter")
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
    data class Success(var token : String) :LoginState()
}