package com.travelme.customer.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.travelme.customer.R
import kotlinx.android.synthetic.main.activity_order.*

class OrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        totalSeat()
    }


    private fun totalSeat(){
        var count = 1
        btn_plus_seat.setOnClickListener {
            count = count+1
            txt_seat.text = count.toString()
        }

        btn_min_seat.setOnClickListener {
            if (count < 1){
                btn_min_seat.isEnabled = false
            }else{
                count = count-1
                txt_seat.text = count.toString()
            }
        }
    }
}
