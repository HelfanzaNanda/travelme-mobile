package com.travelme.customer.repositories

import com.google.gson.GsonBuilder
import com.travelme.customer.models.User
import com.travelme.customer.utilities.SingleResponse
import com.travelme.customer.utilities.WrappedResponse
import com.travelme.customer.webservices.ApiService
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

interface UserContract {
    fun updateProfile(token : String, name: String, password: String, listener : SingleResponse<User>)
    fun updatePhotoProfile(token : String, urlImg : String, listener: SingleResponse<User>)
    fun forgotPassword(email : String, listener : SingleResponse<User>)
}

class UserRepository (private val api : ApiService) : UserContract {

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
                    result(null, Error("masukkan email dan password yang benar"))
                }
            }
        })
    }

    fun register(name: String, email: String, password: String, phone: String, fcmToken : String, result: (String?, Error?) -> Unit){
        api.register(name, email, password, phone, fcmToken).enqueue(object :
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
                    result(null, Error("gagal register, mungkin email sudah pernah di daftarkan"))
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

    override fun updateProfile(token: String, name: String, password: String, listener: SingleResponse<User>) {
        api.updateProfile(token, name, password).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedResponse<User>>,
                response: Response<WrappedResponse<User>>
            ) {
                when {
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!){
                            listener.onSuccess(body.data)
                        }else{
                            listener.onFailure(Error(body.message))
                        }
                    }
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun updatePhotoProfile(token: String, pathImage: String, listener: SingleResponse<User>) {
        val file = File(pathImage)
        val requestBodyForFile = RequestBody.create(MediaType.parse("image/*"), file)
        val image = MultipartBody.Part.createFormData("avatar", file.name, requestBodyForFile)
        api.updatePhotoProfile(token, image).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                listener.onFailure(java.lang.Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!){
                            listener.onSuccess(body.data)
                        }
                    }
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }
        })
    }

    override fun forgotPassword(email: String, listener: SingleResponse<User>) {
        api.forgotPassword(email).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                when {
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!){
                            listener.onSuccess(body.data)
                        }else{
                            listener.onFailure(Error(body.message))
                        }
                    }
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }
}