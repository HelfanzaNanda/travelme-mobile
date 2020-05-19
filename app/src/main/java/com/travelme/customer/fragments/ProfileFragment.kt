package com.travelme.customer.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.travelme.customer.R
import com.travelme.customer.activities.UpdateProfileActivity
import com.travelme.customer.models.User
import com.travelme.customer.utilities.Constants
import com.travelme.customer.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(R.layout.fragment_profile){
    private lateinit var userViewModel: UserViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.getUserIsLogin(Constants.getToken(activity!!))
        userViewModel.getUser().observe(viewLifecycleOwner, Observer {
            setUI(it)
        })
    }

    private fun setUI(it : User){
        txt_name.text = it.name
        txt_email.text = it.email
        txt_telp.text = it.telp

        btn_edit_profile.setOnClickListener {  btn->
            startActivity(Intent(activity, UpdateProfileActivity::class.java).apply {
                putExtra("USER", it )
            })
        }
    }

}