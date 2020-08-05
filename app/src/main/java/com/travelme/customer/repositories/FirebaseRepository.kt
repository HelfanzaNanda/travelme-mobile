package com.travelme.customer.repositories

import com.google.firebase.iid.FirebaseInstanceId
import com.travelme.customer.utilities.SingleResponse


interface FirebaseContract{
    fun getFirebaseToken(listener : SingleResponse<String>)
}

class FirebaseRepository : FirebaseContract{
    override fun getFirebaseToken(listener: SingleResponse<String>) {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            when {
                it.isSuccessful -> {
                    it.result?.let {result->
                        listener.onSuccess(result.token)
                    } ?: kotlin.run {
                        listener.onFailure(Error("Failed to get firebase token"))
                    }
                }
                !it.isSuccessful -> listener.onFailure(Error("Cannot get firebase token"))
            }
        }
    }

}