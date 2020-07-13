package com.travelme.customer.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.travelme.customer.R
import com.travelme.customer.fragments.home.HomeFragment
import com.travelme.customer.fragments.order.OrderFragment
import com.travelme.customer.fragments.profile.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object{
        var navStatus = -1
        const val CHANNEL_ID = "travelme-customer"
        private const val CHANNEL_NAME= "TravelMe"
        private const val CHANNEL_DESC = "Android FCM"
    }
    private var fragment : Fragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        if(savedInstanceState == null){ navigation.selectedItemId = R.id.navigation_home }
        supportActionBar?.hide()
        //isLoggedIn()

        action_info_app.setOnClickListener {
            AlertDialog.Builder(this@MainActivity).apply {
                setTitle("travelme")
                setMessage("by Helfanza Nanda Alfara")
                setPositiveButton("ya"){dialog,_ ->
                    dialog.dismiss()
                }
            }.show()
        }

        setupNotificationManager()
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when(item.itemId){
            R.id.navigation_home -> {
                if(navStatus != 0){
                    fragment = HomeFragment()
                    navStatus = 0
                }
            }
            R.id.navigation_order -> {
                if(navStatus != 1){
                    fragment = OrderFragment()
                    navStatus = 1
                }
            }

            R.id.navigation_profile -> {
                if(navStatus != 2){
                    fragment = ProfileFragment()
                    navStatus = 2
                }
            }
        }
        if(fragment == null){
            navStatus = 0
            fragment = HomeFragment()
        }

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.screen_container, fragment!!)
        fragmentTransaction.commit()
        true
    }

    private fun setupNotificationManager(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = CHANNEL_DESC
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }


    private fun toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}


