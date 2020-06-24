package com.travelme.customer.activities.update_profile_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.travelme.customer.R
import com.travelme.customer.models.User

class UpdateProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)
    }

    private fun getPassedUser() : User? = intent.getParcelableExtra("USER")
}
