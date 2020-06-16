package com.travelme.customer.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.travelme.customer.R
import com.travelme.customer.models.Order
import kotlinx.android.synthetic.main.item_notification.view.*

class NotificationAdapter (private var orders : MutableList<Order>, private var context: Context)
    : RecyclerView.Adapter<NotificationAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false))
    }

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(orders[position], context)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        fun bind(order: Order, context: Context){
            with(itemView){
                if (order.verify.equals("2")){
                    txt_message.text = "pesanan anda sudah di verikasi admin, silahkan lanjutkan pembayaran"
                }else if (order.arrived == true){
                    txt_message.text = "driver sudah menunggu di lokasi penjemputan"
                }
            }
        }
    }

    fun changelist(c : List<Order>){
        orders.clear()
        orders.addAll(c)
        notifyDataSetChanged()
    }
}