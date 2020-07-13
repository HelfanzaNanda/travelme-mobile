package com.travelme.customer.fragments.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.travelme.customer.R
import com.travelme.customer.activities.update_profile.UpdateProfileActivity
import com.travelme.customer.activities.login.LoginActivity
import com.travelme.customer.models.User
import com.travelme.customer.utilities.Constants
import kotlinx.android.synthetic.main.fragment_not_logged_in.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(){
    private val profileViewModel : ProfileViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (isLoggedIn()){
            return inflater.inflate(R.layout.fragment_profile, container, false)
        }
        return inflater.inflate(R.layout.fragment_not_logged_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isLoggedIn()){
            profileViewModel.profile(Constants.getToken(activity!!))
            profileViewModel.listenToUser().observe(viewLifecycleOwner, Observer { handleData(it) })
            logout()
        }else{
            view.btn_login.setOnClickListener {
                startActivity(Intent(activity, LoginActivity::class.java).putExtra("EXPECT_RESULT", false))
            }
        }
    }

    private fun popupIsLogin(message: String){
        AlertDialog.Builder(activity!!).apply {
            setMessage(message)
            setPositiveButton("login"){dialog, _ ->
                startActivity(Intent(activity, LoginActivity::class.java))
                dialog.dismiss()
            }
            setNegativeButton("cancel"){dialog, _ ->
                dialog.dismiss()
            }
        }.show()
    }

    private fun isLoggedIn() = !Constants.getToken(activity!!).equals("UNDEFINED")

    private fun logout(){
        btn_logout.setOnClickListener {
            Constants.clearToken(activity!!)
            startActivity(Intent(activity!!, LoginActivity::class.java))
            activity!!.finish()
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

    override fun onResume() {
        super.onResume()
        profileViewModel.profile(Constants.getToken(activity!!))
    }

}