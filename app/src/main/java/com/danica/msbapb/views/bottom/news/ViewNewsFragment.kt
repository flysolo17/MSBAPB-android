package com.danica.msbapb.views.bottom.news

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.danica.msbapb.R
import com.danica.msbapb.databinding.FragmentViewNewsBinding
import com.danica.msbapb.models.News
import com.danica.msbapb.utils.STORAGE_LINK


class ViewNewsFragment : Fragment() {

    private lateinit var _binding : FragmentViewNewsBinding
    private var news : News ? = null
    private val args by navArgs<ViewNewsFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            news = args.news
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewNewsBinding.inflate(inflater,container,false)
        news?.let {
            Glide.with(_binding.root.context).load("$STORAGE_LINK/${it.photo}").error(R.drawable.news).into(_binding.imageNews)
            _binding.texttitle.text = it.title
            _binding.textDescription.text = it.description
            _binding.textDate.text = it.createdAt
        }
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding.btnReadMore.setOnClickListener {
            news?.let {m->
                openLink(_binding.root.context,m.link)
            }

        }
    }


    private fun openLink(context: Context, link: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        context.startActivity(intent)
    }
}