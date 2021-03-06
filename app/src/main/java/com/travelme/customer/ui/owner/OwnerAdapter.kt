package com.travelme.customer.ui.owner

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.travelme.customer.R
import com.travelme.customer.ui.detail_owner.DetailOwnerActivity
import com.travelme.customer.models.HourOfDeparture
import com.travelme.customer.utilities.Constants
import kotlinx.android.synthetic.main.item_hour_owner.view.*

class OwnerAdapter (private var hours : MutableList<HourOfDeparture>, private var context: Context)
    : RecyclerView.Adapter<OwnerAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_hour_owner, parent, false)
        )
    }

    override fun getItemCount(): Int = hours.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(hours[position], context)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        fun bind(hour : HourOfDeparture, context: Context){
            with(itemView){
                tv_name_owner.text = hour.date.departure.owner.business_name
                iv_car.load(hour.car.photo)
                tv_destination.text = "${hour.date.departure.from} -> ${hour.date.departure.destination}"
                tv_price.text = Constants.setToIDR(hour.date.departure.price!!)
                tv_date.text = hour.date.date!!
                tv_hour.text = "${hour.hour} WIB"
                tv_remaining_seat.text = "Sisa ${hour.remaining_seat} Kursi"
                setOnClickListener {
                    context.startActivity(Intent(context, DetailOwnerActivity::class.java).apply {
                        putExtra("DEPARTURE", hour)
                    })
                }
            }
        }
        private fun popup(context: Context, message : String){
            AlertDialog.Builder(context).apply {
                setMessage(message)
                setPositiveButton("ya"){dialog,_ ->
                    dialog.dismiss()
                }
            }.show()
        }
    }

    fun changelist(c : List<HourOfDeparture>){
        hours.clear()
        hours.addAll(c)
        notifyDataSetChanged()
    }
}