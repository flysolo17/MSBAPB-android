package com.danica.msbapb.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.danica.msbapb.R
import com.danica.msbapb.models.LocationsWithDistance
import com.google.android.material.button.MaterialButton
import org.w3c.dom.Text


interface LocationAdapterClickListener {
    fun onCall(locationsWithDistance: LocationsWithDistance)
    fun onLocate(locationsWithDistance: LocationsWithDistance)
}
class LocationAdapter(private  val context: Context,private  var locationsWithDistance: List<LocationsWithDistance>,private  val locationAdapterClickListener: LocationAdapterClickListener): RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LocationAdapter.LocationViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_locations,parent,false)
        return LocationViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocationAdapter.LocationViewHolder, position: Int) {
        val location = locationsWithDistance[position]
        holder.textLocation.text = location.locations.name
        holder.textContact.text = location.locations.contact
        holder.textDistance.text = location.distance
        holder.buttonLocation.setOnClickListener {
            locationAdapterClickListener.onLocate(location)
        }
        holder.buttonContact.setOnClickListener {
            locationAdapterClickListener.onCall(location)
        }
    }

    fun updateLocations(locationsWithDistance: List<LocationsWithDistance>) {
        this.locationsWithDistance = locationsWithDistance
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return  locationsWithDistance.size
    }
    class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textLocation : TextView;
        var textContact : TextView
        var textDistance : TextView
        var buttonLocation : MaterialButton
        var buttonContact : MaterialButton
        init {

            textLocation = itemView.findViewById(R.id.textLocation)
            textContact = itemView.findViewById(R.id.textContact)
            textDistance = itemView.findViewById(R.id.textDistance)
            buttonLocation = itemView.findViewById(R.id.buttonLocation)
            buttonContact = itemView.findViewById(R.id.buttonContact)
        }
    }

}