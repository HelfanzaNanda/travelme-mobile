package com.travelme.customer.activities

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import coil.api.load
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import com.travelme.customer.R
import com.travelme.customer.models.Car
import com.travelme.customer.models.Departure
import kotlinx.android.synthetic.main.activity_detail_owner.*
import kotlinx.android.synthetic.main.content_detail_owner.*
import android.widget.LinearLayout.LayoutParams
import android.widget.LinearLayout.LayoutParams.MATCH_PARENT

class DetailOwnerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_owner)
        setSupportActionBar(toolbar)
        setValue()

        btn_order.setOnClickListener {
            startActivity(Intent(this@DetailOwnerActivity, OrderActivity::class.java).apply {
                putExtra("DEPARTURE_DETAIL", getPassedDeparture())
            })
        }

    }

    private fun setValue(){
        getPassedDeparture()?.let {

            val imageListener: ImageListener = object : ImageListener {
                override fun setImageForPosition(position: Int, imageView: ImageView) {
                    imageView.load("https://travelme-project.herokuapp.com/uploads/owner/car/"
                            +getPassedDeparture()?.owner!!.cars.get(position).photo)
                }
            }

            val carouselView = findViewById(R.id.iv_photo_detail_owner) as CarouselView;
            carouselView.setPageCount(it.owner.cars.size);
            carouselView.setImageListener(imageListener);

            supportActionBar?.setTitle(it.owner.business_name)
            txt_destination.setText("${it.from} - ${it.destination}")

            txt_address.text = "Alamat : ${it.owner.address}"
            txt_telp.text = "Telephone : ${it.owner.telp}"
            /*txt_date.text = "Tanggal : ${it.date.date}"
            txt_hour.text = "Jam : ${it.date.hour.hour} WIB"
            txt_remaining_seat.text = "Sisa Kursi : ${it.date.hour.remaining_seat}"*/

            for (x in it.owner.cars){
                txt_facility.text = "Fasilitas : ${x.facility}"
            }
        }
    }

    private fun getPassedDeparture() : Departure? = intent.getParcelableExtra("DEPARTURE")
}
