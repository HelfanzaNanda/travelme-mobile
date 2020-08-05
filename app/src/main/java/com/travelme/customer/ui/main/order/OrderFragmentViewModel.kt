package com.travelme.customer.ui.main.order


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.travelme.customer.models.Order
import com.travelme.customer.repositories.OrderRepository
import com.travelme.customer.utilities.ArrayResponse
import com.travelme.customer.utilities.SingleLiveEvent
import com.travelme.customer.utilities.SingleResponse

class OrderFragmentViewModel(private val orderRepository: OrderRepository) : ViewModel(){
    private var orders = MutableLiveData<List<Order>>()
    private var state : SingleLiveEvent<OrderFragmentState> = SingleLiveEvent()

    private fun setLoading() { state.value = OrderFragmentState.IsLoading(true) }
    private fun hideLoading() { state.value = OrderFragmentState.IsLoading(false) }
    private fun toast(message: String) { state.value = OrderFragmentState.ShowToast(message) }
    private fun successDelete() { state.value = OrderFragmentState.SuccessDelete }
    private fun successConfirm() { state.value  = OrderFragmentState.SuccessConfirm }

    fun getMyOrders(token: String){
        setLoading()
        orderRepository.fetchMyOrders(token, object : ArrayResponse<Order>{
            override fun onSuccess(datas: List<Order>?) {
                hideLoading()
                datas?.let { orders.postValue(it) }
            }

            override fun onFailure(err: Error?) {
                hideLoading()
                err?.let { toast(it.message.toString()) }
            }
        })
    }

    fun cancel(token: String, id : String){
        setLoading()
        orderRepository.cancelOrder(token, id, object : SingleResponse<Order>{
            override fun onSuccess(data: Order?) {
                hideLoading()
                data?.let { successDelete() }
            }

            override fun onFailure(err: Error?) {
                hideLoading()
                err?.let { toast(it.message.toString()) }
            }
        })
    }

    fun confirm(token: String, id : String){
        setLoading()
        orderRepository.confirmOrder(token, id, object : SingleResponse<Order>{
            override fun onSuccess(data: Order?) {
                hideLoading()
                data?.let { successConfirm() }
            }

            override fun onFailure(err: Error?) {
                hideLoading()
                err?.let { toast(it.message.toString()) }
            }

        })
    }

    fun updatestatus(token : String, id: String, status : String){
        setLoading()
        orderRepository.updateStatus(token, id, status, object : SingleResponse<Order>{
            override fun onSuccess(data: Order?) {
                hideLoading()
                data?.let { getMyOrders(token) }
            }

            override fun onFailure(err: Error?) {
                hideLoading()
                err?.let { toast(it.message.toString()) }
            }
        })
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