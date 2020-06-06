package com.travelme.customer.activities.order_activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.lifecycle.Observer
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.UIKitCustomSetting
import com.midtrans.sdk.corekit.models.ItemDetails
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import com.travelme.customer.BuildConfig
import com.travelme.customer.R
import com.travelme.customer.models.HourOfDepartureAlternative
import com.travelme.customer.models.User
import com.travelme.customer.utilities.Constants
import kotlinx.android.synthetic.main.activity_order.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.ArrayList

class OrderActivity : AppCompatActivity(), TransactionFinishedCallback {

    private val orderActivityViewModel : OrderActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        //supportActionBar?.hide()


        orderActivityViewModel.listenToUser().observe(this, Observer { setUser(it) })
        orderActivityViewModel.getUserLogin(Constants.getToken(this@OrderActivity))
        orderActivityViewModel.listenToState().observer(this, Observer { handleUI(it) })

        btn_order.setOnClickListener { showPayment() }
        /*tv_pickup_location.setOnClickListener { startActivity(Intent(this@OrderActivity, MapsActivity::class.java)) }
        tv_destination_location.setOnClickListener { startActivity(Intent(this@OrderActivity, MapsActivity::class.java)) }*/
        initPayment()
    }

    @SuppressLint("SetTextI18n")
    private fun setUser(it : User){
        it.let {
            tv_name.setText("Nama Pemesan ${it.name}")
            tv_date.setText("Tanggal ${getPassedHourDeparture()?.dateOfDeparture?.date}")
            txt_hour.text = getPassedHourDeparture()?.hour
            txt_departure.text =
                "${getPassedHourDeparture()?.departure?.from} - ${getPassedHourDeparture()?.departure?.destination}"
            txt_price.text =
                Constants.setToIDR(getPassedHourDeparture()?.departure?.price!!.toInt())
            totalSeat()
        }
    }

    private fun initPayment(){
        SdkUIFlowBuilder.init().apply {
            setClientKey(BuildConfig.CLIENT_KEY)
            setContext(this@OrderActivity)
            enableLog(true)
            setMerchantBaseUrl("${BuildConfig.SNAP_TOKEN}api/snap/")
            setTransactionFinishedCallback(this@OrderActivity)
        }.buildSDK()
    }

    private fun showPayment(){
        val hour_id = getPassedHourDeparture()?.id
        val destination = getPassedHourDeparture()?.departure?.destination!!
        val price = getPassedHourDeparture()?.departure?.price!!
        val total_seat = txt_seat.text.toString().toInt()
        val pickup_location = et_pickup_location.text.toString().trim()
        val destination_location = et_destination_location.text.toString().trim()

        if (orderActivityViewModel.validate(pickup_location, destination_location)){
            val uiKIt = UIKitCustomSetting().apply {
                isSkipCustomerDetailsPages = true
                isShowPaymentStatus = true
            }
            val midtrans = MidtransSDK.getInstance().apply {
                setTransactionRequest(setupTransactionRequest(hour_id.toString(), price, total_seat, destination ))
                uiKitCustomSetting = uiKIt
            }
            midtrans.startPaymentUiFlow(this)
        }
    }

    private fun setupTransactionRequest(id: String, price : Int, qty : Int, name : String) : TransactionRequest{
        val request = TransactionRequest(System.currentTimeMillis().toString(), price.toDouble() * qty)
        val items = ArrayList<ItemDetails>()
        items.add(ItemDetails(id, price.toDouble(), qty, name))
        request.itemDetails = items
        return request
    }

    override fun onTransactionFinished(r: TransactionResult?) {
        r?.let { result ->
            result.response?.let {
                when(result.status){
                    TransactionResult.STATUS_FAILED -> {
                        toast("transaction is failed")
                    }
                    TransactionResult.STATUS_PENDING -> {
                        toast("transaction is pending")
                        val owner_id = getPassedHourDeparture()?.departure?.owner?.id!!
                        val departure_id = getPassedHourDeparture()?.departure?.id!!
                        val date = getPassedHourDeparture()?.dateOfDeparture?.date!!
                        val hour = getPassedHourDeparture()?.hour!!
                        val price = getPassedHourDeparture()?.departure?.price!!
                        val total_seat = txt_seat.text.toString().toInt()
                        val pickup_location = et_pickup_location.text.toString().trim()
                        val destination_location = et_destination_location.text.toString().trim()

                        orderActivityViewModel.storeOrder(
                            Constants.getToken(this@OrderActivity), owner_id, departure_id, date, hour, price, total_seat, pickup_location, destination_location
                        )
                    }
                    TransactionResult.STATUS_SUCCESS -> {
                        toast("transaction is success")
                    }
                    TransactionResult.STATUS_INVALID -> {
                        toast("transaction is invalid")
                    }
                    else -> toast("transaction is invalid")
                }
            }
            if(result.isTransactionCanceled){
                toast("Transaction is cancelled")
            }
            println(result.response.toString())

        } ?: kotlin.run {
            toast("Response is null")
        }

    }


    private fun handleUI(it: OrderActivityState) {
        when (it) {
            //is OrderActivityState.IsLoading -> btn_order.isEnabled = it.state
            is OrderActivityState.ShowToast -> toast(it.message)
            is OrderActivityState.Success -> finish()
            /*is OrderActivityState.Reset -> {
                setPickupLocationError(null)
                setDestinationLocationError(null)
            }
            is OrderActivityState.Validate -> {
                it.pickup_location?.let { setPickupLocationError(it) }
                it.destination_location?.let { setDestinationLocationError(it) }
            }*/
        }
    }

    private fun totalSeat() {
        var count = 1
        val remaining_seat = getPassedHourDeparture()?.remaining_seat

        btn_plus_seat.setOnClickListener {
            if (count >= remaining_seat!!) {
                toast("sisa kursi hanya ${remaining_seat}")
                txt_seat.text = remaining_seat.toString()
                txt_total_seat.text = remaining_seat.toString()
                txt_total_price.text =
                    Constants.setToIDR(getPassedHourDeparture()?.departure?.price.toString().toInt() * remaining_seat)
            } else {
                count++
                txt_seat.text = count.toString()
                txt_total_seat.text = count.toString()
                txt_total_price.text =
                    Constants.setToIDR(getPassedHourDeparture()?.departure?.price.toString().toInt() * count)
            }
        }
        btn_min_seat.setOnClickListener {
            if (count > 1) count-- else count = 1
            txt_seat.text = count.toString()
            txt_total_seat.text = count.toString()
            txt_total_price.text =
                Constants.setToIDR(getPassedHourDeparture()?.departure?.price.toString().toInt() * count)
        }
    }

    private fun toast(message: String) = Toast.makeText(this@OrderActivity, message, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }

    private fun getPassedHourDeparture(): HourOfDepartureAlternative? = intent.getParcelableExtra("DEPARTURE_DETAIL")
}