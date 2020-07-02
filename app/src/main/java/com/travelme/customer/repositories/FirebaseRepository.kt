package com.travelme.customer.repositories

import com.google.firebase.iid.FirebaseInstanceId
import com.travelme.customer.webservices.ApiService

class FirebaseRepository {

    fun getFirebaseToken(result : (String?, Error?)->Unit){
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            if (it.isSuccessful){
                it.result?.let {result->
                    result(result.token, null)
                } ?: kotlin.run {
                    result(null, Error("Failed to get firebase token"))
                }
            }else{
                result(null, Error("Cannot get firebase token"))
            }
        }
    }

}