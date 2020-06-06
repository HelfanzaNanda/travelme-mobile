package com.travelme.customer.fragments.home_fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.travelme.customer.R
import com.travelme.customer.adapters.DestinationAdapter
import com.travelme.customer.utilities.Constants
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(R.layout.fragment_home){

    private val homeViewModel : HomeViewModel  by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rv_home.apply {
            layoutManager = GridLayoutManager(activity, 3)
            adapter = DestinationAdapter(mutableListOf(), activity!!)
        }

        homeViewModel.listenToDepartures().observe(viewLifecycleOwner, Observer {
            view.rv_home.adapter?.let {adapter ->  
                val filtered = it.distinctBy { departure -> departure.destination }
                if (adapter is DestinationAdapter){adapter.changelist(filtered)}
            }
        })
        homeViewModel.listenToState().observer(viewLifecycleOwner, Observer { handleUI(it) })

    }

    private fun handleUI(it : HomeState){
        when(it){
            is HomeState.IsLoading -> {
                if (it.state){
                    pb_home.visibility = View.VISIBLE
                    pb_home.isIndeterminate = true
                }else{
                    pb_home.visibility = View.GONE
                    pb_home.isIndeterminate = false
                }
            }
            is HomeState.ShowToast -> toast(it.message)
        }
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.getDestination(Constants.getToken(activity!!))
    }

    private fun toast(message : String) = Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
}