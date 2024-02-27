package com.danica.msbapb.views.bottom.library

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.danica.msbapb.R
import com.danica.msbapb.databinding.FragmentLibraryBinding
import com.danica.msbapb.utils.UiState
import com.danica.msbapb.viewmodels.PersonelViewModel
import com.danica.msbapb.views.adapters.CarouselAdapter
import com.danica.msbapb.views.adapters.GalleryAdapter
import com.danica.msbapb.views.adapters.PersonelAdapter


class LibraryFragment : Fragment() {

    private lateinit var _binding : FragmentLibraryBinding
    private  val _personelViewModel by  activityViewModels<PersonelViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater,container,false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _personelViewModel.getAllPersonels()
        observers()
    }
    private fun observers() {
        _personelViewModel.personels.observe(viewLifecycleOwner) {
            if (it is UiState.SUCCESS) {

                _binding.recyclerviewPNP.apply {
                    layoutManager = GridLayoutManager(_binding.root.context,2)
                    adapter =PersonelAdapter(_binding.root.context,it.data.filter { it.type == "PNP" })
                }
                _binding.recyclerviewBFP.apply {
                    layoutManager = GridLayoutManager(_binding.root.context,2)
                    adapter =PersonelAdapter(_binding.root.context,it.data.filter { it.type == "BFP" })
                }
            }
        }
    }
}