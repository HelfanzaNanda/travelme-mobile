package com.travelme.customer.activities.owner_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.travelme.customer.R
import com.travelme.customer.adapters.OwnerAdapter
import com.travelme.customer.extensions.gone
import com.travelme.customer.extensions.visible
import com.travelme.customer.models.Departure
import com.travelme.customer.models.HourOfDeparture
import kotlinx.android.synthetic.main.activity_owner.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class OwnerActivity : AppCompatActivity() {

    private val ownerViewModel : OwnerViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner)

        rv_owner.apply {
            adapter = OwnerAdapter(mutableListOf(), this@OwnerActivity)
            layoutManager = LinearLayoutManager(this@OwnerActivity)
        }

        ownerViewModel.listenToState().observer(this, Observer { handleUI(it) })
        ownerViewModel.listenToOwners().observe(this, Observer { handleDepartures(it) })
    }

    private fun handleUI(it : OwnerState){
        when(it){
            is OwnerState.ShowToast -> toast(it.message)
            is OwnerState.IsLoading -> {
                if (it.state){
                    pb_owner.visible()
                }else{
                    pb_owner.gone()
                }
            }
        }
    }

    private fun handleDepartures(it : List<HourOfDeparture>){
        rv_owner.adapter?.let {adapter ->
            if (adapter is OwnerAdapter){
                adapter.changelist(it)
            }
        }
    }

    private fun toast(message : String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()


    private fun getPassedFrom() = intent.getStringExtra("FROM")
    private fun getPassedDestination() = intent.getStringExtra("DESTINATION")
    private fun getPassedDate() = intent.getStringExtra("DATE")
    private fun getPassedHour() = intent.getStringExtra("HOUR")

    override fun onResume() {
        super.onResume()
        ownerViewModel.getOwners(getPassedFrom(), getPassedDestination(), getPassedDate(), getPassedHour())

    }
}
