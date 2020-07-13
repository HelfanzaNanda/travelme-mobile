package com.travelme.customer.fragments.order

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.travelme.customer.R
import com.travelme.customer.activities.login.LoginActivity
import com.travelme.customer.extensions.gone
import com.travelme.customer.extensions.visible
import com.travelme.customer.models.Order
import com.travelme.customer.utilities.Constants
import kotlinx.android.synthetic.main.fragment_order.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrderFragment : Fragment(R.layout.fragment_order){
    private val orderFragmentViewModel : OrderFragmentViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (isLoggedIn()){
            view.rv_my_order.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = MyOrderAdapter(
                    mutableListOf(),
                    activity!!,
                    orderFragmentViewModel
                )
            }
            orderFragmentViewModel.listenToState().observer(viewLifecycleOwner, Observer { handleUI(it) })
            orderFragmentViewModel.listenToOrders().observe(viewLifecycleOwner, Observer { handleData(it) })
        }else{
            popupIsLogin("anda belum login, untuk melanjutkan anda harus login dahulu!")
        }
    }

    private fun popupIsLogin(message: String){
        AlertDialog.Builder(activity!!).apply {
            setMessage(message)
            setPositiveButton("login"){dialog, _ ->
                startActivity(Intent(activity, LoginActivity::class.java).putExtra("EXCEPT_RESULT", true))
                dialog.dismiss()
            }
        }.show()
    }

    private fun isLoggedIn() = !Constants.getToken(activity!!).equals("UNDEFINED")

    private fun handleData(it : List<Order>){
        if (it.isNullOrEmpty()){
            view!!.iv_empty_data.visible()
            view!!.tv_empty_data.visible()
        }
        view!!.rv_my_order.adapter?.let {adapter ->
            if (adapter is MyOrderAdapter){
                adapter.changelist(it)
            }
        }
    }

    private fun handleUI(it : OrderFragmentState){
        when(it){
            is OrderFragmentState.IsLoading ->{
                if (it.state){
                    view!!.pb_my_order.visible()
                }else{
                    view!!.pb_my_order.gone()
                }
            }
            is OrderFragmentState.ShowToast -> toast(it.message)
            is OrderFragmentState.SuccessDelete -> orderFragmentViewModel.getMyOrders(Constants.getToken(requireActivity()))
            is OrderFragmentState.SuccessUpdate -> orderFragmentViewModel.getMyOrders(Constants.getToken(requireActivity()))
            is OrderFragmentState.SuccessConfirm -> orderFragmentViewModel.getMyOrders(Constants.getToken(requireActivity()))
        }
    }

    override fun onResume() {
        super.onResume()
        orderFragmentViewModel.getMyOrders(Constants.getToken(activity!!))
    }

    private fun toast(message : String) = Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
}