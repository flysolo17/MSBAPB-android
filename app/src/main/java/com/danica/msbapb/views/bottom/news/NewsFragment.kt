package com.danica.msbapb.views.bottom.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.danica.msbapb.R
import com.danica.msbapb.databinding.FragmentNewsBinding
import com.danica.msbapb.models.News
import com.danica.msbapb.utils.UiState
import com.danica.msbapb.viewmodels.NewsViewModel
import com.danica.msbapb.views.adapters.NewsAdapter
import com.danica.msbapb.views.adapters.NewsClickListener


class NewsFragment : Fragment() ,NewsClickListener {

    private lateinit var _binding : FragmentNewsBinding
    private val _newsViewModel by activityViewModels<NewsViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsBinding.inflate(inflater,container,false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _newsViewModel.getAllNews()
        observers()
    }

    private fun observers() {
        _newsViewModel.news.observe(viewLifecycleOwner) {
            if (it is UiState.SUCCESS) {
                _binding.recyclerviewNews.apply {
                    layoutManager = LinearLayoutManager(_binding.root.context)
                    adapter = NewsAdapter(_binding.root.context,it.data.data,this@NewsFragment)
                }
            }
        }
    }

    override fun onNewsClicked(news: News) {
        val directions = NewsFragmentDirections.actionBottomNewsToViewNewsFragment(news)
        findNavController().navigate(directions)
    }

}