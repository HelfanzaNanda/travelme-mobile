package com.travelme.customer.activities.update_profile

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.travelme.customer.R
import com.travelme.customer.models.User
import com.travelme.customer.utilities.Constants
import kotlinx.android.synthetic.main.activity_update_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class UpdateProfileActivity : AppCompatActivity() {

    private val updateProfilViewModel : UpdateProfilViewModel by viewModel()
    private var imgUrl = ""
    private val REQ_CODE_PIX = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)
        supportActionBar?.hide()
        setUi()
        //img_change_image.setOnClickListener { Pix.start(this, REQ_CODE_PIX) }
        observer()
        updateProfil()
    }

    private fun setUi(){
        getPassedUser()?.let {
            if (!it.photo.isNullOrEmpty()){
                //img_user.load(it.photo)
            }
            et_name.setText(it.name)
            et_email.setText(it.email)
            et_telp.setText(it.telp)
        }
    }

    private fun updateProfil() {
        btn_update.setOnClickListener {
            val token = Constants.getToken(this@UpdateProfileActivity)
            val name = et_name.text.toString().trim()
            val password = et_password.text.toString().trim()
            if (validate(name)){
                updateProfilViewModel.updateProfile(token, name, password, imgUrl)
            }
        }
    }

    private fun validate(name : String) : Boolean{
        if(name.isEmpty()){
            toast("nama tidak boleh kosong")
            return false
        }
        return true
    }

    private fun observer() {
        updateProfilViewModel.listenToState().observer(this, Observer { handleUiState(it) })
    }

    private fun handleUiState(it: UpdateProfilState) {
        when(it){
            is UpdateProfilState.Loading -> handleLoading(it.state)
            is UpdateProfilState.ShowToast -> toast(it.message)
            is UpdateProfilState.Success -> handleSuccess()
        }
    }

    private fun handleLoading(state: Boolean) {
        btn_update.isEnabled = !state
    }

    private fun handleSuccess() {
        finish()
        toast("berhasil mengupdate profile")
    }

    private fun toast(message : String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    private fun getPassedUser() : User? = intent.getParcelableExtra("USER")
}
