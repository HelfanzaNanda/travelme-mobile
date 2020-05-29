package com.travelme.customer.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.UIKitCustomSetting
import com.midtrans.sdk.corekit.models.snap.ItemDetails
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import com.travelme.customer.BuildConfig
import com.travelme.customer.R
import kotlinx.android.synthetic.main.activity_try_sandbox.*
import java.util.ArrayList

class TrySandboxActivity : AppCompatActivity(), TransactionFinishedCallback {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_try_sandbox)
        initPayment()
        btn_pay.setOnClickListener {
            showPayment()
        }
    }

    private fun initPayment(){
        SdkUIFlowBuilder.init().apply {
            setClientKey(BuildConfig.CLIENT_KEY)
            setContext(this@TrySandboxActivity)
            enableLog(true)
            setMerchantBaseUrl("${BuildConfig.SNAP_TOKEN}api/try/")
            setTransactionFinishedCallback(this@TrySandboxActivity)
        }.buildSDK()
    }

    private fun showAlert(message : String){
        AlertDialog.Builder(this).apply {
            setMessage(message)
            setPositiveButton("Ya"){x, _ -> x.dismiss()}
        }.show()
    }

    private fun showPayment(){
        val uiKIt = UIKitCustomSetting().apply {
            isSkipCustomerDetailsPages = true
            isShowPaymentStatus = true
        }
        val midtrans = MidtransSDK.getInstance().apply {
            setTransactionRequest(setupTransactionRequest())
            uiKitCustomSetting = uiKIt
        }
        midtrans.startPaymentUiFlow(this)
    }

    private fun setupTransactionRequest() : TransactionRequest{
        val request = TransactionRequest(System.currentTimeMillis().toString(), 20000.toDouble())
        val items = ArrayList<com.midtrans.sdk.corekit.models.ItemDetails>()
        items.add(com.midtrans.sdk.corekit.models.ItemDetails("1", 10000.toDouble(), 1, "Indomie"))
        items.add(com.midtrans.sdk.corekit.models.ItemDetails("2", 10000.toDouble(), 1, "Bubur"))
        request.itemDetails = items
        return request
    }

    override fun onTransactionFinished(r: TransactionResult?) {
        r?.let { result ->
            result.response?.let {
                when(result.status){
                    TransactionResult.STATUS_FAILED -> {
                        showAlert("Transaction is failed")
                        println(result.statusMessage)
                        println(result.status)
                        println(result.response)
                    }
                    TransactionResult.STATUS_PENDING -> {
                        showAlert("Transaction is pending")
                        println(result.statusMessage)
                        println(result.status)
                        println(result.response)

                        //retrofit
                        //viewMOdel.createOrderRecord()
//                        result.response.orderId(token:, orderId)
                    }
                    TransactionResult.STATUS_SUCCESS -> {
                        showAlert("Transaction is success")
                        println(result.statusMessage)
                        println(result.status)
                        println(result.response)
                    }
                    TransactionResult.STATUS_INVALID -> {
                        showAlert("Transaction is invalid")
                        println(result.statusMessage)
                        println(result.status)
                        println(result.response)
                    }
                }
            }
            if(result.isTransactionCanceled){
                toast("Transaction is cancelled")
            }
            println(result.response.toString())
            showAlert(result.response.toString())
        } ?: kotlin.run {
            showAlert("Response is null")
        }

    }

    private fun toast(x :String) = Toast.makeText(this, x, Toast.LENGTH_LONG).show()
}
