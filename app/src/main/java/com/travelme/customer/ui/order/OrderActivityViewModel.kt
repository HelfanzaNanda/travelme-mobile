package com.travelme.customer.ui.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.travelme.customer.models.CreateOrder
import com.travelme.customer.models.Order
import com.travelme.customer.models.Seat
import com.travelme.customer.models.User
import com.travelme.customer.repositories.OrderRepository
import com.travelme.customer.repositories.UserRepository
import com.travelme.customer.utilities.SingleLiveEvent
import com.travelme.customer.utilities.SingleResponse

class OrderActivityViewModel(private val orderRepository: OrderRepository, private val userRepository: UserRepository) : ViewModel(){

    private val user = MutableLiveData<User>()
    private val state : SingleLiveEvent<OrderActivityState> = SingleLiveEvent()
    private var createOrder = MutableLiveData<CreateOrder>()
    private val seats =  MutableLiveData<ArrayList<Seat>>()

    private fun setLoading() { state.value = OrderActivityState.IsLoading(true) }
    private fun hideLoading() { state.value = OrderActivityState.IsLoading(false) }
    private fun toast(message: String) { state.value = OrderActivityState.ShowToast(message) }
    private fun reset() { state.value = OrderActivityState.Reset }
    private fun alert() { state.value = OrderActivityState.Alert}

    fun validate(pickup_location: String, destination_location: String, seats : ArrayList<Seat>?, payment : Boolean?): Boolean{
        reset()
        if (pickup_location.isEmpty()){
            state.value = OrderActivityState.Validate(pickup_location = "lokasi penjemputan silahkan di isi")
            return false
        }
        if (destination_location.isEmpty()){
            state.value = OrderActivityState.Validate(destination_location = "lokasi tujuan silahkan di isi")
            return false
        }

        if (seats.isNullOrEmpty()){
            toast("harus pilih kursi dahulu")
            return false
        }

        if (payment == null){
            toast("harus pilih metode pembayaran")
            return false
        }
        return true
    }

    fun storeOrder(token : String, createOrder: CreateOrder){
        setLoading()
        orderRepository.createOrder(token, createOrder, object : SingleResponse<CreateOrder>{
            override fun onSuccess(data: CreateOrder?) {
                hideLoading()
                data?.let { alert() }
            }

            override fun onFailure(err: Error?) {
                err?.let { toast(it.message.toString()) }
            }
        })
    }

    fun selectPayment(b : Boolean){
        val c = CreateOrder(payment = b)
        createOrder.postValue(c)
    }

    fun resultSelectSeat(s : ArrayList<Seat>) = seats.postValue(s)

    fun getUserLogin(token: String){
        setLoading()
        userRepository.profile(token, object : SingleResponse<User>{
            override fun onSuccess(data: User?) {
                hideLoading()
                data?.let { user.postValue(it) }
            }

            override fun onFailure(err: Error?) {
                hideLoading()
                err?.let { toast(it.message.toString()) }
            }
        })
    }

    fun listenToState() = state
    fun listenToUser() = user
    fun listenToSelectPayment() = createOrder
    fun listenToResultSelectSeat() = seats
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