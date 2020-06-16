package com.travelme.customer.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.travelme.customer.R
import com.travelme.customer.activities.login_activity.LoginActivity
import com.travelme.customer.fragments.home_fragment.HomeFragment
import com.travelme.customer.fragments.notification_fragment.NotificationFragment
import com.travelme.customer.fragments.order_fragment.OrderFragment
import com.travelme.customer.fragments.profile_fragment.ProfileFragment
import com.travelme.customer.utilities.Constants
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object{ var navStatus = -1 }
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

            R.id.navigation_notification -> {
                if(navStatus != 2){
                    fragment = NotificationFragment()
                    navStatus = 2
                }
            }

            R.id.navigation_profile -> {
                if(navStatus != 3){
                    fragment = ProfileFragment()
                    navStatus = 3
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



    private fun toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}


