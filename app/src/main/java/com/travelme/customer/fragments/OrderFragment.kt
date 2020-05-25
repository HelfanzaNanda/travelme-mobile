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
import com.travelme.customer.utilities.Constants
import com.travelme.customer.viewmodels.OrderState
import com.travelme.customer.viewmodels.OrderViewModel
import kotlinx.android.synthetic.main.fragment_order.*

class OrderFragment : Fragment(R.layout.fragment_order){
    private lateinit var orderViewModel: OrderViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        rv_my_order.apply {
            layoutManager = LinearLayoutManager(activity!!)
            adapter = MyOrderAdapter(mutableListOf(), activity!!)
        }
        orderViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
        orderViewModel.getMyOrder(Constants.getToken(activity!!))
        orderViewModel.getOrders().observe(viewLifecycleOwner, Observer {
            rv_my_order.adapter?.let {adapter ->
                if (adapter is MyOrderAdapter){
                    adapter.changelist(it)
                    for (x in it){
                        x.owner.business_name
                    }
                    if(it.isNullOrEmpty()){
                        iv_no_data.visibility = View.VISIBLE
                        tv_no_data.text = "tidak ada pesanan"
                        tv_no_data.visibility = View.VISIBLE
                    }else{
                        iv_no_data.visibility = View.GONE
                        tv_no_data.visibility = View.GONE
                    }
                }
            }
        })
        orderViewModel.getState().observer(viewLifecycleOwner, Observer {
            handleui(it)
        })

    }

    private fun handleui(it : OrderState){
        when(it){
            is OrderState.IsLoading ->{
                if (it.state){
                    pb_my_order.isIndeterminate = true
                    pb_my_order.visibility = View.VISIBLE
                }else{
                    pb_my_order.isIndeterminate = false
                    pb_my_order.visibility = View.GONE
                }
            }
            is OrderState.ShowToast -> toast(it.message)
        }
    }

    private fun toast(message : String) = Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()

}