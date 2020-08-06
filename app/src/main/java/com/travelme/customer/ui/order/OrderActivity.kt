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
import com.travelme.customer.models.CreateOrder
import com.travelme.customer.ui.main.MainActivity
import com.travelme.customer.ui.maps.MapsActivity
import com.travelme.customer.ui.maps.ResultMaps
import com.travelme.customer.utilities.extensions.notfocusable
import com.travelme.customer.models.HourOfDeparture
import com.travelme.customer.models.Seat
import com.travelme.customer.models.User
import com.travelme.customer.ui.seat.SeatActivity
import com.travelme.customer.utilities.Constants
import com.travelme.customer.utilities.extensions.gone
import com.travelme.customer.utilities.extensions.visible
import kotlinx.android.synthetic.main.activity_order.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrderActivity : AppCompatActivity() {

    private val REQUEST_CODE_PICKUP_LOCATION = 101
    private val REQUEST_CODE_DESTINATION_LOCATION = 202
    private var pickupPoint : String? = null
    private var latPickupLocation : String? = null
    private var lngPickupLocation : String? = null
    private var destinationPoint : String? = null
    private var latDestinationLocation : String? = null
    private var lngDestinationLocation : String? = null

    private val orderActivityViewModel : OrderActivityViewModel by viewModel()
    companion object{ const val REQ_SELECT_SEAT = 101 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        observe()
        fetchUserWhoLoggedIn()
        selectSeatForResult()
        checkedPayment()
        order()
    }

    private fun observe() {
        observeState()
        observeUser()
    }

    private fun observeUser() = orderActivityViewModel.listenToUser().observe(this, Observer { setUser(it) })
    private fun observeState() = orderActivityViewModel.listenToState().observer(this, Observer { handleUI(it) })
    private fun selectPayment() = orderActivityViewModel.listenToSelectPayment().value?.payment
    private fun fetchUserWhoLoggedIn() = orderActivityViewModel.getUserLogin(Constants.getToken(this@OrderActivity))
    private fun resultSelectSeat() = orderActivityViewModel.listenToResultSelectSeat().value

    @SuppressLint("SetTextI18n")
    private fun setUser(it : User){
        it.let {
            val price = getPassedHourDeparture()?.date!!.departure.price
            tv_name.text = "Nama Pemesan : ${it.name}"
            tv_date.text = "Tanggal : ${getPassedHourDeparture()?.date!!.date}"
            txt_hour.text = "${getPassedHourDeparture()?.hour} WIB"
            txt_departure.text = "${getPassedHourDeparture()?.date!!.departure.from} -> ${getPassedHourDeparture()?.date!!.departure.destination}"
            txt_price.text = Constants.setToIDR(price!!)
        }
    }

    private fun order(){
        btn_order.setOnClickListener {
            val token = Constants.getToken(this@OrderActivity)
            val ownerId = getPassedHourDeparture()?.date!!.departure.owner.id!!
            val departureId = getPassedHourDeparture()?.date!!.departure.id!!
            val carId = getPassedHourDeparture()?.car!!.id
            val date = getPassedHourDeparture()?.date!!.date!!
            val hour = getPassedHourDeparture()?.hour!!
            val pickupPoint = et_pickup_location.text.toString().trim()
            val destinationPoint = et_destination_location.text.toString().trim()
            if (orderActivityViewModel.validate(pickupPoint, destinationPoint, resultSelectSeat(), selectPayment())){
                val createOrder = CreateOrder(owner_id = ownerId, departure_id = departureId, car_id = carId,
                    date = date, hour = hour, pickup_point = pickupPoint,
                    destination_point = destinationPoint, payment = selectPayment(), seats = resultSelectSeat()!!)
                orderActivityViewModel.storeOrder(token, createOrder)
            }
        }
    }

    private fun checkedPayment(){
        rbgrup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.payment_offline -> orderActivityViewModel.selectPayment(false)
                R.id.payment_online -> orderActivityViewModel.selectPayment(true)
            }
        }
    }

    private fun handleUI(it: OrderActivityState) {
        when (it) {
            is OrderActivityState.IsLoading -> handleLoading(it.state)
            is OrderActivityState.ShowToast -> toast(it.message)
            is OrderActivityState.Alert -> popup("terima kasih telah memesan travel ini lanjutkan pembayaran jika sudah di konfirmasi")
            is OrderActivityState.Reset -> handleReset()
            is OrderActivityState.Validate -> handleValidate(it)
        }
    }

    private fun handleLoading(state: Boolean) {
        if (state){
            loading.visible()
        }else{
            loading.gone()
        }
        btn_order.isEnabled = !state
    }

    private fun handleReset() {
        setPickupPointError(null)
        setDestinationPointError(null)
    }

    private fun handleValidate(validate: OrderActivityState.Validate) {
        validate.pickup_location?.let { setPickupPointError(it) }
        validate.destination_location?.let { setDestinationPointError(it) }
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

    private fun selectSeatForResult() {
        btn_choose_seat.setOnClickListener {
            startActivityForResult(Intent(this, SeatActivity::class.java).apply {
                putExtra("DEPARTURE_DETAIL", getPassedHourDeparture())
            }, REQ_SELECT_SEAT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQ_SELECT_SEAT){
            val seats = data?.getParcelableArrayListExtra<Seat>("selected_seats")!!
            orderActivityViewModel.resultSelectSeat(seats)
            val price = getPassedHourDeparture()?.date!!.departure.price!!
            txt_total_seat.text = seats.size.toString()
            txt_total_price.text = Constants.setToIDR(price.times(seats.size))
        }
    }

    private fun getPassedHourDeparture(): HourOfDeparture? = intent.getParcelableExtra("DEPARTURE_DETAIL")
    private fun getPassedResultMaps(data: Intent) : ResultMaps? = data.getParcelableExtra("RESULT_MAPS")
    private fun setPickupPointError(err : String?){ tip_pickup_location.error = err }
    private fun setDestinationPointError(err : String?){ tip_destination_location.error = err }

    private fun toast(message: String) = Toast.makeText(this@OrderActivity, message, Toast.LENGTH_SHORT).apply {
        setGravity(Gravity.CENTER, 0, 0)
        show()
    }

    override fun onResume() {
        super.onResume()
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

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PICKUP_LOCATION && data != null){
//            onPickupLocationOnReturned(data)
//        }
//        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_DESTINATION_LOCATION && data != null){
//            onDestinationOnReturned(data)
//        }
//    }

}
