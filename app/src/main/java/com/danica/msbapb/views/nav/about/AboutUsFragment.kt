package com.danica.msbapb.views.nav.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.danica.msbapb.R
import com.danica.msbapb.databinding.FragmentAboutUsBinding
import com.danica.msbapb.views.adapters.CarouselAdapter
import com.danica.msbapb.views.adapters.GalleryAdapter


val _gallery = listOf<Int>(R.drawable.action1,R.drawable.action2,R.drawable.action3,R.drawable.action4)
class AboutUsFragment : Fragment() {
    private lateinit var _binding : FragmentAboutUsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAboutUsBinding.inflate(layoutInflater,container,false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding.carousel.apply {
            adapter = CarouselAdapter(view.context, listOf(R.drawable.bfp1,R.drawable.bfp2))
        }
        _binding.recyclerviewGallery.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = GalleryAdapter(view.context, _gallery)
        }
    }


}