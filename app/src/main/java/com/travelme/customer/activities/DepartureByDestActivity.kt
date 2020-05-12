package com.travelme.customer.activities

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.travelme.customer.R
import com.travelme.customer.adapters.DepartureAdapter
import com.travelme.customer.utilities.Constants
import com.travelme.customer.viewmodels.DepartureState
import com.travelme.customer.viewmodels.DepartureViewModel
import kotlinx.android.synthetic.main.activity_departure_by_dest.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import java.util.*

class DepartureByDestActivity : AppCompatActivity() {

    private lateinit var departureViewModel: DepartureViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_departure_by_dest)
        supportActionBar?.hide()
        rv_car_by_destination.apply {
            layoutManager = LinearLayoutManager(this@DepartureByDestActivity)
            adapter = DepartureAdapter(mutableListOf(), this@DepartureByDestActivity)
        }
        departureViewModel = ViewModelProvider(this).get(DepartureViewModel::class.java)
        departureViewModel.getDepartureByDest(Constants.token, getPassedDestination())
        search()
        departureViewModel.getDepartures().observe(this@DepartureByDestActivity, Observer {
            rv_car_by_destination.adapter?.let { adapter ->
                if (adapter is DepartureAdapter){
                    adapter.changelist(it)
                    for (x in it){
                        x.owner.business_name
                    }
                }
            }
        })
        setDate()
        reset()
        handleUI()
    }

    private fun handleUI(){
        departureViewModel.getState().observer(this, Observer {
            when(it){
                is DepartureState.IsLoading -> {
                    if (it.state){
                        pb_departure_by_destination.visibility = View.VISIBLE
                        pb_departure_by_destination.isIndeterminate = true
                    }else{
                        pb_departure_by_destination.visibility = View.GONE
                        pb_departure_by_destination.isIndeterminate = false
                    }
                }
                is DepartureState.ShowToast -> toast(it.message)
            }
        })
    }

    private fun setDate(){
        var cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "yyyy-MM-dd"
            val simpleDateFormat = SimpleDateFormat(myFormat, Locale.US)
            txt_date.text = simpleDateFormat.format(cal.time)
        }
        txt_date.setOnClickListener {
            DatePickerDialog(this@DepartureByDestActivity, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun search(){
        btn_search.setOnClickListener {
            val date = txt_date.text.toString()
            departureViewModel.searchDeparture(Constants.token, getPassedDestination(), date)
        }
    }

    private fun reset(){
        btn_reset.setOnClickListener {
            txt_date.text = ""
            departureViewModel.getDepartureByDest(Constants.token, getPassedDestination())
        }
    }

    private fun getPassedDestination() = intent.getStringExtra("DESTINATION")

    private fun toast(message : String) = Toast.makeText(this@DepartureByDestActivity, message, Toast.LENGTH_SHORT).show()
}
