package com.danica.msbapb.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.danica.msbapb.R

class GalleryAdapter(private val context: Context,private  val images : List<Int>): RecyclerView.Adapter<GalleryAdapter.GalleryViewHoler>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GalleryAdapter.GalleryViewHoler {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_gallery,parent,false)
        return  GalleryViewHoler(view);
    }

    override fun onBindViewHolder(holder: GalleryAdapter.GalleryViewHoler, position: Int) {
        val image = images[position]
        holder.imageGallery.setImageResource(image);
    }

    override fun getItemCount(): Int {
        return  images.size
    }
    class GalleryViewHoler(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageGallery : ImageView;
        init {
            imageGallery = itemView.findViewById(R.id.imageGallery);
        }
    }
}