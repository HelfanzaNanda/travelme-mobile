package com.travelme.customer.fragments.profile_fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.travelme.customer.R
import com.travelme.customer.activities.UpdateProfileActivity
import com.travelme.customer.activities.login_activity.LoginActivity
import com.travelme.customer.models.User
import com.travelme.customer.utilities.Constants
import com.travelme.customer.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(R.layout.fragment_profile){
    private val profileViewModel : ProfileViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.profile(Constants.getToken(activity!!))
        profileViewModel.listenToUser().observe(viewLifecycleOwner, Observer { handleData(it) })

        btn_logout.setOnLongClickListener {
            Constants.clearToken(activity!!)
            startActivity(Intent(activity!!, LoginActivity::class.java))
            activity!!.finish()
            true
        }
    }

    private fun handleData(user : User){
        txt_name.text = user.name
        txt_email.text = user.email
        txt_telp.text = user.telp

        btn_edit_profile.setOnClickListener {
            startActivity(Intent(activity, UpdateProfileActivity::class.java).apply {
                putExtra("USER", user )
            })
        }
    }

}