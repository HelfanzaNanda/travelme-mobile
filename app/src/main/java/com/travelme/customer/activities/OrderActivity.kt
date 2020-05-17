package com.travelme.customer.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.travelme.customer.R
import com.travelme.customer.models.Departure
import com.travelme.customer.utilities.Constants
import com.travelme.customer.viewmodels.OrderState
import com.travelme.customer.viewmodels.OrderViewModel
import com.travelme.customer.viewmodels.UserState
import com.travelme.customer.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_order.*

class OrderActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var orderViewModel: OrderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        supportActionBar?.hide()

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.getUser().observe(this, Observer {
            it.let {
                et_name.setText(it.name)
                et_name.isEnabled = false
                //et_date.setText(getPassedDeparture()?.date?.date)
                et_date.isEnabled = false
                //txt_hour.text = getPassedDeparture()?.date?.hour?.hour

                txt_departure.text = "${getPassedDeparture()?.from} - ${getPassedDeparture()?.destination}"
                txt_price.text = Constants.setToIDR(getPassedDeparture()?.price!!.toInt())
                //totalSeat()
            }
        })
        userViewModel.getUserIsLogin(Constants.getToken(this@OrderActivity))

        orderViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
        orderViewModel.getState().observer(this, Observer {
            handleUIOrder(it)
        })
        insert()
    }

    private fun insert(){
        btn_order.setOnClickListener {
            val owner_id = getPassedDeparture()?.owner?.id!!
            val departure_id = getPassedDeparture()?.id!!
            //val date = getPassedDeparture()?.date?.date.toString()
            //val hour = getPassedDeparture()?.date?.hour?.hour.toString()
            val price = getPassedDeparture()?.price!!
            val total_seat = txt_seat.text.toString().toInt()
            val pickup_location = et_pickup_location.text.toString().trim()
            val destination_location = et_destination_location.text.toString().trim()
            /*if (orderViewModel.validate(pickup_location, destination_location)){
                orderViewModel.storeOrder(Constants.getToken(this@OrderActivity),
                    owner_id, departure_id, date, hour, price, total_seat, pickup_location, destination_location)
            }*/
        }
    }

    private fun handleUIOrder(it : OrderState){
        when(it){
            is OrderState.IsLoading -> btn_order.isEnabled = it.state
            is OrderState.ShowToast -> toast(it.message)
            is OrderState.Success -> finish()
            is OrderState.Reset -> {
                setPickupLocationError(null)
                setDestinationLocationError(null)
            }
            is OrderState.Validate -> {
                it.pickup_location?.let { setPickupLocationError(it) }
                it.destination_location?.let { setDestinationLocationError(it) }
            }
        }
    }

    private fun setPickupLocationError(err : String?){til_pickup_location.error = err}
    private fun setDestinationLocationError(err : String?){til_destination_location.error = err}


    /*private fun totalSeat() {
        var count = 1
        var remaining_seat = getPassedDeparture()?.date?.hour?.remaining_seat

        btn_plus_seat.setOnClickListener {
            if (count >= remaining_seat!!){
                toast("sisa kursi hanya ${remaining_seat}")
                txt_seat.text = remaining_seat.toString()
                txt_total_seat.text = remaining_seat.toString()
                txt_total_price.text = Constants.setToIDR(getPassedDeparture()?.price.toString().toInt() * remaining_seat)
            }else{
                count++
                txt_seat.text = count.toString()
                txt_total_seat.text = count.toString()
                txt_total_price.text = Constants.setToIDR(getPassedDeparture()?.price.toString().toInt() * count)
            }
        }
        btn_min_seat.setOnClickListener {
            if (count > 1) count-- else count = 1
            txt_seat.text = count.toString()
            txt_total_seat.text = count.toString()
            txt_total_price.text = Constants.setToIDR(getPassedDeparture()?.price.toString().toInt() * count)
        }
    }*/

    private fun toast (message : String) = Toast.makeText(this@OrderActivity, message, Toast.LENGTH_SHORT).show()

    private fun getPassedDeparture() : Departure? = intent.getParcelableExtra("DEPARTURE_DETAIL")
}
