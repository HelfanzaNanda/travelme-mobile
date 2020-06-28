package com.travelme.customer.repositories

import com.travelme.customer.models.User
import com.travelme.customer.utilities.WrappedResponse
import com.travelme.customer.webservices.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository (private val api : ApiService){

    fun login(email: String, password: String, result: (String?, Error?) -> Unit){
        api.login(email, password).enqueue(object : Callback<WrappedResponse<User>> {
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                result(null, Error(t.message))
            }
            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val token = body.data!!.token
                        result(token, null)
                    }else{
                        result(null, Error("tidak dapat login. pastikan email dan password benar dan sudah di verifikasi"))
                    }
                }else{
                    result(null, Error("login gagal"))
                }
            }

        })
    }

    fun register(name: String, email: String, password: String, phone: String, result: (String?, Error?) -> Unit){
        api.register(name, email, password, phone).enqueue(object :
            Callback<WrappedResponse<User>> {
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                result(null, Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        result(email, null)
                    }else{
                        result(null, Error("tidak dapat register"))
                    }
                }else{
                    result(null, Error(response.message()))
                }
            }

        })
    }

    fun profile(token: String, result: (User?, Error?) -> Unit){
        api.profile(token).enqueue(object : Callback<WrappedResponse<User>> {
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                result(null, Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body?.status!!){
                        val data = body.data
                        result(data, null)
                    }else{
                        result(null, Error())
                    }
                }else{
                    result(null, Error(response.message()))
                }
            }

        })
    }
}