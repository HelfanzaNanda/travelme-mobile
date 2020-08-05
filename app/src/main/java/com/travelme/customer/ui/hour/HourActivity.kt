package com.travelme.customer.ui.hour

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.travelme.customer.MyOnClickListener
import com.travelme.customer.R
import com.travelme.customer.ui.owner.OwnerActivity
import com.travelme.customer.utilities.extensions.gone
import com.travelme.customer.utilities.extensions.visible
import com.travelme.customer.models.HourOfDeparture
import kotlinx.android.synthetic.main.activity_hour.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HourActivity : AppCompatActivity(), MyOnClickListener {

    private val hourViewModel : HourViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hour)
        supportActionBar?.hide()
        setUpUI()
        observer()
        observe()

    }

    private fun setUpUI() {
        rv_hour.apply {
            adapter = HourAdapter(
                mutableListOf(),
                this@HourActivity
            )
            layoutManager = LinearLayoutManager(this@HourActivity)
        }
    }

    private fun observe() = hourViewModel.listenToHours().observe(this, Observer { handleHours(it) })
    private fun observer() = hourViewModel.listenToState().observer(this, Observer { handleUI(it) })

    private fun handleUI(it : HourState){
        when(it){
            is HourState.ShowToast -> toast(it.message)
            is HourState.IsLoading -> handleLoading(it.state)
        }
    }

    private fun handleLoading(state: Boolean) {
        if (state){
            pb_hour.visible()
        }else{
            pb_hour.gone()
        }
    }

    private fun handleHours(it : List<HourOfDeparture>){
        if (it.isNullOrEmpty()){
            iv_empty_data.visible()
            tv_empty_data.visible()
        }
        rv_hour.adapter?.let {adapter ->
            if (adapter is HourAdapter){
                val distinct = it.distinctBy { hourOfDeparture -> hourOfDeparture.hour }
                adapter.changelist(distinct)
            }
        }
    }

    override fun click(hour: String) {
        startActivity(Intent(this@HourActivity, OwnerActivity::class.java).apply {
            putExtra("FROM", getPassedFrom())
            putExtra("DESTINATION", getPassedDestination())
            putExtra("DATE", getPassedDate())
            putExtra("HOUR", hour)
        })
    }

    private fun toast(message : String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    private fun getPassedFrom() = intent.getStringExtra("FROM")
    private fun getPassedDestination() = intent.getStringExtra("DESTINATION")
    private fun getPassedDate() = intent.getStringExtra("DATE")

    override fun onResume() {
        super.onResume()
        hourViewModel.searchHours(getPassedFrom(), getPassedDestination(), getPassedDate())
    }
}
