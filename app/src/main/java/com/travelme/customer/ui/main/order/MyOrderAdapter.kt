package com.travelme.customer.ui.main.order

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.Layout.JUSTIFICATION_MODE_INTER_WORD
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.travelme.customer.R
import com.travelme.customer.utilities.extensions.gone
import com.travelme.customer.utilities.extensions.visible
import com.travelme.customer.models.Order
import com.travelme.customer.utilities.Constants
import kotlinx.android.synthetic.main.item_my_order.view.*

class MyOrderAdapter(
    private var orders: MutableList<Order>,
    private var context: Context,
    private var orderFragmentViewModel: OrderFragmentViewModel
) : RecyclerView.Adapter<MyOrderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_my_order, parent, false))
    }

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(orders[position], context, orderFragmentViewModel)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var currentPosition = false
        private val paymentMidtrans = PaymentMidtrans()
        @SuppressLint("SetTextI18n", "WrongConstant")
        fun bind(order: Order, context: Context, orderFragmentViewModel: OrderFragmentViewModel) {
            with(itemView) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    txt_content.justificationMode = JUSTIFICATION_MODE_INTER_WORD
                }
                txt_content.text = "Menunggu konfirmasi dari admin, nanti kami kabarin lagi"
                txt_content.setOnClickListener {
                    if (currentPosition == false) {
                        val slideDown = AnimationUtils.loadAnimation(context, R.anim.anim)
                        linear_layout.visible()
                        linear_layout.startAnimation(slideDown)
                        currentPosition = true
                    } else {
                        val slideDown = AnimationUtils.loadAnimation(context, R.anim.anim)
                        linear_layout.gone()
                        linear_layout.startAnimation(slideDown)
                        currentPosition = false
                    }
                }

                tv_order_id.text = "id : ${order.order_id}"
                tv_name_owner.text = order.owner.business_name
                tv_hour.text = "Jam : ${order.hour} WIB"
                tv_date.text = "Tanggal : ${order.date}"
                tv_destination.text = "${order.departure.from} -> ${order.departure.destination}"
                tv_price.text = Constants.setToIDR(order.total_price!!)
                tv_total_seat.text = "${order.total_seat} Kursi"

                if (order.verify!!.equals("1")) {
                    btn_cancel.visible()
                    btn_cancel.setOnClickListener {
                        AlertDialog.Builder(context).apply {
                            setMessage("apakah anda ingin membatalkan pesanan ini?")
                            setNegativeButton("tidak") { dialog, _ ->
                                dialog.dismiss()
                            }
                            setPositiveButton("ya") { _, _ ->
                                orderFragmentViewModel.cancel(Constants.getToken(context), order.id.toString())
                            }
                        }.create().show()
                    }
                } else if (order.verify!!.equals("2") && order.status.equals("none")) {
                    txt_content.text = "silahkan lanjutkan pembayaran"
                    paymentMidtrans.initPayment(context, order.id.toString(), orderFragmentViewModel)
                    btn_confirm.gone()
                    btn_pay.visible()
                    btn_pay.setOnClickListener {
                        paymentMidtrans.showPayment(context, order.id.toString(), order.price!!, order.total_seat!!, order.departure.destination!!)
                    }
                } else if (order.verify!!.equals("2") && order.status.equals("pending")) {
                    txt_content.text = "silahkan di bayar"
                    setOnClickListener {
                        val url_snap =
                            "https://app.sandbox.midtrans.com/snap/v2/vtweb/${order.snap}"
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url_snap)))
                    }
                }else if(order.verify!!.equals("2") && order.status.equals("deny")){
                    txt_content.text = "sudah di bayar"
                } else if(order.verify!!.equals("2") && order.status.equals("settlement")){
                    txt_content.text = "sudah di bayarkan"
                } else {
                    btn_cancel.gone()
                    btn_pay.gone()
                    if (order.additional_price!! > 0){
                        btn_cancel.visible()
                        btn_cancel.setOnClickListener {
                            AlertDialog.Builder(context).apply {
                                setMessage("apakah anda ingin membatalkan pesanan ini?")
                                setNegativeButton("tidak") { dialog, _ ->
                                    dialog.dismiss()
                                }
                                setPositiveButton("ya") { _, _ ->
                                    orderFragmentViewModel.cancel(
                                        Constants.getToken(context),
                                        order.id.toString()
                                    )
                                }
                            }.create().show()
                        }
                        btn_confirm.visible()
                        btn_confirm.setOnClickListener {
                            orderFragmentViewModel.confirm(Constants.getToken(context), order.id.toString())
                        }
                        txt_content.text = "${order.owner.business_name} meminta tambahan harga sebesar " +
                                "${Constants.setToIDR(order.additional_price!!)}, karena lokasi penjemputan atau lokasi tujuan terlalu jauh"
                    }else if (order.reason_for_refusing != null){
                        txt_content.text = "pesanan anda di tolak karena ${order.reason_for_refusing}"
                    }else if(order.verify!! == "0" && order.additional_price == 0){
                        txt_content.text = "pesanan tertolak"
                        btn_cancel.gone()
                        btn_confirm.gone()
                    }
                }
            }
        }
    }

    fun changelist(c: List<Order>) {
        orders.clear()
        orders.addAll(c)
        notifyDataSetChanged()
    }
}