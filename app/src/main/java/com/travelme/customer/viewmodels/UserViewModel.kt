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

    private fun setLoading() { state.value = UserState.IsLoading(true) }
    private fun hideLoading() { state.value = UserState.IsLoading(false) }
    private fun toast(message: String) { state.value = UserState.ShowToast(message) }
    private fun success(message: String) { state.value = UserState.Success(message) }

    fun login(email: String, password: String){
        setLoading()
        api.login(email, password).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                hideLoading()
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val token = body.data!!.token
                        toast(body.message!!)
                        success(token!!)
                    }else{
                        toast("tidak dapat login")
                    }
                }else{
                    toast("login gagal")
                }
                hideLoading()
            }

        })
    }

    fun register(name: String, email: String, password: String, phone: String){
        setLoading()
        api.register(name, email, password, phone).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                hideLoading()
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    body?.let {
                        if (it.status!!){
                            success(email)
                        }else{
                            toast(it.message!!)
                        }
                    }
                }else{
                    toast("email sudah terdaftar, silahkan pakai email lainnya! "+response.message())
                }
                hideLoading()
            }

        })
    }

    fun profile(token: String){
        setLoading()
        api.profile(token).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                hideLoading()
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data
                        user.postValue(data)
                    }else{
                        toast("gagal memuat info")
                    }
                }else{
                    toast("kesalahan pada mengambil info ${response.message()}")
                }
                hideLoading()
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
            if (telp.length < 11 && telp.length > 13){
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
    data class Success(var message : String) :UserState()
}