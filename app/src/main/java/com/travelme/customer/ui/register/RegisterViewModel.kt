package com.travelme.customer.ui.register


import androidx.lifecycle.ViewModel
import com.travelme.customer.models.User
import com.travelme.customer.repositories.FirebaseRepository
import com.travelme.customer.repositories.UserRepository
import com.travelme.customer.utilities.Constants
import com.travelme.customer.utilities.SingleLiveEvent
import com.travelme.customer.utilities.SingleResponse

class RegisterViewModel(private val userRepository: UserRepository, private val firebaseRepository: FirebaseRepository) : ViewModel(){

    private var state : SingleLiveEvent<RegisterState> = SingleLiveEvent()
    private fun setLoading() { state.value = RegisterState.IsLoading(true) }
    private fun hideLoading() { state.value = RegisterState.IsLoading(false) }
    private fun toast(message: String) { state.value = RegisterState.ShowToast(message) }
    private fun success(message: String) { state.value = RegisterState.Success(message) }

    private fun getFirebaseToken(name: String, email: String, password: String, phone : String){
        firebaseRepository.getFirebaseToken(object : SingleResponse<String>{
            override fun onSuccess(data: String?) {
                data?.let {
                    userRepository.register(name, email, password, phone, it, object : SingleResponse<User>{
                        override fun onSuccess(data: User?) {
                            hideLoading()
                            data?.let { success(email) }
                        }

                        override fun onFailure(err: Error?) {
                            hideLoading()
                            err?.let { error ->  toast(error.message.toString()) }
                        }
                    })
                }
            }

            override fun onFailure(err: Error?) {
                hideLoading()
                err?.let { toast(it.message.toString()) }
            }
        })
    }

    fun register(name: String, email: String, password: String, phone: String){
        setLoading()
        getFirebaseToken(name, email, password, phone)
    }

    fun validate(name: String, email: String, password: String, confirmPassword : String, telp : String): Boolean{
        state.value = RegisterState.Reset
        if (name.isEmpty()){
            state.value = RegisterState.Validate(name = "nama tidak boleh kosong")
            return false
        }

        if (!Constants.isAlpha(name)){
            state.value = RegisterState.Validate(name = "nama hanya mengandung huruf saja")
            return false
        }

        if (email.isEmpty()){
            state.value = RegisterState.Validate(email = "email tidak boleh kosong")
            return false
        }

        if (!Constants.isValidEmail(email)){
            state.value = RegisterState.Validate(email = "email tidak valid")
            return false
        }

        if (password.isEmpty()){
            state.value = RegisterState.Validate(password ="password tidak boleh kosong")
            return false
        }
        if (!Constants.isValidPassword(password)){
            state.value = RegisterState.Validate(password = "password minimal 8 karakter")
            return false
        }
        if (confirmPassword.isEmpty()){
            state.value = RegisterState.Validate(confirmPassword = "konfirmasi password tidak boleh kosong")
            return false
        }
        if(confirmPassword != password){
            state.value = RegisterState.Validate(confirmPassword = "Konfirmasi password tidak cocok")
            return false
        }

        if (telp.isEmpty()){
            state.value = RegisterState.Validate(telp = "no telepon harus di isi")
            return false
        }
        if (telp.length <= 10 || telp.length >= 13){
            state.value = RegisterState.Validate(telp = "no telepon setidaknya 11 sampai 13 karakter")
            return false
        }
        return true
    }

    fun listenToState() = state
}

sealed class RegisterState{
    object Reset : RegisterState()
    data class IsLoading (var state : Boolean = false) : RegisterState()
    data class ShowToast(var message : String) : RegisterState()
    data class Validate(
        var name : String? = null,
        var email : String? = null,
        var password : String? = null,
        var confirmPassword : String? = null,
        var telp : String? = null
    ) : RegisterState()
    data class Success(var message : String) :RegisterState()
}