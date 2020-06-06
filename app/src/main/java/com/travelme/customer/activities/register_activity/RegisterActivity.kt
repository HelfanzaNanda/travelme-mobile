package com.travelme.customer.activities.register_activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.travelme.customer.R
import com.travelme.customer.activities.login_activity.LoginActivity
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.til_email
import kotlinx.android.synthetic.main.activity_register.til_password
import kotlinx.android.synthetic.main.activity_register.txt_login
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {
    
    private val registerViewModel : RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        txt_login.setOnClickListener { startActivity(Intent(this@RegisterActivity, LoginActivity::class.java)) }

        registerViewModel.listenToState().observe(this, Observer { handleUIState(it) })
        

        btn_register.setOnClickListener {
            val name = et_name.text.toString().trim()
            val email = et_email.text.toString().trim()
            val password = et_password.text.toString().trim()
            val confirm_pass = et_password.text.toString().trim()
            val phone = et_telp.text.toString().trim()
            if (registerViewModel.validate(name, email, password, confirm_pass, phone)){
                registerViewModel.register(name, email, password, phone)
            }
        }
    }

    private fun handleUIState(it : RegisterState){
        when(it){
            is RegisterState.Validate -> {
                it.name?.let { setNameError(it) }
                it.email?.let { setEmailError(it) }
                it.password?.let { setPasswordError(it) }
                it.confirmPassword?.let { setConfirmPasswordError(it) }
                it.telp?.let { setTelpError(it) }
            }
            is RegisterState.ShowToast -> toast(it.message)
            is RegisterState.Reset -> {
                setNameError(null)
                setEmailError(null)
                setPasswordError(null)
                setConfirmPasswordError(null)
                setTelpError(null)
            }
            is RegisterState.IsLoading -> {
                if (it.state){
                    pb_register.visibility = View.VISIBLE
                    btn_register.isEnabled = false
                }else{
                    btn_register.isEnabled = true
                    pb_register.visibility = View.GONE
                }
            }
            is RegisterState.Success -> success(it.message)
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
