package com.travelme.customer.fragments.home_fragment.destination_other_fragment

import android.annotation.SuppressLint
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
import com.travelme.customer.activities.hour_activity.HourActivity
import com.travelme.customer.extensions.visible
import com.travelme.customer.models.Owner
import kotlinx.android.synthetic.main.fragment_destination_other.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class DestinationOtherFragment : Fragment(R.layout.fragment_destination_other){
    private val destinationOtherViewModel : DestinationOtherViewModel by viewModel()

    private lateinit var from : String
    private lateinit var destination : String
    private lateinit var domicilies : List<Owner>
    private lateinit var domicile : List<String>


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        destinationOtherViewModel.listenToState().observer(requireActivity(), Observer {handleUI(it)})
        destinationOtherViewModel.listenToOwners().observe(requireActivity(), Observer { handleData(it) })

        setDate()
        btn_search.setOnClickListener {
            val date = txt_date.text.toString().trim()
            if (destinationOtherViewModel.validate(date)){
                startActivity(Intent(requireActivity(), HourActivity::class.java).apply {
                    putExtra("FROM", "Tegal")
                    putExtra("DESTINATION", destination)
                    putExtra("DATE", date)
                })
            }
        }
    }

    private fun handleUI(it : DestinationOtherState){
        when(it){
            is DestinationOtherState.ShowToast -> toast(it.message)
            is DestinationOtherState.IsLoading -> {
                if (it.state){
                    pb_destination_other.visible()
                }else{
                    pb_destination_other.visibility = View.GONE
                }
            }
        }
    }

    private fun handleData(it : List<Owner>){
        domicilies = it.distinctBy { owner -> owner.domicile }
        domicile = domicilies.map { owner -> owner.domicile!! }
        spinnerDestination()
    }

    private fun spinnerDestination(){
        val adapter = ArrayAdapter(requireActivity(), R.layout.item_spinner, domicile).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        sp_destination.adapter = adapter
        sp_destination.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                destination = domicile[position]
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
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun toast(message : String) = Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()

    override fun onResume() {
        super.onResume()
        destinationOtherViewModel.domicile()
    }

}