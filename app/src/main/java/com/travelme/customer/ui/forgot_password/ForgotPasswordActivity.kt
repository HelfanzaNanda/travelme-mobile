package com.travelme.customer.ui.forgot_password

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.travelme.customer.R
import com.travelme.customer.utilities.extensions.gone
import com.travelme.customer.utilities.extensions.visible
import kotlinx.android.synthetic.main.activity_forgot_password.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForgotPasswordActivity : AppCompatActivity() {

    private val forgotPasswordViewModel : ForgotPasswordViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        supportActionBar?.hide()
        observer()
        forgotPassword()
    }

    private fun handleUiState(it: ForgotPasswordState) {
        when(it){
            is ForgotPasswordState.Loading -> handleLoading(it.state)
            is ForgotPasswordState.ShowToast -> toast(it.message)
            is ForgotPasswordState.Validate -> handleValidate(it)
            is ForgotPasswordState.Reset -> handleReset()
            is ForgotPasswordState.Success -> handleSuccess()
        }
    }

    private fun alert(message: String){
        AlertDialog.Builder(this).apply {
            setMessage(message)
            setPositiveButton("ya"){dialog, _ ->
                dialog.dismiss()
                finish()
            }
        }.show()
    }

    private fun handleReset() = setEmailError(null)
    private fun handleSuccess() = alert("kami telah mengirimkan email, silahkan reset password melalui email")
    private fun handleValidate(validate: ForgotPasswordState.Validate) = validate.email?.let { setEmailError(it) }

    private fun handleLoading(state: Boolean) {
        if (state) loading.visible() else loading.gone()
        btn_forgot_password.isEnabled = !state
    }

    private fun observer() = forgotPasswordViewModel.listenToState().observer(this, Observer { handleUiState(it) })
    private fun toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    private fun setEmailError(err : String?){ til_email.error = err }

    private fun forgotPassword(){
        btn_forgot_password.setOnClickListener {
            val email = et_email.text.toString().trim()
            if (forgotPasswordViewModel.validate(email)){
                forgotPasswordViewModel.forgotPassword(email)
            }
        }
    }
}
