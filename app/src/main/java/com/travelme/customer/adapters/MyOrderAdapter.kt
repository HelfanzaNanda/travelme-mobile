package com.travelme.customer.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.travelme.customer.R
import com.travelme.customer.activities.DetailMyOrderActivity
import com.travelme.customer.models.HourOfDepartureAlternative
import com.travelme.customer.models.Order
import com.travelme.customer.utilities.Constants
import kotlinx.android.synthetic.main.item_my_order.view.*

class MyOrderAdapter (private var orders : MutableList<Order>, private var context: Context)
    : RecyclerView.Adapter<MyOrderAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_my_order, parent, false))
    }

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(orders[position], context)


    class ViewHolder(itemVoew: View) : RecyclerView.ViewHolder(itemVoew){
        fun bind(order: Order, context: Context){
            with(itemView){
                tv_name_owner.text = order.owner.business_name
                tv_hour.text = "${order.hour} WIB"
                tv_date.text = order.date
                tv_destination.text = "${order.departure.from} -> ${order.departure.destination}"
                tv_price.text = "${Constants.setToIDR(order.price!!)}"
                tv_total_seat.text = "${order.total_seat} Kursi"
                setOnClickListener {
                    context.startActivity(Intent(context , DetailMyOrderActivity::class.java).apply {
                        putExtra("MY_ORDER", order)
                    })
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