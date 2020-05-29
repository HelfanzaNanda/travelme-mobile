package com.travelme.customer.viewmodels

import androidx.lifecycle.ViewModel
import com.mapbox.api.geocoding.v5.GeocodingCriteria
import com.mapbox.api.geocoding.v5.MapboxGeocoding
import com.mapbox.api.geocoding.v5.models.GeocodingResponse
import com.mapbox.geojson.Point
import com.travelme.customer.utilities.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel : ViewModel(){
    private var state: SingleLiveEvent<MapsState> = SingleLiveEvent()

    fun reverseGeocode(point : Point, token : String){
        println("point ${point}")
        println(token)
        val client = MapboxGeocoding.builder()
            .accessToken(token)
            .query(Point.fromLngLat(point.longitude(), point.latitude()))
            .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
            .build()

        client.enqueueCall(object : Callback<GeocodingResponse>{
            override fun onFailure(call: Call<GeocodingResponse>, t: Throwable) {
                println("OnFailure : ${t.message}")
            }

            override fun onResponse(call: Call<GeocodingResponse>, response: Response<GeocodingResponse>) {
                if (response.isSuccessful){
                    println("response sukses ${response.message()}")
                    val body = response.body()
                    val results = body!!.features()
                    val features = results.get(0)
                    state.value = MapsState.ShowToast(features.placeName()!!)
                    println("result ${features}")
                }else{
                    println("response not response ${response.message()}")
                    state.value = MapsState.ShowToast("response not response ${response.message()}")
                }
            }

        })
    }

    fun getState() = state
}

sealed class MapsState{
    data class IsLoading(var state: Boolean = false) : MapsState()
    data class ShowToast(var message: String) : MapsState()
}