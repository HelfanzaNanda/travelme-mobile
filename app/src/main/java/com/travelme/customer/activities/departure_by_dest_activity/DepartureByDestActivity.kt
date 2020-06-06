package com.travelme.customer.activities.departure_by_dest_activity

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.travelme.customer.R
import com.travelme.customer.adapters.DepartureAdapter
import com.travelme.customer.models.*
import com.travelme.customer.utilities.Constants
import kotlinx.android.synthetic.main.activity_departure_by_dest.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class DepartureByDestActivity : AppCompatActivity() {

    private val departureViewModel: DepartureByDestViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_departure_by_dest)
        supportActionBar?.hide()
        rv_car_by_destination.apply {
            layoutManager = LinearLayoutManager(this@DepartureByDestActivity)
            adapter = DepartureAdapter(mutableListOf(), this@DepartureByDestActivity)
        }

        departureViewModel.getDepartureByDest(Constants.getToken(this@DepartureByDestActivity), getPassedDestination())
        search()
        departureViewModel.listenToDepartures().observe(this@DepartureByDestActivity, Observer { handleData(it) })
        departureViewModel.listenToState().observer(this, Observer { handleUI(it) })
        setDate()
        reset()
        initEmptyView()
        setSpinner()
    }


    private fun handleUI(it : DepartureByDestState){
        when(it){
            is DepartureByDestState.IsLoading -> {
                if (it.state){
                    iv_empty_data.visibility = View.GONE
                    tv_empty_data.visibility = View.GONE
                    pb_departure_by_destination.visibility = View.VISIBLE
                    pb_departure_by_destination.isIndeterminate = true
                }else{
                    pb_departure_by_destination.visibility = View.GONE
                    pb_departure_by_destination.isIndeterminate = false
                }
            }
            is DepartureByDestState.ShowToast -> toast(it.message)
        }
    }

    private fun setDate(){
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
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

    private fun initEmptyView(){
        if (departureViewModel.listenToDepartures().value == null || departureViewModel.listenToDepartures().value!!.isEmpty()){
            iv_empty_data.visibility = View.VISIBLE
            tv_empty_data.visibility = View.VISIBLE
        }else{
            iv_empty_data.visibility = View.GONE
            tv_empty_data.visibility = View.GONE
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
                    departureViewModel.listenToDepartures().observe(this@DepartureByDestActivity, Observer {
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
            }
        }
    }

    private fun restructureData(d : List<Departure>): MutableList<HourOfDepartureAlternative> {
        val hoursAlt = mutableListOf<HourOfDepartureAlternative>()
        val departureWithDates = d.map {
            Temp(
                it.dates,
                it
            )
        }
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
        if (hoursAlt.isNullOrEmpty()){
            iv_empty_data.visibility = View.VISIBLE
            tv_empty_data.visibility = View.VISIBLE
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