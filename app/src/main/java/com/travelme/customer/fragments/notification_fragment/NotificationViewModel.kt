package com.travelme.customer.fragments.notification_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.travelme.customer.models.Order
import com.travelme.customer.repositories.OrderRepository
import com.travelme.customer.utilities.SingleLiveEvent

class NotificationViewModel(private var orderRepository: OrderRepository) : ViewModel(){
    private val state : SingleLiveEvent<NotificationState> = SingleLiveEvent()
    private val orders = MutableLiveData<List<Order>>()

    private fun toast(message: String) { state.value = NotificationState.ShowToast(message) }
    private fun setLoading() { state.value = NotificationState.IsLoading(true) }
    private fun hideLoading() { state.value = NotificationState.IsLoading(false) }

    fun getOrderVerify(token :String){
        setLoading()
        orderRepository.orderVerify(token){resultOrder, error->
            hideLoading()
            error?.let { it.message?.let { message-> toast(message) }}
            resultOrder?.let { orders.postValue(it) }
        }
    }

    fun driverArrived(token :String){
        setLoading()
        orderRepository.driverArrived(token){resultOrder, error->
            hideLoading()
            error?.let { it.message?.let { message-> toast(message) }}
            resultOrder?.let { orders.postValue(it) }
        }
    }

    fun listenToState() = state
    fun listenToOrders() = orders
}

sealed class NotificationState{
    data class IsLoading(var state: Boolean = false) : NotificationState()
    data class ShowToast(var message: String) : NotificationState()
}