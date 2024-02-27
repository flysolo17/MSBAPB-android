package com.danica.msbapb.views.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.danica.msbapb.R
import com.danica.msbapb.models.Personels
import com.danica.msbapb.utils.STORAGE_LINK


class PersonelAdapter(private  val context: Context,private  val  personels : List<Personels>): RecyclerView.Adapter<PersonelAdapter.PersonelViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonelViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_personels,parent,false)
        return PersonelViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  personels.size
    }

    override fun onBindViewHolder(holder: PersonelViewHolder, position: Int) {
        val personel = personels[position]
        holder.textPersonelName.text = personel.name
        holder.textPersonelContact.text = personel.contact

        Log.d("personels","${STORAGE_LINK}/${personel.photo}")
        Glide.with(context)
            .load("${STORAGE_LINK}/${personel.photo}")
            .error(R.drawable.baseline_person_2_24)
            .into(holder.imagePersonel)
        holder.viewPersonelStatus.backgroundTintList = ColorStateList.valueOf(
            if (personel.active == 1) Color.GREEN else Color.GRAY
        )

    }
    class PersonelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imagePersonel : ImageView;
        var textPersonelName : TextView
        var textPersonelContact : TextView
        var viewPersonelStatus : View

        init {

            imagePersonel = itemView.findViewById(R.id.imagePersonel)
            textPersonelName = itemView.findViewById(R.id.textPersonelName)
            textPersonelContact = itemView.findViewById(R.id.textContact)
            viewPersonelStatus = itemView.findViewById(R.id.textStatus)
        }
    }
}