package com.travelme.customer.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import coil.api.load
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import com.travelme.customer.R
import com.travelme.customer.models.Car
import com.travelme.customer.models.Departure
import kotlinx.android.synthetic.main.activity_detail_owner.*
import kotlinx.android.synthetic.main.content_detail_owner.*

class DetailOwnerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_owner)
        setSupportActionBar(toolbar)
        setValue()
        btn_order.setOnClickListener {
            startActivity(Intent(this@DetailOwnerActivity, OrderActivity::class.java))
        }

    }

    private fun setValue(){
        getPassedDeparture()?.let {

            val imageListener: ImageListener = object : ImageListener {
                override fun setImageForPosition(position: Int, imageView: ImageView) {
                    imageView.load("https://travelme-project.herokuapp.com/uploads/owner/car/"
                            +getPassedDeparture()?.owner!!.cars.get(position).photo)
                    //getPassedCar()?.get(position)!!.photo)
                }
            }

            val carouselView = findViewById(R.id.iv_photo_detail_owner) as CarouselView;
            carouselView.setPageCount(it.owner.cars.size);
            carouselView.setImageListener(imageListener);

            supportActionBar?.setTitle(it.owner.business_name)
            txt_destination.setText("${it.from} - ${it.destination}")
            txt_date.setText("Tanggal : ${it.date.date}")
            txt_address.setText("Alamat : ${it.owner.address}")
            txt_telp.setText("Telephone : ${it.owner.telp}")

            txt_hour.setText("Jam : ${it.date.hours.joinToString { hourOfDeparture -> hourOfDeparture.hour.toString() }}")
            txt_remaining_seat.setText("Sisa Kursi : ${it.date.hours.joinToString { hourOfDeparture -> hourOfDeparture.remaining_seat.toString() }}")
        }
    }

    //private fun getPassedCar() = intent.getParcelableArrayListExtra<Car>("CARS")

    private fun getPassedDeparture() : Departure? = intent.getParcelableExtra("DEPARTURE")
}
