package com.travelme.customer.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import coil.api.load
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import com.travelme.customer.R
import kotlinx.android.synthetic.main.activity_detail_owner.*
import kotlinx.android.synthetic.main.content_detail_owner.*
import com.travelme.customer.activities.order_activity.OrderActivity
import com.travelme.customer.models.HourOfDepartureAlternative

class DetailOwnerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_owner)
        setSupportActionBar(toolbar)
        setValue()

        btn_order.setOnClickListener {
            startActivity(Intent(this@DetailOwnerActivity, OrderActivity::class.java).apply {
                putExtra("DEPARTURE_DETAIL", getPassedHourDeparture())
            })
        }

    }

    private fun setValue(){

        getPassedHourDeparture()?.let {
            val imageListener: ImageListener = object : ImageListener {
                override fun setImageForPosition(position: Int, imageView: ImageView) {
                    imageView.load("https://travelme-project.herokuapp.com/uploads/owner/car/"
                            +getPassedHourDeparture()?.departure!!.owner.cars.get(position).photo)
                }
            }

            val carouselView = findViewById(R.id.iv_photo_detail_owner) as CarouselView;
            carouselView.setPageCount(it.departure!!.owner.cars.size);
            carouselView.setImageListener(imageListener);

            supportActionBar?.setTitle(it.departure!!.owner.business_name)
            txt_destination.setText("${it.departure!!.from} - ${it.departure!!.destination}")

            txt_address.text = "Alamat : ${it.departure!!.owner.address}"
            txt_telp.text = "Telephone : ${it.departure!!.owner.telp}"
            txt_facility.text = "Fasilitas : ${it.departure!!.owner.cars.joinToString { car -> car.facility.toString() }}"


            txt_date.text = "Tanggal : ${it.dateOfDeparture!!.date}"
            txt_hour.text = "Jam : ${it.hour}"
            txt_remaining_seat.text = "Sisa Kursi : ${it.remaining_seat}"

        }
    }

    //private fun getPassedDeparture() : Departure? = intent.getParcelableExtra("DEPARTURE")
    private fun getPassedHourDeparture() : HourOfDepartureAlternative? = intent.getParcelableExtra("DEPARTURE")
}
