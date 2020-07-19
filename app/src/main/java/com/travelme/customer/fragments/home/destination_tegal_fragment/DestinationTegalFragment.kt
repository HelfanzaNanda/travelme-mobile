package com.travelme.customer.fragments.home.destination_tegal_fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.travelme.customer.R
import com.travelme.customer.activities.hour.HourActivity
import com.travelme.customer.extensions.gone
import com.travelme.customer.extensions.visible
import com.travelme.customer.models.Destination
import com.travelme.customer.models.Owner
import kotlinx.android.synthetic.main.fragment_destination_tegal.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class DestinationTegalFragment : Fragment(R.layout.fragment_destination_tegal){
    private val destinationTegalViewModel : DestinationTegalViewModel by viewModel()

    private lateinit var from : String
    private lateinit var destinations : List<Destination>
    private lateinit var domicile : List<String>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        destinationTegalViewModel.listenToState().observer(requireActivity(), Observer {handleUI(it)})
        destinationTegalViewModel.listenToDestinations().observe(requireActivity(), Observer { handleData(it) })

        setDate()
        btn_search.setOnClickListener {
            val date = txt_date.text.toString().trim()
            if (destinationTegalViewModel.validate(date)){
                startActivity(Intent(requireActivity(), HourActivity::class.java).apply {
                    putExtra("FROM", from)
                    putExtra("DESTINATION", "Tegal")
                    putExtra("DATE", date)
                })
            }
        }
    }

    private fun handleUI(it : DestinationTegalState){
        when(it){
            is DestinationTegalState.ShowToast -> toast(it.message)
            is DestinationTegalState.IsLoading -> {
                if (it.state){
                    pb_destination_tegal.visible()
                }else{
                    pb_destination_tegal.gone()
                }
            }
        }
    }

    private fun handleData(it : List<Destination>){
        destinations = it.distinctBy { destination -> destination.destination }
        domicile = destinations.map { destination -> destination.destination!! }
        spinnerFrom()
    }

    private fun spinnerFrom(){
        val adapter = ArrayAdapter(requireActivity(), R.layout.item_spinner, domicile).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        sp_from.adapter = adapter
        sp_from.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                from = domicile[position]
            }

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
        label_date.setOnClickListener {
            DatePickerDialog(requireActivity(), dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).apply {
                datePicker.minDate = cal.timeInMillis
            }.show()
        }
    }

    private fun toast(message : String) = Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()

    override fun onResume() {
        super.onResume()
        destinationTegalViewModel.domicile()
    }

}