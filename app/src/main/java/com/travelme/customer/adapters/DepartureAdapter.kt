package com.travelme.customer.adapters

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.travelme.customer.R
import com.travelme.customer.activities.DetailOwnerActivity
import com.travelme.customer.models.Car
import com.travelme.customer.models.Departure
import com.travelme.customer.utilities.Constants
import kotlinx.android.synthetic.main.item_departure_by_dest.view.*
import java.util.ArrayList

class DepartureAdapter (private var departures : MutableList<Departure>, private var context: Context)
    : RecyclerView.Adapter<DepartureAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_departure_by_dest, parent, false))
    }

    override fun getItemCount(): Int = departures.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(departures[position], context)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(departure: Departure, context: Context){

            itemView.tv_name_owner.text = departure.owner.business_name
            itemView.iv_car.load("https://travelme-project.herokuapp.com/uploads/owner/car/"+departure.owner.cars.joinToString { car -> car.photo.toString() })
            itemView.tv_destination.text = "${departure.from} - ${departure.destination}"
            itemView.tv_price.text = "Harga ${Constants.setToIDR(departure.price!!)}"

            itemView.setOnClickListener {
                Toast.makeText(context, departure.owner.business_name, Toast.LENGTH_SHORT).show()
                context.startActivity(Intent(context, DetailOwnerActivity::class.java).apply {
                    putExtra("DEPARTURE", departure)
                    //putParcelableArrayListExtra("CARS", departure.owner.cars as ArrayList<Car>)
                })
            }

            //itemView.tv_date.text = departure.date.date
            /*itemView.tv_hour.text = "Jam : ${departure.date.hour.hour} WIB"
            itemView.tv_remaining_seat.text = "Sisa Kursi : ${departure.date.hour.remaining_seat}"*/
        }
    }
    fun changelist(c : List<Departure>){
        departures.clear()
        departures.addAll(c)
        notifyDataSetChanged()
    }

}