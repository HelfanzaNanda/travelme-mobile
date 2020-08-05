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
    fun updatePhotoProfile(token : String, pathImage : String, listener: SingleResponse<User>)
    fun forgotPassword(email : String, listener : SingleResponse<User>)
    fun login (email: String, password: String, listener : SingleResponse<User>)
    fun register(name: String, email: String, password: String, phone: String, fcmToken : String, listener: SingleResponse<User>)
    fun profile(token : String, listener: SingleResponse<User>)
}

class UserRepository (private val api : ApiService) : UserContract {

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

    override fun login(email: String, password: String, listener: SingleResponse<User>) {
        api.login(email, password).enqueue(object : Callback<WrappedResponse<User>> {
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
                            listener.onFailure(Error("tidak dapat login. pastikan email dan password benar dan sudah di verifikasi"))
                        }
                    }
                    !response.isSuccessful -> listener.onFailure(Error("masukkan email dan password yang benar"))
                }
            }
        })
    }

    override fun register(name: String, email: String, password: String, phone: String, fcmToken: String, listener: SingleResponse<User>) {
        api.register(name, email, password, phone, fcmToken).enqueue(object :
            Callback<WrappedResponse<User>> {
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
                            listener.onFailure(Error("tidak dapat register"))
                        }
                    }
                    !response.isSuccessful -> listener.onFailure(Error("gagal register, mungkin email sudah pernah di daftarkan"))
                }
            }
        })
    }

    override fun profile(token: String, listener: SingleResponse<User>) {
        api.profile(token).enqueue(object : Callback<WrappedResponse<User>> {
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                when {
                    response.isSuccessful ->listener.onSuccess(response.body()!!.data)
                    !response.isSuccessful -> listener.onFailure(Error("gagal register, mungkin email sudah pernah di daftarkan"))
                }
            }
        })
    }
}