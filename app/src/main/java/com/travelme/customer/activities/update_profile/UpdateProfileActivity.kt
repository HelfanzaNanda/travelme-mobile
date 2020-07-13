package com.travelme.customer.activities.update_profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.api.load
import com.travelme.customer.R
import com.travelme.customer.models.User
import kotlinx.android.synthetic.main.activity_update_profile.*

class UpdateProfileActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)

        getPassedUser()?.let {
            img_user.load(it.photo)
            et_name.setText(it.name)
            et_email.setText(it.email)
            et_telp.setText(it.telp)
        }
    }

    private fun getPassedUser() : User? = intent.getParcelableExtra("USER")
}
