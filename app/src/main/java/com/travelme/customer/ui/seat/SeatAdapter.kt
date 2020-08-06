package com.travelme.customer.ui.seat

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.travelme.customer.R
import com.travelme.customer.models.Seat
import com.travelme.customer.utilities.extensions.gone
import kotlinx.android.synthetic.main.item_seat.view.*

class SeatAdapter (private var seats : MutableList<Seat>,
                   private var context : Context,
                   private var seatViewModel: SeatViewModel)
    : RecyclerView.Adapter<SeatAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_seat, parent, false))
    }

    override fun getItemCount(): Int = seats.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(seats[position], context, seatViewModel, position)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(seat: Seat, context: Context, seatViewModel: SeatViewModel, position: Int){
            with(itemView){
                when(position){
                    0 -> {
                        card_seat.isEnabled = false
                        img_seat.load(R.drawable.ic_close)
                        img_seat.setColorFilter(Color.GRAY)
                    }
                    1 -> gone()
                }
                if (seat.status.equals("booked")){
                    img_seat.load(R.drawable.ic_close)
                    img_seat.setColorFilter(Color.GRAY)
                    card_seat.isEnabled = false
                }else if (seat.status.equals("available")){
                    if (seat.selected){
                        card_seat.setBackgroundColor(Color.GRAY)
                        card_seat.setOnClickListener { seatViewModel.removeSelectedSeat(seat) }
                    }else{
                        card_seat.setBackgroundColor(Color.WHITE)
                        card_seat.setOnClickListener { seatViewModel.addSelectedSeat(seat) }
                    }
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