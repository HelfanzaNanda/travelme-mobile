package com.travelme.customer.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.travelme.customer.R
import com.travelme.customer.activities.DepartureByDestActivity
import com.travelme.customer.models.Departure
import kotlinx.android.synthetic.main.item_destination.view.*

class DestinationAdapter (private var departures : MutableList<Departure>, private var context : Context)
    : RecyclerView.Adapter<DestinationAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_destination, parent, false))
    }

    override fun getItemCount(): Int = departures.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(departures[position], context)


    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(departure: Departure, context: Context){
            itemView.iv_destination.load("https://travelme-project.herokuapp.com/uploads/owner/destination/"+departure.photo_destination)
            itemView.setOnClickListener {
                Toast.makeText(context, departure.destination, Toast.LENGTH_SHORT).show()
                context.startActivity(Intent(context, DepartureByDestActivity::class.java).apply {
                    putExtra("DESTINATION", departure.destination)
                })
            }
        }
    }
    fun changelist(c : List<Departure>){
        departures.clear()
        departures.addAll(c)
        notifyDataSetChanged()
    }

}