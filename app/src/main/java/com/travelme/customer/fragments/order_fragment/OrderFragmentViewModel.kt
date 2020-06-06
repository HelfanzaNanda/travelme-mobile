package com.travelme.customer.fragments.order_fragment


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.travelme.customer.models.Order
import com.travelme.customer.repositories.OrderRepository
import com.travelme.customer.utilities.SingleLiveEvent
import com.travelme.customer.utilities.WrappedListResponse
import com.travelme.customer.utilities.WrappedResponse
import com.travelme.customer.webservices.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field

class OrderFragmentViewModel(private val orderRepository: OrderRepository) : ViewModel(){
    private var orders = MutableLiveData<List<Order>>()
    private var state : SingleLiveEvent<OrderFragmentState> = SingleLiveEvent()

    private fun setLoading() { state.value = OrderFragmentState.IsLoading(true) }
    private fun hideLoading() { state.value = OrderFragmentState.IsLoading(false) }
    private fun toast(message: String) { state.value = OrderFragmentState.ShowToast(message) }
    private fun successDelete() { state.value = OrderFragmentState.SuccessDelete }

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
                OrderFragmentState.SuccessDelete
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
}