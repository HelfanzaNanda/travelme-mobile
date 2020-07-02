package com.travelme.customer.activities.register_activity


import androidx.lifecycle.ViewModel
import com.travelme.customer.repositories.FirebaseRepository
import com.travelme.customer.repositories.UserRepository
import com.travelme.customer.utilities.Constants
import com.travelme.customer.utilities.SingleLiveEvent
import retrofit2.Callback

class RegisterViewModel(private val userRepository: UserRepository, private val firebaseRepository: FirebaseRepository)
    : ViewModel(){

    private var state : SingleLiveEvent<RegisterState> = SingleLiveEvent()

    private fun setLoading() { state.value = RegisterState.IsLoading(true) }
    private fun hideLoading() { state.value = RegisterState.IsLoading(false) }
    private fun toast(message: String) { state.value = RegisterState.ShowToast(message) }
    private fun success(message: String) { state.value = RegisterState.Success(message) }

    private fun getFirebaseToken(name: String, email: String, password: String, phone : String){
        firebaseRepository.getFirebaseToken(){firebaseToken, error ->
            hideLoading()
            error?.let { it.message?.let { message-> toast(message) } }
            firebaseToken?.let {
                toast(it)
                userRepository.register(name, email, password, phone, it){resultEmail, err ->
                    err?.let { it.message?.let { message-> toast(message) } }
                    resultEmail?.let {result->
                        success(result)
                    }
                }
            }
        }
    }

    fun register(name: String, email: String, password: String, phone: String){
        setLoading()
        getFirebaseToken(name, email, password, phone)
//        userRepository.register(name, email, password, phone){resultEmail, error->
//            hideLoading()
//            error?.let { it.message?.let { message->
//                toast(message)
//            }}
//            resultEmail?.let { success(it) }
//        }
    }

    fun validate(name: String, email: String, password: String, confirmPassword : String, telp : String): Boolean{
        state.value = RegisterState.Reset
        if (name.isEmpty()){
            state.value = RegisterState.ShowToast("nama tidak boleh kosong")
            return false
        }
        if (name.length < 5){
            state.value = RegisterState.ShowToast("nama setidaknya 5 karakter")
            return false
        }
        if (telp.isEmpty()){
            state.value = RegisterState.ShowToast("no telepon harus di isi")
        }
        if (telp.length < 11 && telp.length > 13){
            state.value = RegisterState.ShowToast("no telepon setidaknya 11 sampai 13 karakter")
        }

        if (email.isEmpty() || password.isEmpty()){
            state.value = RegisterState.ShowToast("mohon isi semua form")
            return false
        }
        if (!Constants.isValidEmail(email)){
            state.value = RegisterState.Validate(email = "email tidak valid")
            return false
        }
        if (!Constants.isValidPassword(password)){
            state.value = RegisterState.Validate(password = "password tidak valid")
            return false
        }
        if (confirmPassword.isEmpty()){
            state.value = RegisterState.ShowToast("Isi semua form terlebih dahulu")
            return false
        }
        if(!confirmPassword.equals(password)){
            state.value = RegisterState.Validate(confirmPassword = "Konfirmasi password tidak cocok")
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