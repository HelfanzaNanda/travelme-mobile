package com.travelme.customer.ui.seat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.travelme.customer.R
import com.travelme.customer.models.Seat
import kotlinx.android.synthetic.main.item_seat.view.*

class SeatAdapter (private var seats : MutableList<Seat>, private var context : Context)
    : RecyclerView.Adapter<SeatAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_seat, parent, false))
    }

    override fun getItemCount(): Int = seats.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(seats[position], context)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(seat: Seat, context: Context){
            with(itemView){
                card_seat.setOnClickListener {
                    Toast.makeText(context, seat.name, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun changelist(c : List<Seat>){
        seats.clear()
        seats.addAll(c)
        notifyDataSetChanged()
    }
}