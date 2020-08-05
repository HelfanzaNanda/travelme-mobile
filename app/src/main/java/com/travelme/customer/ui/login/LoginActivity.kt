package com.travelme.customer.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.travelme.customer.R
import com.travelme.customer.ui.main.MainActivity
import com.travelme.customer.ui.forgot_password.ForgotPasswordActivity
import com.travelme.customer.ui.register.RegisterActivity
import com.travelme.customer.utilities.Constants
import com.travelme.customer.utilities.extensions.visible
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        observer()
        txt_forgot_password.setOnClickListener {startActivity(Intent(this, ForgotPasswordActivity::class.java)) }
        txt_register.setOnClickListener { startActivity(Intent(this@LoginActivity, RegisterActivity::class.java)) }
        doLogin()
    }

    private fun observer() = loginViewModel.listenToState().observer(this@LoginActivity, Observer { handleUI(it) })

    private fun handleUI(it : LoginState) {
        when(it){
            is LoginState.IsLoading -> handleLoading(it.state)
            is LoginState.ShowToast -> toast(it.message)
            is LoginState.Success -> handleSuccess(it.token)
            is LoginState.Reset -> handleReset()
            is LoginState.Validate -> handleValidate(it)
        }
    }

    private fun handleValidate(validate: LoginState.Validate) {
        validate.email?.let { setEmailError(it) }
        validate.password?.let { setPasswordError(it) }
    }

    private fun handleReset() {
        setEmailError(null)
        setPasswordError(null)
    }

    private fun handleSuccess(token  : String) {
        Constants.setToken(this@LoginActivity, "Bearer $token")
        if (getExpectResult()){
            finish()
        }else{
            startActivity(Intent(this@LoginActivity, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }).also { finish() }
        }
    }

    private fun handleLoading(state: Boolean) {
        if (state){
            pb_login.visible()
        }else{
            pb_login.visible()
        }
        btn_login.isEnabled = !state
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

    private fun getExpectResult() = intent.getBooleanExtra("EXPECT_RESULT", false)
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
