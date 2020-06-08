package com.travelme.customer.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.travelme.customer.R
import com.travelme.customer.activities.DetailOwnerActivity
import com.travelme.customer.models.Car
import com.travelme.customer.models.HourOfDepartureAlternative
import com.travelme.customer.utilities.Constants
import kotlinx.android.synthetic.main.item_departure_by_dest.view.*
import java.util.ArrayList
import kotlin.time.milliseconds

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
        @SuppressLint("SetTextI18n")
        fun bind(hourAlt: HourOfDepartureAlternative, context: Context) {
            with(itemView) {
                tv_name_owner.text = hourAlt.departure?.owner?.business_name
                iv_car.load("https://travelme-project.herokuapp.com/uploads/owner/car/"
                        + hourAlt.departure?.owner?.cars?.joinToString { car -> car.photo.toString() })
                tv_destination.text =
                    "${hourAlt.departure?.from} -> ${hourAlt.departure?.destination}"
                tv_price.text = Constants.setToIDR(hourAlt.departure?.price!!)
                tv_date.text = hourAlt.dateOfDeparture?.date.toString()
                tv_hour.text = "${hourAlt.hour.toString()} WIB"
                tv_remaining_seat.text = "Sisa ${hourAlt.remaining_seat} Kursi"

                if (hourAlt.remaining_seat!! != 0){
                    setOnClickListener {
                        context.startActivity(Intent(context, DetailOwnerActivity::class.java).apply {
                            putExtra("DEPARTURE", hourAlt)
                            putParcelableArrayListExtra("CARS", hourAlt.departure?.owner?.cars as ArrayList<Car>)
                        })
                    }
                }else if (hourAlt.remaining_seat!! == 0){
                    setOnClickListener {popup(context, "habis")}

                    tv_sold_out.visibility = View.VISIBLE

                    tv_name_owner.alpha = 0.4F
                    iv_car.alpha = 0.4F
                    tv_destination.alpha = 0.4F
                    tv_price.alpha = 0.4F
                    tv_date.alpha = 0.4F
                    tv_hour.alpha = 0.4F
                    tv_remaining_seat.alpha = 0.4F
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
}