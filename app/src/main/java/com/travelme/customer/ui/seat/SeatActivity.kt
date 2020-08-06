package com.travelme.customer.ui.seat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.travelme.customer.R
import com.travelme.customer.models.HourOfDeparture
import com.travelme.customer.models.Seat
import com.travelme.customer.utilities.extensions.gone
import com.travelme.customer.utilities.extensions.visible
import kotlinx.android.synthetic.main.activity_seat.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SeatActivity : AppCompatActivity() {

    private val seatViewModel : SeatViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seat)

        setUpUI()
        observer()
        observe()
        onFinishSelectSeat()
    }

    private fun onFinishSelectSeat() {
        btn_ok.setOnClickListener { _->
            val seats = seatViewModel.listenToSeats().value
            val selectedSeats : MutableList<Seat> = mutableListOf()
            seats!!.map {
                if (it.selected){
                    it.price = getPassedHourDeparture()!!.date.departure.price
                    selectedSeats.add(it)
                }
            }
            val i = Intent()
            i.putParcelableArrayListExtra("selected_seats", selectedSeats as ArrayList<out Parcelable>)
            setResult(Activity.RESULT_OK, i)
            finish()
        }
    }

    private fun observer() = seatViewModel.listenToState().observer(this, Observer { handleUiState(it) })
    private fun observe() = seatViewModel.listenToSeats().observe(this, Observer { handleSeats(it) })

    private fun handleSeats(it: List<Seat>) {
        recycler_view.adapter?.let { adapter ->
            if (adapter is SeatAdapter){
                adapter.changelist(it)
            }
        }
    }

    private fun handleUiState(it: SeatState) {
        when(it){
            is SeatState.Loading -> handleLoading(it.state)
            is SeatState.ShowToast -> toast(it.message)
        }
    }

    private fun handleLoading(state: Boolean) {
        if (state){
            loading.visible()
        }else{
            loading.gone()
        }
    }

    private fun setUpUI() {
        recycler_view.apply {
            adapter = SeatAdapter(mutableListOf(), this@SeatActivity, seatViewModel)
            layoutManager = GridLayoutManager(this@SeatActivity, 3)
        }
    }

    private fun getPassedHourDeparture(): HourOfDeparture? = intent.getParcelableExtra("DEPARTURE_DETAIL")
    private fun toast(message : String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    override fun onResume() {
        super.onResume()
        val carId = getPassedHourDeparture()?.car!!.id.toString()
        val date = getPassedHourDeparture()?.date!!.date!!
        val hour = getPassedHourDeparture()?.hour!!
        seatViewModel.checkSeat(carId, date, hour)
    }
}