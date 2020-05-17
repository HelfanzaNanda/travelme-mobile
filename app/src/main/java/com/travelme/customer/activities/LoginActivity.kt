package com.travelme.customer.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.travelme.customer.R
import com.travelme.customer.utilities.Constants
import com.travelme.customer.viewmodels.UserState
import com.travelme.customer.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.getState().observer(this@LoginActivity, Observer {
            handleUI(it)
        })
        doLogin()

        txt_register.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    private fun handleUI(it : UserState){
        when(it){
            is UserState.IsLoading -> {
                if (it.state){
                    pb_login.visibility = View.VISIBLE
                    btn_login.isEnabled = false
                }else{
                    btn_login.isEnabled = true
                    pb_login.visibility = View.GONE
                }
            }
            is UserState.ShowToast -> toast(it.message)
            is UserState.Success -> {
                Constants.setToken(this@LoginActivity, "Bearer ${it.token}")
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            }
            is UserState.Reset -> {
                setEmailError(null)
                setPasswordError(null)
            }
            is UserState.Validate -> {
                it.email?.let {
                    setEmailError(it)
                }
                it.password?.let {
                    setPasswordError(it)
                }
            }
        }
    }

    private fun doLogin(){
        btn_login.setOnClickListener {
            val email = et_email.text.toString().trim()
            val password = et_password.text.toString().trim()
            if (userViewModel.validate(null, email, password, null, null)){
                userViewModel.login(email, password)
            }
        }
    }

    private fun setEmailError(err : String?){ til_email.error = err }
    private fun setPasswordError(err : String?){ til_password.error = err }

    private fun toast(message : String) = Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()

    override fun onResume() {
        super.onResume()
        if (!Constants.getToken(this@LoginActivity).equals("UNDEFINED")){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
    }
}
