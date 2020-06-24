package com.travelme.customer.activities.detail_owner_activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import coil.api.load
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import com.travelme.customer.R
import com.travelme.customer.activities.login_activity.LoginActivity
import kotlinx.android.synthetic.main.activity_detail_owner.*
import kotlinx.android.synthetic.main.content_detail_owner.*
import com.travelme.customer.activities.order_activity.OrderActivity
import com.travelme.customer.models.HourOfDeparture
import com.travelme.customer.utilities.Constants

class DetailOwnerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_owner)
        setSupportActionBar(toolbar)
        setValue()

        btn_order.setOnClickListener {
            if (isLoggedIn()){
                startActivity(Intent(this@DetailOwnerActivity, OrderActivity::class.java).apply {
                    putExtra("DEPARTURE_DETAIL", getPassedHourDeparture())
                })
            }else{
                popupIsLogin("anda belum login, untuk melanjutkan anda harus login dahulu!")
            }
        }
    }

    private fun popupIsLogin(m: String){
        AlertDialog.Builder(this).apply {
            setMessage(m)
            setPositiveButton("login"){dialog, _ ->
                startActivity(Intent(this@DetailOwnerActivity, LoginActivity::class.java).putExtra("EXPECT_RESULT", true))
                dialog.dismiss()
            }
        }.show()
    }

    private fun isLoggedIn() = Constants.getToken(this@DetailOwnerActivity) != "UNDEFINED"

    @SuppressLint("SetTextI18n")
    private fun setValue(){

        getPassedHourDeparture()?.let {
            val imageListener =
                ImageListener { position, imageView ->
                    imageView.load(getPassedHourDeparture()?.date!!.departure.owner.cars[position].photo) }

            val carouselView = findViewById<CarouselView>(R.id.iv_photo_detail_owner)
            carouselView.pageCount = it.date.departure.owner.cars.size
            carouselView.setImageListener(imageListener)

            supportActionBar?.title = it.date.departure.owner.business_name
            txt_destination.text = "${it.date.departure.from} - ${it.date.departure.destination}"


            txt_address.text = "Alamat : ${it.date.departure.owner.address}"
            txt_telp.text = "Telephone : ${it.date.departure.owner.telp}"
            txt_facility.text = "Fasilitas : ${it.date.departure.owner.cars.joinToString { car -> car.facility.toString() }}"


            txt_date.text = "Tanggal : ${it.date.date}"
            txt_hour.text = "Jam : ${it.hour}"
            txt_remaining_seat.text = "Sisa Kursi : ${it.remaining_seat}"

        }
    }

    //private fun getPassedDeparture() : Departure? = intent.getParcelableExtra("DEPARTURE")
    private fun getPassedHourDeparture() : HourOfDeparture? = intent.getParcelableExtra("DEPARTURE")
}
