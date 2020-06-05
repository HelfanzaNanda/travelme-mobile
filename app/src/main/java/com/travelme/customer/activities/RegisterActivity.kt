package com.travelme.customer.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.travelme.customer.R
import com.travelme.customer.models.User
import com.travelme.customer.viewmodels.UserState
import com.travelme.customer.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.til_email
import kotlinx.android.synthetic.main.activity_register.til_password
import kotlinx.android.synthetic.main.activity_register.txt_login

class RegisterActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        txt_login.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.getState().observe(this, Observer {
            handleUIState(it)
        })

        btn_register.setOnClickListener {
            val name = et_name.text.toString().trim()
            val email = et_email.text.toString().trim()
            val password = et_password.text.toString().trim()
            val confirm_pass = et_password.text.toString().trim()
            val phone = et_telp.text.toString().trim()
            if (userViewModel.validate(name, email, password, confirm_pass, phone)){
                userViewModel.register(name, email, password, phone)
            }
        }
    }

    private fun handleUIState(it : UserState){
        when(it){
            is UserState.Validate -> {
                it.name?.let { setNameError(it) }
                it.email?.let { setEmailError(it) }
                it.password?.let { setPasswordError(it) }
                it.confirmPassword?.let { setConfirmPasswordError(it) }
                it.telp?.let { setTelpError(it) }
            }
            is UserState.ShowToast -> toast(it.message)
            is UserState.Reset -> {
                setNameError(null)
                setEmailError(null)
                setPasswordError(null)
                setConfirmPasswordError(null)
                setTelpError(null)
            }
            is UserState.IsLoading -> {
                if (it.state){
                    pb_register.visibility = View.VISIBLE
                    btn_register.isEnabled = false
                }else{
                    btn_register.isEnabled = true
                    pb_register.visibility = View.GONE
                }
            }
            is UserState.Success -> success(it.token)
        }
    }

    private fun toast(message : String) = Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
    private fun setNameError(err : String?){ til_name.error = err }
    private fun setEmailError(err : String?){ til_email.error = err }
    private fun setPasswordError(err : String?){ til_password.error = err }
    private fun setConfirmPasswordError(err : String?){ til_password.error = err }
    private fun setTelpError(err : String?){ til_telp.error = err }

    private fun success(email : String) {
        AlertDialog.Builder(ContextThemeWrapper(this, R.style.ThemeOverlay_AppCompat_Dialog_Alert)).apply {
            setMessage("Kami telah mengirim email ke $email. Pastikan anda telah memverifikasi email sebelum login")
            setPositiveButton("Mengerti"){ d, _ ->
                d.cancel()
                finish()
            }.create().show()
        }
    }

}
