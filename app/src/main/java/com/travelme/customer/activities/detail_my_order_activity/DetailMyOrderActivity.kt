package com.travelme.customer.activities.detail_my_order_activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.travelme.customer.R
import com.travelme.customer.models.Order
import com.travelme.customer.utilities.Constants
import kotlinx.android.synthetic.main.activity_detail_my_order.*

class DetailMyOrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_my_order)
        supportActionBar?.hide()

        setData()
    }

    @SuppressLint("SetTextI18n")
    private fun setData(){
        tv_id_order.text = "ID : ${getPassedMyOrder()?.order_id}"
        tv_total_seat.text = "${getPassedMyOrder()?.total_seat} Kursi"
        tv_name_user.text = getPassedMyOrder()?.user?.name
        tv_email_user.text = getPassedMyOrder()?.user?.email
        tv_departure_from.text = getPassedMyOrder()?.departure?.from
        tv_departure_destination.text = getPassedMyOrder()?.departure?.destination
        tv_date.text = getPassedMyOrder()?.date
        tv_hour.text = getPassedMyOrder()?.hour
        tv_price.text = Constants.setToIDR(getPassedMyOrder()?.total_price!!)

        if (getPassedMyOrder()?.verify!!.equals("2")){
            tv_status.text = "DI KONFIRMASI"
        }else{
            tv_status.text = "BELUM DI KONFIRMASI"
        }
    }

    private fun getPassedMyOrder(): Order? = intent.getParcelableExtra("MY_ORDER")
}
