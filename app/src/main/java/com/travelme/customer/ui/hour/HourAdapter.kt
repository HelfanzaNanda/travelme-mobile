package com.travelme.customer.ui.hour

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.travelme.customer.MyOnClickListener
import com.travelme.customer.R
import com.travelme.customer.models.HourOfDeparture
import kotlinx.android.synthetic.main.item_hour.view.*

class HourAdapter (private var hours: MutableList<HourOfDeparture>, private var myOnClickListener: MyOnClickListener)
    : RecyclerView.Adapter<HourAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_hour, parent, false)
        )
    }

    override fun getItemCount(): Int = hours.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =holder.bind(hours[position], myOnClickListener)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        fun bind(hour : HourOfDeparture, myOnClickListener: MyOnClickListener){
            with(itemView){
                txt_hour.text = "${hour.hour} WIB"
                setOnClickListener {
                    myOnClickListener.click(hour.hour!!)
                }
            }
        }
    }

    fun changelist(c : List<HourOfDeparture>){
        hours.clear()
        hours.addAll(c)
        notifyDataSetChanged()
    }
}