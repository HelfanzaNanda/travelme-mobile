package com.travelme.customer.activities.login_activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.travelme.customer.R
import com.travelme.customer.activities.MainActivity
import com.travelme.customer.activities.register_activity.RegisterActivity
import com.travelme.customer.utilities.Constants
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        
        
        loginViewModel.listenToState().observer(this@LoginActivity, Observer { handleUI(it) })
        doLogin()

        txt_register.setOnClickListener { startActivity(Intent(this@LoginActivity, RegisterActivity::class.java)) }
    }

    private fun handleUI(it : LoginState){
        when(it){
            is LoginState.IsLoading -> {
                if (it.state){
                    pb_login.visibility = View.VISIBLE
                    btn_login.isEnabled = false
                }else{
                    btn_login.isEnabled = true
                    pb_login.visibility = View.GONE
                }
            }
            is LoginState.ShowToast -> toast(it.message)
            is LoginState.Success -> {
                Constants.setToken(this@LoginActivity, "Bearer ${it.message}")
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
            is LoginState.Reset -> {
                setEmailError(null)
                setPasswordError(null)
            }
            is LoginState.Validate -> {
                it.email?.let { setEmailError(it) }
                it.password?.let { setPasswordError(it) }
            }
        }
    }

    private fun doLogin(){
        btn_login.setOnClickListener {
            val email = et_email.text.toString().trim()
            val password = et_password.text.toString().trim()
            if (loginViewModel.validate(email, password)){
                loginViewModel.login(email, password)
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
            finish()
        }
    }
}
