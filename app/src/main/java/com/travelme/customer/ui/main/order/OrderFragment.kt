package com.travelme.customer.ui.main.order

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.travelme.customer.R
import com.travelme.customer.ui.login.LoginActivity
import com.travelme.customer.utilities.extensions.gone
import com.travelme.customer.utilities.extensions.visible
import com.travelme.customer.models.Order
import com.travelme.customer.utilities.Constants
import kotlinx.android.synthetic.main.fragment_order.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrderFragment : Fragment(R.layout.fragment_order){
    private val orderFragmentViewModel : OrderFragmentViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpUI()
        
    }
    private fun setUpUI() {
        if (isLoggedIn()){
            requireView().rv_my_order.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = MyOrderAdapter(mutableListOf(), requireActivity(), orderFragmentViewModel)
            }
            observer()
            observe()
        }else{
            popupIsLogin("anda belum login, untuk melanjutkan anda harus login dahulu!")
        }
    }

    private fun observer() = orderFragmentViewModel.listenToState().observer(viewLifecycleOwner, Observer { handleUI(it) })
    private fun observe() = orderFragmentViewModel.listenToOrders().observe(viewLifecycleOwner, Observer { handleData(it) })

    private fun popupIsLogin(message: String){
        AlertDialog.Builder(activity!!).apply {
            setMessage(message)
            setPositiveButton("login"){dialog, _ ->
                startActivity(Intent(activity, LoginActivity::class.java).putExtra("EXCEPT_RESULT", true))
                dialog.dismiss()
            }
        }.show()
    }

    private fun handleData(it : List<Order>){
        if (it.isNullOrEmpty()){
            requireView().iv_empty_data.visible()
            requireView().tv_empty_data.visible()
        }
        requireView().rv_my_order.adapter?.let {adapter ->
            if (adapter is MyOrderAdapter){
                adapter.changelist(it)
            }
        }
    }

    private fun handleUI(it : OrderFragmentState){
        when(it){
            is OrderFragmentState.IsLoading -> handleLoading(it.state)
            is OrderFragmentState.ShowToast -> toast(it.message)
            is OrderFragmentState.SuccessDelete -> fetchMyOrders()
            is OrderFragmentState.SuccessUpdate -> fetchMyOrders()
            is OrderFragmentState.SuccessConfirm -> fetchMyOrders()
        }
    }

    private fun isLoggedIn() = !Constants.getToken(activity!!).equals("UNDEFINED")
    private fun fetchMyOrders() = orderFragmentViewModel.getMyOrders(Constants.getToken(requireActivity()))
    private fun handleLoading(state: Boolean) {
        if (state){
            requireView().pb_my_order.visible()
        }else{
            requireView().pb_my_order.gone()
        }
    }

    override fun onResume() {
        super.onResume()
        fetchMyOrders()
    }

    private fun toast(message : String) = Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
}