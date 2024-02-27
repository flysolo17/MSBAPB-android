package com.danica.msbapb.views.bottom.map


import android.content.Context
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.danica.msbapb.R
import com.danica.msbapb.databinding.FragmentMapBinding
import com.danica.msbapb.models.Locations
import com.danica.msbapb.models.LocationsWithDistance
import com.danica.msbapb.repository.TAG
import com.danica.msbapb.utils.UiState
import com.danica.msbapb.viewmodels.LocationViewModels
import com.danica.msbapb.views.adapters.LocationAdapter
import com.danica.msbapb.views.adapters.LocationAdapterClickListener
import com.danica.msbapb.views.bottom.LocationBottomSheet
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.awaitMapLoad
import com.google.maps.android.ktx.cameraIdleEvents
import com.google.maps.android.ktx.cameraMoveStartedEvents
import kotlinx.coroutines.launch
import java.lang.Math.atan2
import java.lang.Math.cos
import java.lang.Math.sin
import java.lang.Math.sqrt


class MapFragment : Fragment() ,LocationAdapterClickListener{

    private lateinit var _binding : FragmentMapBinding

    private val binalonan = LatLng(  16.05030000   , 120.59260000 )
    private val _locationViewModel by activityViewModels<LocationViewModels>()
    private lateinit var _mapFragment : SupportMapFragment
    private lateinit var _fusedLocationClient: FusedLocationProviderClient
    private lateinit var _locationsAdapter : LocationAdapter
    private var locations : List<Locations> = listOf()
    private var myCurrentLocation : LatLng ? = null
    private val locationPermissionRequest = this.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
            } else -> {
            checkPermision()
        }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater,container,false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val isRestore = savedInstanceState != null
        super.onViewCreated(view, savedInstanceState)
        _locationsAdapter = LocationAdapter(view.context, emptyList(),this);
        _binding.recyclerviewLocations.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = _locationsAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        observers()
         _mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        _fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

//        lifecycle.coroutineScope.launch {
//            val googleMap = _mapFragment.awaitMap()
//
//            googleMap.addMarker(
//                MarkerOptions()
//                    .position(binalonan)
//                    .title("Binalonan")
//            )
//
//            if (!isRestore) {
//                googleMap.awaitMapLoad()
//                googleMap.animateCamera(
//                    CameraUpdateFactory.newLatLngZoom(
//                        binalonan,
//                        10F
//                    )
//                )
//            }
//            launch {
//                googleMap.cameraMoveStartedEvents().collect {
//                    Log.d(TAG, "Camera moved - reason $it")
//                }
//            }
//            launch {
//                googleMap.cameraIdleEvents().collect {
//                    Log.d(TAG, "Camera is idle.")
//                }
//            }
//        }


        lifecycle.coroutineScope.launch {
            val googleMap = _mapFragment.awaitMap()
            addCurrentLocationMarker(googleMap)
            moveCameraToCurrentLocation(googleMap)
            addOtherLocationMarkers(googleMap)
        }

        val bottomSheet = BottomSheetBehavior.from(_binding.dragableView)
        bottomSheet.setPeekHeight(400,true)
        bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED

    }


    private fun checkPermision() {
        if (ActivityCompat.checkSelfPermission(
                _binding.root.context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                _binding.root.context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {


            locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION))
            return
        }
    }
    private fun observers() {
        _locationViewModel.locations.observe(viewLifecycleOwner) {
            if (it is UiState.SUCCESS) {
                lifecycle.coroutineScope.launch {
                    val googleMap = _mapFragment.awaitMap()
                    it.data.data.forEach {
                        val latLng = LatLng(it.latitude.toDoubleOrNull() ?: 0.00,it.longitude.toDoubleOrNull() ?: 0.00)
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title(it.name)
                        )
                    }

                }
            }
        }
    }



    private fun getDistance(latLng1: LatLng, latLng2: LatLng): Double {
        val R = 6371 // Radius of the Earth in kilometers
        val latDistance = Math.toRadians(latLng2.latitude - latLng1.latitude)
        val lonDistance = Math.toRadians(latLng2.longitude - latLng1.longitude)
        val a = kotlin.math.sin(latDistance / 2) * kotlin.math.sin(latDistance / 2) +
                kotlin.math.cos(Math.toRadians(latLng1.latitude)) * kotlin.math.cos(
            Math.toRadians(
                latLng2.latitude
            )
        ) *
                kotlin.math.sin(lonDistance / 2) * kotlin.math.sin(lonDistance / 2)
        val c = 2 * kotlin.math.atan2(kotlin.math.sqrt(a), kotlin.math.sqrt(1 - a))
        val distance = R * c

        // Format the distance to have only two decimal places
        return "%.2f".format(distance).toDouble()
    }

    private fun addCurrentLocationMarker(googleMap: GoogleMap) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {


            return
        }
        _fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                val locationsWithDistance : MutableList<LocationsWithDistance> = mutableListOf()
                location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title("My Location")
                    )
                    myCurrentLocation = latLng
                    locations.forEach {
                        val  latLng2 = LatLng(it.latitude.toDoubleOrNull()?: 0.0,it.longitude.toDoubleOrNull() ?: 0.00)
                        locationsWithDistance.add(LocationsWithDistance(it,"${getDistance(latLng,latLng2)}km"))
                    }

                    _locationsAdapter.updateLocations(locationsWithDistance)
                }
            }
    }

    private fun moveCameraToCurrentLocation(googleMap: GoogleMap) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            checkPermision()
            return
        }
        _fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    googleMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            latLng,
                            DEFAULT_ZOOM.toFloat()
                        )
                    )
                }
            }
    }

    private fun addOtherLocationMarkers(googleMap: GoogleMap) {
        _locationViewModel.locations.observe(viewLifecycleOwner) { uiState ->
            if (uiState is UiState.SUCCESS) {
                locations = uiState.data.data
                uiState.data.data.forEach { location ->
                    val latLng =
                        LatLng(
                            location.latitude.toDoubleOrNull() ?: 0.0,
                            location.longitude.toDoubleOrNull() ?: 0.0
                        )
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title(location.name)
                    )
                }
            }
        }
    }

    companion object {
        private const val DEFAULT_ZOOM = 15
    }

    private fun isLocationServiceEnabled() : Boolean {
        val locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    override fun onCall(locationsWithDistance: LocationsWithDistance) {
        sendCallIntent(locationsWithDistance.locations.contact)
    }

    override fun onLocate(locationsWithDistance: LocationsWithDistance) {
        openGoogleMap(locationsWithDistance.locations.latitude.toDoubleOrNull() ?: 0.0,locationsWithDistance.locations.longitude.toDoubleOrNull() ?: 0.0)
    }


    private fun sendCallIntent(data: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$data")
        startActivity(intent)
    }

    private fun openGoogleMap(latitude : Double, longitude : Double) {
        if (myCurrentLocation ===  null) {
            val uri = "geo:${latitude},${longitude}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            intent.setPackage("com.google.android.apps.maps")
            startActivity(intent)
        } else {

            val uri = "https://www.google.com/maps/dir/?api=1&origin=${myCurrentLocation?.latitude},${myCurrentLocation?.longitude}&destination=${latitude},${longitude}&travelmode=driving&dir_action=navigate"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            startActivity(intent)
        }

    }

}