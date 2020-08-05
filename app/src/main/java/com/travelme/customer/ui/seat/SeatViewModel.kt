package com.travelme.customer.ui.seat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.travelme.customer.models.Seat
import com.travelme.customer.repositories.SeatRepository
import com.travelme.customer.utilities.ArrayResponse
import com.travelme.customer.utilities.SingleLiveEvent

class SeatViewModel (private val seatRepository: SeatRepository) : ViewModel(){
    private val state : SingleLiveEvent<SeatState> = SingleLiveEvent()
    private val seats = MutableLiveData<List<Seat>>()

    private fun setLoading(){ state.value = SeatState.Loading(true) }
    private fun hideLoading(){ state.value = SeatState.Loading(false) }
    private fun toast(message: String){ state.value = SeatState.ShowToast(message) }

    fun checkSeat(car_id : String, date : String, hour : String){
        setLoading()
        seatRepository.checkSeat(car_id, date, hour, object : ArrayResponse<Seat>{
            override fun onSuccess(datas: List<Seat>?) {
                hideLoading()
                datas?.let { seats.postValue(it) }
            }

            override fun onFailure(err: Error?) {
                hideLoading()
                err?.let { toast(it.message.toString()) }
            }
        })
    }

    fun addSelectedSeat(s : Seat){
        val tempSelectedSeat = if(seats.value == null){
            mutableListOf()
        } else {
            seats.value as MutableList<Seat>
        }
        val sameSeat = tempSelectedSeat.find { same ->same.id == s.id }
        sameSeat?.let { seat ->
            seat.selected = true
        }
        seats.postValue(tempSelectedSeat)
    }

    fun removeSelectedSeat(s : Seat) {
        val tempSelectedSeat = if(seats.value == null){
            mutableListOf()
        } else {
            seats.value as MutableList<Seat>
        }
        val sameSeat = tempSelectedSeat.find { same ->same.id == s.id }
        sameSeat?.let { seat ->
            seat.selected = false
        }
        seats.postValue(tempSelectedSeat)
    }

    fun listenToState() = state
    fun listenToSeats() = seats
}

sealed class SeatState{
    data class Loading(var state : Boolean = false) : SeatState()
    data class ShowToast(var message : String) : SeatState()
}