package com.travelme.customer.activities.order_activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.travelme.customer.models.Order
import com.travelme.customer.models.User
import com.travelme.customer.repositories.OrderRepository
import com.travelme.customer.repositories.UserRepository
import com.travelme.customer.utilities.SingleLiveEvent
import com.travelme.customer.utilities.WrappedListResponse
import com.travelme.customer.utilities.WrappedResponse
import com.travelme.customer.webservices.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field

class OrderActivityViewModel(private val orderRepository: OrderRepository, private val userRepository: UserRepository) : ViewModel(){

    private var order = MutableLiveData<Order>()
    private val user = MutableLiveData<User>()
    private var state : SingleLiveEvent<OrderActivityState> = SingleLiveEvent()

    private fun setLoading() { state.value = OrderActivityState.IsLoading(true) }
    private fun hideLoading() { state.value = OrderActivityState.IsLoading(false) }
    private fun toast(message: String) { state.value = OrderActivityState.ShowToast(message) }
    private fun success() { state.value = OrderActivityState.Success }
    private fun reset() { state.value = OrderActivityState.Reset }
    private fun alert() { state.value = OrderActivityState.Alert}

    fun validate(pickup_location: String, destination_location: String): Boolean{
        reset()
        if (pickup_location.isEmpty()){
            toast("lokasi penjemputan silahkan di isi")
            return false
        }
        if (destination_location.isEmpty()){
            toast("lokasi tujuan silahkan di isi")
            return false
        }
        return true
    }

    fun storeOrder(token : String, owner_id : Int, departure_id : Int, date : String,
                   hour : String, price : Int, total_seat: Int, pickup_location: String, destination_location: String){
        setLoading()
        orderRepository.createorder(token, owner_id, departure_id, date, hour, price, total_seat, pickup_location,
            destination_location){resultBool, error->
            hideLoading()
            error?.let { it.message?.let { mesage-> toast(mesage)  }}
            if (resultBool){
                alert()
            }
        }
    }

    fun getUserLogin(token: String){
        setLoading()
        userRepository.profile(token){resultUser, error->
            hideLoading()
            error?.let { it.message?.let { mesage-> toast(mesage)  }}
            resultUser?.let { user.postValue(it) }
        }
    }

    fun listenToState() = state
    fun listenToUser() = user
}

sealed class OrderActivityState{
    object Reset : OrderActivityState()
    data class IsLoading (var state : Boolean = false) : OrderActivityState()
    data class ShowToast(var message : String) : OrderActivityState()
    object Success: OrderActivityState()
    object Alert : OrderActivityState()
    data class Validate(
        var pickup_location : String? = null,
        var destination_location : String? = null
    ) : OrderActivityState()

}