package com.travelme.customer.ui.order

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.travelme.customer.R
import com.travelme.customer.ui.main.MainActivity
import com.travelme.customer.ui.maps.MapsActivity
import com.travelme.customer.ui.maps.ResultMaps
import com.travelme.customer.utilities.extensions.notfocusable
import com.travelme.customer.models.HourOfDeparture
import com.travelme.customer.models.User
import com.travelme.customer.ui.seat.SeatActivity
import com.travelme.customer.utilities.Constants
import kotlinx.android.synthetic.main.activity_order.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrderActivity : AppCompatActivity() {

    private val orderActivityViewModel : OrderActivityViewModel by viewModel()
    private val REQUEST_CODE_PICKUP_LOCATION = 101
    private val REQUEST_CODE_DESTINATION_LOCATION = 202
    private var pickupPoint : String? = null
    private var latPickupLocation : String? = null
    private var lngPickupLocation : String? = null
    private var destinationPoint : String? = null
    private var latDestinationLocation : String? = null
    private var lngDestinationLocation : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        //supportActionBar?.hide()
        observe()
        btn_order.setOnClickListener { order() }
        //pickupLocationOnClick()
        //destinationLocatinOnClick()
        fetchUserWhoLoggedIn()
        checkSeat()
    }

    private fun pickupLocationOnClick(){
        et_pickup_location.notfocusable()
        et_pickup_location.setOnClickListener {
            startActivityForResult(Intent(this@OrderActivity, MapsActivity::class.java), REQUEST_CODE_PICKUP_LOCATION)
        }
    }

    private fun destinationLocatinOnClick(){
        et_destination_location.notfocusable()
        et_destination_location.setOnClickListener {
            startActivityForResult(Intent(this@OrderActivity, MapsActivity::class.java), REQUEST_CODE_DESTINATION_LOCATION)
        }
    }

    private fun observe() {
        observeState()
        observeUser()
    }


    private fun observeUser() = orderActivityViewModel.listenToUser().observe(this, Observer { setUser(it) })
    private fun observeState() = orderActivityViewModel.listenToState().observer(this, Observer { handleUI(it) })
    private fun fetchUserWhoLoggedIn() = orderActivityViewModel.getUserLogin(Constants.getToken(this@OrderActivity))

    @SuppressLint("SetTextI18n")
    private fun setUser(it : User){
        it.let {
            tv_name.text = "Nama Pemesan : ${it.name}"
            tv_date.text = "Tanggal : ${getPassedHourDeparture()?.date!!.date}"
            txt_hour.text = getPassedHourDeparture()?.hour
            txt_departure.text = "${getPassedHourDeparture()?.date!!.departure.from} -> ${getPassedHourDeparture()?.date!!.departure.destination}"
            txt_price.text = Constants.setToIDR(getPassedHourDeparture()?.date!!.departure.price!!.toInt())
            //totalSeat()
        }
    }

    private fun order(){
        val token = Constants.getToken(this@OrderActivity)
        val ownerId = getPassedHourDeparture()?.date!!.departure.owner.id!!
        val departureId = getPassedHourDeparture()?.date!!.departure.id!!
        val date = getPassedHourDeparture()?.date!!.date!!
        val hour = getPassedHourDeparture()?.hour!!
        val price = getPassedHourDeparture()?.date!!.departure.price!!
        //val totalSeat = txt_seat.text.toString().toInt()
        val pickupPoint = et_pickup_location.text.toString().trim()
        val destinationPoint = et_destination_location.text.toString().trim()
        if (orderActivityViewModel.validate(pickupPoint, destinationPoint)){
//            orderActivityViewModel.storeOrder(token, ownerId, departureId, date, hour, price, totalSeat,
//                pickupPoint, latPickupLocation!!, lngPickupLocation!!, destinationPoint,
//                latDestinationLocation!!, lngDestinationLocation!!)
        }
    }

    private fun handleUI(it: OrderActivityState) {
        when (it) {
            is OrderActivityState.ShowToast -> toast(it.message)
            is OrderActivityState.Alert -> popup("terima kasih telah memesan travel ini lanjutkan pembayaran jika sudah di konfirmasi")
            is OrderActivityState.Reset -> {
                setPickupPointError(null)
                setDestinationPointError(null)
            }
            is OrderActivityState.Validate -> {
                it.pickup_location?.let { setPickupPointError(it) }
                it.destination_location?.let { setDestinationPointError(it) }
            }
        }
    }

    private fun popup(message: String){
        AlertDialog.Builder(this).apply {
            setMessage(message)
            setPositiveButton("paham") { dialog, _ ->
                dialog.dismiss()
                startActivity(Intent(this@OrderActivity, MainActivity::class.java))
            }
        }.show()
    }

//    private fun totalSeat() {
//        var count = 1
//        val remaining_seat = getPassedHourDeparture()?.remaining_seat
//
//        btn_plus_seat.setOnClickListener {
//            if (count >= remaining_seat!!) {
//                toast("sisa kursi hanya $remaining_seat")
//                txt_seat.text = remaining_seat.toString()
//                txt_total_seat.text = remaining_seat.toString()
//                txt_total_price.text =
//                    Constants.setToIDR(getPassedHourDeparture()?.date!!.departure.price.toString().toInt() * remaining_seat)
//            } else {
//                count++
//                txt_seat.text = count.toString()
//                txt_total_seat.text = count.toString()
//                txt_total_price.text =
//                    Constants.setToIDR(getPassedHourDeparture()?.date!!.departure.price.toString().toInt() * count)
//            }
//        }
//        btn_min_seat.setOnClickListener {
//            if (count > 1) count-- else count = 1
//            txt_seat.text = count.toString()
//            txt_total_seat.text = count.toString()
//            txt_total_price.text =
//                Constants.setToIDR(getPassedHourDeparture()?.date!!.departure.price.toString().toInt() * count)
//        }
//    }

    private fun toast(message: String) = Toast.makeText(this@OrderActivity, message, Toast.LENGTH_SHORT).apply {
        setGravity(Gravity.CENTER, 0, 0)
        show()
    }

    private fun setPickupPointError(err : String?){ tip_pickup_location.error = err }
    private fun setDestinationPointError(err : String?){ tip_destination_location.error = err }

    private fun onPickupLocationOnReturned(data : Intent?){
        data?.let {
            et_pickup_location.setText(getPassedResultMaps(it)!!.address)
            pickupPoint = getPassedResultMaps(it)!!.address
            latPickupLocation = getPassedResultMaps(it)!!.lat
            lngPickupLocation = getPassedResultMaps(it)!!.lng
        }
    }

    private fun onDestinationOnReturned(data: Intent?){
        data?.let {
            et_destination_location.setText(getPassedResultMaps(it)!!.address)
            destinationPoint = getPassedResultMaps(it)!!.address
            latDestinationLocation = getPassedResultMaps(data)!!.lat
            lngDestinationLocation = getPassedResultMaps(data)!!.lng
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PICKUP_LOCATION && data != null){
            onPickupLocationOnReturned(data)
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_DESTINATION_LOCATION && data != null){
            onDestinationOnReturned(data)
        }
    }


    private fun checkSeat() {
        btn_choose_seat.setOnClickListener {
            startActivity(Intent(this, SeatActivity::class.java).apply {
                putExtra("DEPARTURE_DETAIL", getPassedHourDeparture())
            })
        }
    }

    private fun getPassedHourDeparture(): HourOfDeparture? = intent.getParcelableExtra("DEPARTURE_DETAIL")
    private fun getPassedResultMaps(data: Intent) : ResultMaps? = data.getParcelableExtra("RESULT_MAPS")
}
