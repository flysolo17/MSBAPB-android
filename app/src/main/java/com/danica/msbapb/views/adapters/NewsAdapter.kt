package com.danica.msbapb.views.adapters

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.danica.msbapb.R
import com.danica.msbapb.models.News
import com.danica.msbapb.models.Personels
import com.danica.msbapb.utils.STORAGE_LINK
import com.danica.msbapb.utils.formatDate
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface NewsClickListener {
    fun onNewsClicked(news: News)
}

class NewsAdapter(private  val context: Context, private  val  news : List<News>,private val newsClickListener : NewsClickListener): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_news,parent,false)
        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  news.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val new = news[position]
        holder.textTitle.text = new.title
        holder.textDescription.text = new.description
        holder.textDate.text = new.createdAt.formatDate()

        Glide.with(context)
            .load("${STORAGE_LINK}/${new.photo}")
            .error(R.drawable.news)
            .into(holder.imageNews)

        holder.btnReadMore.setOnClickListener {
            newsClickListener.onNewsClicked(new)
        }
    }





    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageNews : ShapeableImageView;
        var textTitle : TextView
        var textDescription : TextView
        var textDate : TextView
        var btnReadMore : MaterialButton
        init {

            imageNews = itemView.findViewById(R.id.imageNews)
            textTitle = itemView.findViewById(R.id.texttitle)
            textDescription = itemView.findViewById(R.id.textDescription)
            textDate = itemView.findViewById(R.id.textDate)
            btnReadMore = itemView.findViewById(R.id.btnReadMore)
        }
    }
}