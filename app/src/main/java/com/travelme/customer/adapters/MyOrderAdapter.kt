package com.travelme.customer.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.travelme.customer.R
import com.travelme.customer.activities.DetailMyOrderActivity
import com.travelme.customer.fragments.order_fragment.OrderFragmentViewModel
import com.travelme.customer.models.Order
import com.travelme.customer.utilities.Constants
import kotlinx.android.synthetic.main.item_my_order.view.*

class MyOrderAdapter (private var orders : MutableList<Order>,
                      private var context: Context,
                      private var orderFragmentViewModel: OrderFragmentViewModel)
    : RecyclerView.Adapter<MyOrderAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_my_order, parent, false))
    }

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(orders[position], context, orderFragmentViewModel)


    class ViewHolder(itemVoew: View) : RecyclerView.ViewHolder(itemVoew){
        @SuppressLint("SetTextI18n")
        fun bind(order: Order, context: Context, orderFragmentViewModel: OrderFragmentViewModel){
            with(itemView){
                if (order.verify!!.equals("1")){
                    btn_cancel.visibility = View.VISIBLE
                }else if (order.verify!!.equals("2")){
                    btn_pay.visibility = View.VISIBLE
                }else{
                    btn_cancel.visibility = View.GONE
                }
                tv_name_owner.text = order.owner.business_name
                tv_hour.text = "${order.hour} WIB"
                tv_date.text = order.date
                tv_destination.text = "${order.departure.from} -> ${order.departure.destination}"
                tv_price.text = Constants.setToIDR(order.price!!)
                tv_total_seat.text = "${order.total_seat} Kursi"
                setOnClickListener {
                    context.startActivity(Intent(context , DetailMyOrderActivity::class.java).apply {
                        putExtra("MY_ORDER", order)
                    })
                }
                btn_cancel.setOnClickListener {
                    AlertDialog.Builder(context).apply {
                        setMessage("apakah anda ingin membatalkan pesanan ini?")
                        setNegativeButton("tidak"){dialog, _ ->
                            dialog.dismiss()
                        }
                        setPositiveButton("ya"){_,_ ->
                            orderFragmentViewModel.cancel(Constants.getToken(context), order.id.toString())
                        }
                    }.create().show()
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