package com.travelme.customer.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.travelme.customer.models.User
import com.travelme.customer.utilities.Constants
import com.travelme.customer.utilities.SingleLiveEvent
import com.travelme.customer.utilities.WrappedResponse
import com.travelme.customer.webservices.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel(){
    private var user = MutableLiveData<User>()
    private var state : SingleLiveEvent<UserState> = SingleLiveEvent()
    private var api = ApiClient.instance()


    fun login(email: String, password: String){
        state.value = UserState.IsLoading(true)
        api.login(email, password).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                println("OnFailure : "+t.message)
                state.value = UserState.IsLoading(false)
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val token = body.data!!.token
                        state.value = UserState.ShowToast("selamat datang $email")
                        state.value = UserState.Success(token!!)
                    }else{
                        state.value = UserState.ShowToast("tidak dapat login!")
                    }
                }else{
                    state.value = UserState.ShowToast("login gagal")
                }
                state.value = UserState.IsLoading(false)
            }

        })
    }

    fun register(name: String, email: String, password: String, phone: String){
        state.value = UserState.IsLoading(false)
        api.register(name, email, password, phone).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                println("OnFailure : "+t.message)
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    body?.let {
                        if (it.status!!){
                            state.value = UserState.Success(email)
                        }else{
                            state.value = UserState.ShowToast(it.message!!)
                        }
                    }
                }else{
                    println("email sudah terdaftar, silahkan pakai email lainnya! "+response.message())
                }
                state.value = UserState.IsLoading(false)
            }

        })
    }

    fun getUserIsLogin(token: String){
        println("token : " +token)
        state.value = UserState.IsLoading(true)
        api.getUserIsLogin(token).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                println("onFailure : "+t.message)
                state.value = UserState.IsLoading(false)
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data
                        user.postValue(data)
                    }else{
                        state.value = UserState.ShowToast("tidak dapat memuat info")
                    }
                }else{
                    state.value = UserState.ShowToast("Kesalahan saat mengambil info user")
                }
                state.value = UserState.IsLoading(false)
            }

        })
    }

    fun validate(name: String?, email: String, password: String, confirmPassword : String?, telp : String?): Boolean{
        state.value = UserState.Reset
        if (name != null){
            if (name.isEmpty()){
                state.value = UserState.ShowToast("nama tidak boleh kosong")
                return false
            }
            if (name.length < 5){
                state.value = UserState.ShowToast("nama setidaknya 5 karakter")
                return false
            }
        }
        if (telp != null){
            if (telp.isEmpty()){
                state.value = UserState.ShowToast("no telepon harus di isi")
            }
            if (telp.length < 11 || telp.length > 11){
                state.value = UserState.ShowToast("no telepon setidaknya 11 sampai 13 karakter")
            }
        }

        if (email.isEmpty() || password.isEmpty()){
            state.value = UserState.ShowToast("mohon isi semua form")
            return false
        }
        if (!Constants.isValidEmail(email)){
            state.value = UserState.Validate(email = "email tidak valid")
            return false
        }
        if (!Constants.isValidPassword(password)){
            state.value = UserState.Validate(password = "password tidak valid")
            return false
        }
        if(confirmPassword != null){
            if (confirmPassword.isEmpty()){
                state.value = UserState.ShowToast("Isi semua form terlebih dahulu")
                return false
            }
            if(!confirmPassword.equals(password)){
                state.value = UserState.Validate(confirmPassword = "Konfirmasi password tidak cocok")
                return false
            }
        }
        return true
    }

    fun getState() = state
    fun getUser() = user
}

sealed class UserState{
    object Reset : UserState()
    data class IsLoading (var state : Boolean = false) : UserState()
    data class ShowToast(var message : String) : UserState()
    data class Validate(
        var name : String? = null,
        var email : String? = null,
        var password : String? = null,
        var confirmPassword : String? = null,
        var telp : String? = null
    ) : UserState()
    data class Success(var token : String) :UserState()
}