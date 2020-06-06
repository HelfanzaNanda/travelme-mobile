package com.travelme.customer.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.travelme.customer.R
import com.travelme.customer.adapters.MyOrderAdapter
import com.travelme.customer.models.Order
import com.travelme.customer.utilities.Constants
import com.travelme.customer.viewmodels.OrderState
import com.travelme.customer.viewmodels.OrderViewModel
import kotlinx.android.synthetic.main.fragment_order.*
import kotlinx.android.synthetic.main.fragment_order.view.*

class OrderFragment : Fragment(R.layout.fragment_order){
    private lateinit var orderViewModel: OrderViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        orderViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
        view.rv_my_order.apply {
            layoutManager = LinearLayoutManager(activity!!)
            adapter = MyOrderAdapter(mutableListOf(), activity!!, orderViewModel)
        }
        orderViewModel.getOrders().observe(viewLifecycleOwner, Observer {handleData(it)})
        orderViewModel.getState().observer(viewLifecycleOwner, Observer { handleui(it) })
        initEmptyView()
    }

    private fun initEmptyView(){
        if (orderViewModel.getOrders().value == null || orderViewModel.getOrders().value!!.isEmpty()){
            view!!.iv_empty_data.visibility = View.VISIBLE
            view!!.tv_empty_data.visibility = View.VISIBLE
        }else{
            view!!.iv_empty_data.visibility = View.GONE
            view!!.tv_empty_data.visibility = View.GONE
        }
    }

    private fun handleData(it : List<Order>){
        if (it.isNullOrEmpty()){
            view!!.iv_empty_data.visibility = View.VISIBLE
            view!!.tv_empty_data.visibility = View.VISIBLE
        }
        view!!.rv_my_order.adapter?.let {adapter ->
            if (adapter is MyOrderAdapter){
                adapter.changelist(it)
            }
        }
    }

    private fun handleui(it : OrderState){
        when(it){
            is OrderState.IsLoading ->{
                if (it.state){
                    view!!.iv_empty_data.visibility = View.GONE
                    view!!.tv_empty_data.visibility = View.GONE
                    view!!.pb_my_order.isIndeterminate = true
                    view!!.pb_my_order.visibility = View.VISIBLE
                }else{
                    view!!.pb_my_order.isIndeterminate = false
                    view!!.pb_my_order.visibility = View.GONE
                }
            }
            is OrderState.ShowToast -> toast(it.message)
            is OrderState.SuccessDelete -> orderViewModel.getMyOrders(Constants.getToken(activity!!))
        }
    }

    override fun onResume() {
        super.onResume()
        orderViewModel.getMyOrders(Constants.getToken(activity!!))
    }

    private fun toast(message : String) = Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()

}