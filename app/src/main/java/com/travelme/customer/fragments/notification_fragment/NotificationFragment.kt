package com.travelme.customer.fragments.notification_fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.travelme.customer.R
import com.travelme.customer.adapters.NotificationAdapter
import com.travelme.customer.extensions.gone
import com.travelme.customer.extensions.visible
import com.travelme.customer.models.Order
import com.travelme.customer.utilities.Constants
import kotlinx.android.synthetic.main.fragment_notification.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationFragment : Fragment(R.layout.fragment_notification) {
    private val notificationViewModel : NotificationViewModel by viewModel()

    companion object {
        private const val CHANNEL_NAME = "travelme channel"
        private const val GROUP_KEY_EMAILS = "group_key_emails"
        private const val NOTIFICATION_REQUEST_CODE = 200
        private const val MAX_NOTIFICATION = 2
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_notification.apply {
            adapter = NotificationAdapter(mutableListOf(), requireActivity())
            layoutManager = LinearLayoutManager(requireActivity())
        }

        notificationViewModel.listenToState().observer(viewLifecycleOwner, Observer { handleUI(it) })
        notificationViewModel.listenToOrders().observe(viewLifecycleOwner, Observer { handleOrders(it) })

        //sendNotif()
        //val methodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        //methodManager.hideSoftInputFromWindow()
    }

    private fun handleOrders(it : List<Order>){
        rv_notification.adapter?.let {adapter->
            if (adapter is NotificationAdapter){
                adapter.changelist(it)
            }
        }
    }


    private fun handleUI(it : NotificationState){
        when(it){
            is NotificationState.IsLoading -> {
                if (it.state){
                    pb_notification.visible()
                }else{
                    pb_notification.gone()
                }
            }
            is NotificationState.ShowToast -> toast(it.message)
        }
    }

    private fun toast(message : String) = Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()

    override fun onResume() {
        super.onResume()
        notificationViewModel.driverArrived(Constants.getToken(requireActivity()))
        notificationViewModel.getOrderVerify(Constants.getToken(requireActivity()))
    }
}