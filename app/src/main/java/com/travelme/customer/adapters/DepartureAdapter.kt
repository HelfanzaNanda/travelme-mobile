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
import com.travelme.customer.models.HourOfDepartureAlternative
import com.travelme.customer.utilities.Constants
import kotlinx.android.synthetic.main.item_departure_by_dest.view.*
import java.util.ArrayList

class DepartureAdapter (private var hourAlts : MutableList<HourOfDepartureAlternative>, private var context: Context)
    : RecyclerView.Adapter<DepartureAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_departure_by_dest, parent, false))
    }

    override fun getItemCount(): Int = hourAlts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(hourAlts[position], context)

    fun changelist(c : List<HourOfDepartureAlternative>){
        hourAlts.clear()
        hourAlts.addAll(c)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(hourAlt: HourOfDepartureAlternative, context: Context) {
            with(itemView) {
                tv_name_owner.text = hourAlt.departure?.owner?.business_name
                iv_car.load("https://travelme-project.herokuapp.com/uploads/owner/car/"
                        + hourAlt.departure?.owner?.cars?.joinToString { car -> car.photo.toString() })
                tv_destination.text =
                    "${hourAlt.departure?.from} -> ${hourAlt.departure?.destination}"
                tv_price.text = "${Constants.setToIDR(hourAlt.departure?.price!!)}"
                tv_date.text = hourAlt.dateOfDeparture?.date.toString()
                tv_hour.text = "${hourAlt.hour.toString()} WIB"
                tv_remaining_seat.text = "Sisa ${hourAlt.remaining_seat} Kursi"
                setOnClickListener {
                    context.startActivity(Intent(context, DetailOwnerActivity::class.java).apply {
                        putExtra("DEPARTURE", hourAlt)
                        putParcelableArrayListExtra(
                            "CARS",
                            hourAlt.departure?.owner?.cars as ArrayList<Car>
                        )
                    })
                }
            }
        }
    }

}