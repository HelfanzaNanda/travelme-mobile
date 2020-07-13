package com.travelme.customer.fragments.order


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.travelme.customer.models.Order
import com.travelme.customer.repositories.OrderRepository
import com.travelme.customer.utilities.SingleLiveEvent

class OrderFragmentViewModel(private val orderRepository: OrderRepository) : ViewModel(){
    private var orders = MutableLiveData<List<Order>>()
    private var state : SingleLiveEvent<OrderFragmentState> = SingleLiveEvent()

    private fun setLoading() { state.value = OrderFragmentState.IsLoading(true) }
    private fun hideLoading() { state.value = OrderFragmentState.IsLoading(false) }
    private fun toast(message: String) { state.value = OrderFragmentState.ShowToast(message) }
    private fun successDelete() { state.value = OrderFragmentState.SuccessDelete }
    private fun successUpdate() { state.value = OrderFragmentState.SuccessUpdate }
    private fun successConfirm() { state.value  = OrderFragmentState.SuccessConfirm }

    fun getMyOrders(token: String){
        setLoading()
        orderRepository.getMyOrders(token){resultOrder, error ->
            hideLoading()
            error?.let { it.message?.let { message-> toast(message) }}
            resultOrder?.let { orders.postValue(it) }
        }
    }

    fun cancel(token: String, id : String){
        setLoading()
        orderRepository.canceloroder(token, id){resultBool, error ->
            hideLoading()
            error?.let { it.message?.let { message-> toast(message) }}
            if (resultBool){
                successDelete()
            }
        }
    }

    fun confirm(token: String, id : String){
        setLoading()
        orderRepository.confirmOroder(token, id){resultBool, error ->
            hideLoading()
            error?.let { it.message?.let { message-> toast(message) }}
            if (resultBool){
                successConfirm()
            }
        }
    }

    fun updatestatus(token : String, id: String, status : String){
        setLoading()
        orderRepository.updatestatus(token, id, status){resultBool, error ->
            hideLoading()
            error?.let { it.message?.let {message-> toast(message) }}
            if (resultBool){
                getMyOrders(token)
                //successUpdate()
            }
        }
    }

    fun listenToState() = state
    fun listenToOrders() = orders
}

sealed class OrderFragmentState{
    data class IsLoading (var state : Boolean = false) : OrderFragmentState()
    data class ShowToast(var message : String) : OrderFragmentState()
    object SuccessDelete : OrderFragmentState()
    object SuccessUpdate : OrderFragmentState()
    object SuccessConfirm : OrderFragmentState()
}