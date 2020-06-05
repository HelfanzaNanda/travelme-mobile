package com.travelme.customer.activities

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.travelme.customer.R
import com.travelme.customer.adapters.DepartureAdapter
import com.travelme.customer.models.*
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
        departureViewModel.getDepartureByDest(Constants.getToken(this@DepartureByDestActivity), getPassedDestination())
        departureViewModel.getDepartures().observe(this@DepartureByDestActivity, Observer {
            handleData(it)
        })
        setDate()
        reset()
        handleUI()
        search()
        setSpinner()
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
            departureViewModel.searchDeparture(Constants.getToken(this@DepartureByDestActivity), getPassedDestination(), date)
        }
    }

    private fun reset(){
        btn_reset.setOnClickListener {
            txt_date.text = ""
            departureViewModel.getDepartureByDest(Constants.getToken(this@DepartureByDestActivity), getPassedDestination())
        }
    }

    private fun setSpinner(){
        val prices = resources.getStringArray(R.array.prices)
        if (spinner_sort_price != null){
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, prices).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            spinner_sort_price.adapter = adapter

            spinner_sort_price.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    departureViewModel.getDepartures().observe(this@DepartureByDestActivity, Observer {
                        rv_car_by_destination.adapter?.let {adapter->
                            val sortByPrice = it.sortedBy { departure -> departure.price  }
                            val restructured = restructureData(sortByPrice)
                            if (adapter is DepartureAdapter){
                                adapter.changelist(restructured)
                            }
                        }
                    })
                }

            }
        }
    }

    private fun handleData(data : List<Departure>){
        rv_car_by_destination.adapter?.let { adapter ->
            if (adapter is DepartureAdapter){
                val restructured = restructureData(data)
                adapter.changelist(restructured)
                if(restructured.isNullOrEmpty()){
                    iv_no_data.visibility = View.VISIBLE
                    tv_no_data.text = "tidak ada travel pada tanggal ${txt_date.text}"
                    tv_no_data.visibility = View.VISIBLE
                }else{
                    iv_no_data.visibility = View.GONE
                    tv_no_data.visibility = View.GONE
                }
            }
        }
    }

    private fun restructureData(d : List<Departure>): MutableList<HourOfDepartureAlternative> {
        val hoursAlt = mutableListOf<HourOfDepartureAlternative>()
        val departureWithDates = d.map { Temp(it.dates, it) }
        departureWithDates.map {
            if(!it.dates.isNullOrEmpty()){
                it.dates!!.map { date ->
                    for (i in date.hours){
                        hoursAlt.add(
                            HourOfDepartureAlternative(
                                id = i.id,
                                dateOfDeparture = date,
                                hour = i.hour,
                                seat = i.seat,
                                remaining_seat = i.remaining_seat,
                                departure = it.departure
                            ))
                    }
                }
            }
        }
        return hoursAlt
    }

    private fun getPassedDestination() = intent.getStringExtra("DESTINATION")

    private fun toast(message : String) = Toast.makeText(this@DepartureByDestActivity, message, Toast.LENGTH_SHORT).show()
}

data class Temp(
    var dates : List<DateOfDeparture>? = mutableListOf(),
    var departure : Departure? = null
)