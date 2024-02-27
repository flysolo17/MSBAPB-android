package com.danica.msbapb.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.danica.msbapb.R

class CarouselAdapter(private val context: Context, private  val images : List<Int>) : RecyclerView.Adapter<CarouselAdapter.ImageViewHolder>() {
    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageview : ImageView = itemView.findViewById(R.id.list_item_image)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_carousel,parent,false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.imageview.setImageResource(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }

}