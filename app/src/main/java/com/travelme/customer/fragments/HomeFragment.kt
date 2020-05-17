package com.travelme.customer.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.travelme.customer.R
import com.travelme.customer.adapters.DestinationAdapter
import com.travelme.customer.utilities.Constants
import com.travelme.customer.viewmodels.DepartureState
import com.travelme.customer.viewmodels.DepartureViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment(R.layout.fragment_home){

    private lateinit var departureViewModel: DepartureViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rv_home.apply {
            layoutManager = GridLayoutManager(activity, 3)
            adapter = DestinationAdapter(mutableListOf(), activity!!)
        }
        departureViewModel = ViewModelProvider(this).get(DepartureViewModel::class.java)
        departureViewModel.getDepartures().observe(viewLifecycleOwner, Observer {
            view.rv_home.adapter?.let {adapter ->  
                val filtered = it.distinctBy { departure -> departure.destination }
                if (adapter is DestinationAdapter){adapter.changelist(filtered)}
            }
        })
        handleUI()

    }

    private fun handleUI(){
        departureViewModel.getState().observer(this, Observer {
            when(it){
                is DepartureState.IsLoading -> {
                    if (it.state){
                        pb_home.visibility = View.VISIBLE
                        pb_home.isIndeterminate = true
                    }else{
                        pb_home.visibility = View.GONE
                        pb_home.isIndeterminate = false
                    }
                }
                is DepartureState.ShowToast -> toast(it.message)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        departureViewModel.getDestination(Constants.getToken(activity!!))
    }

    private fun toast(message : String) = Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
}