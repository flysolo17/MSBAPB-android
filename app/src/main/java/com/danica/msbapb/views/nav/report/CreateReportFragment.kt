package com.danica.msbapb.views.nav.report

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.danica.msbapb.databinding.FragmentCreateReportBinding
import com.danica.msbapb.models.LocationsWithDistance
import com.danica.msbapb.models.User
import com.danica.msbapb.utils.UiState
import com.danica.msbapb.viewmodels.AuthViewModel
import com.danica.msbapb.viewmodels.IncidentReportViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date


private val incidentTypes = arrayOf("Accident", "Fire", "Robbery", "Medical Emergency", "Natural Disaster","Other")
private val incidentSeverity= arrayOf("LOW", "MEDIUM", "HIGH")
class CreateReportFragment : Fragment() {
    private lateinit var _binding : FragmentCreateReportBinding
    private val incidentReportViewModel by activityViewModels<IncidentReportViewModel>()
    private val _authViewModel by  activityViewModels<AuthViewModel>()
    private var _user : User ? = null
    private lateinit var _fusedLocationClient: FusedLocationProviderClient
    private var imageUri : Uri ?  = null;
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
    private val launcher = registerForActivityResult<PickVisualMediaRequest, Uri>(
        ActivityResultContracts.PickVisualMedia(),
        object : ActivityResultCallback<Uri?> {
            override fun onActivityResult(result: Uri?) {
                result?.let {
                    imageUri = it
                     Glide.with(_binding.root.context).load(imageUri).into(_binding.imagePick)
                } ?: run {
                    Toast.makeText(_binding.root.context, "No image Selected", Toast.LENGTH_SHORT)
                }

            }
        })

    lateinit var currentPhotoPath: String



    private val cameraActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.data != null) {
            val bitmap = result.data!!.extras!!.get("data") as Bitmap?

            if (bitmap != null) {
                imageUri = bitmapToUri(bitmap)
                Glide.with(_binding.root.context).load(imageUri).into(_binding.imagePick)

            }
        }
    }

    private fun bitmapToUri(bitmap: Bitmap): Uri {
        val imagesDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File(imagesDir, "image_${System.currentTimeMillis()}.jpg")

        FileOutputStream(imageFile).use { fos ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        }

        // Use FileProvider to generate a content URI for the image file
        return FileProvider.getUriForFile(
            requireContext(),
            "com.example.android.fileprovider", // Replace with your FileProvider authority
            imageFile
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateReportBinding.inflate(inflater,container,false)
        val adapter = ArrayAdapter(_binding.root.context, android.R.layout.simple_dropdown_item_1line, incidentTypes)
        _binding.autoCompleteTextView.setAdapter(adapter)

        val adapter2 = ArrayAdapter(_binding.root.context, android.R.layout.simple_dropdown_item_1line, incidentSeverity)
        _binding.inputSeverity.setAdapter(adapter2)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        _authViewModel.users.observe(viewLifecycleOwner) {
            if (it is UiState.SUCCESS) {
                _user = it.data
            }
        }

        _binding.pickImage.setOnClickListener {
            launcher.launch(PickVisualMediaRequest.Builder().setMediaType(ImageOnly).build())
        }
        _binding.captureImage.setOnClickListener {
            launchCamera(_binding.root.context)
        }



        _binding.buttonSaveReport.setOnClickListener {
            val reporterID = _user?.id ?: 0
            val location = _binding.inputLocation.text.toString()
            val description = _binding.inputDescription.text.toString()
            val type = _binding.autoCompleteTextView.text.toString()
            val severity = _binding.inputSeverity.text.toString().getServerity()
            if (location.isEmpty()) {
                _binding.layoutLocation.error = "Enter location"
                return@setOnClickListener
            }
            if (description.isEmpty()) {
                _binding.layoutDescription.error = "Enter description"
                return@setOnClickListener
            }
            if (type.isEmpty()) {
                _binding.layoutType.error = "Enter incident Type"
                return@setOnClickListener
            }
            if (severity <= 0) {
                _binding.layoutSeverity.error = "Enter severity"
                return@setOnClickListener
            }

            getCurrentLocation(reporterID,location,description,type,severity,imageUri)

        }

    }
    fun getCurrentLocation(reporterID: Int, location: String, description: String, type: String, severity: Int, imageUri: Uri?) {
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
            .addOnSuccessListener { locations ->
                saveReport(_binding.root.context,reporterID, location, description, type, severity, imageUri,locations.latitude,locations.longitude)
        }
    }

    companion object {

        const val  IMAGE_SIZE = 224
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



    private fun saveReport(context: Context, reporterID: Int, location: String, description: String, type: String, severity: Int, imageUri: Uri?,lat : Double,lang : Double) {

        lifecycleScope.launch {
            try {
                val requestBody = imageUri?.let {
                    context.contentResolver.openInputStream(it)?.use { inputStream ->
                        inputStream.readBytes().toRequestBody("image/*".toMediaTypeOrNull())
                    }
                }

                // Convert RequestBody to MultipartBody.Part
                val filePart = requestBody?.let {
                    MultipartBody.Part.createFormData("photo", "incident_photo.jpg", it)
                }

                if (filePart != null) {
                    incidentReportViewModel.incidentReportRepository.createIncidentReport(
                        reporterID, location, type, description, severity, filePart,lat,lang
                    ) { result ->
                        when (result) {
                            is UiState.FAILED -> {
                                progress(false)
                                Toast.makeText(context,result.message,Toast.LENGTH_LONG).show()
                            }
                            is UiState.LOADING -> progress(true)
                            is UiState.SUCCESS -> {
                                progress(false)
                                Toast.makeText(context, result.data.message, Toast.LENGTH_LONG).show()
                                findNavController().popBackStack()
                            }
                        }
                    }
                } else {
                    // Handle the case where filePart is null
                    Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                // Handle any potential IO errors, maybe show a toast or log the error
                Toast.makeText(context, "Error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun progress(status : Boolean) {
        if (status) {
            _binding.progressCircular.visibility = View.VISIBLE
            _binding.buttonSaveReport.visibility = View.GONE
        } else {
            _binding.progressCircular.visibility = View.GONE
            _binding.buttonSaveReport.visibility = View.VISIBLE
        }
    }


    private fun String.getServerity() : Int {
        return if (this == "LOW") {
            1
        } else if (this == "MEDIUM") {
            2
        } else {
            4
        }
    }
    private fun launchCamera(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(context.packageManager) != null) {
                cameraActivityResultLauncher.launch(intent)
            }

        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraActivityResultLauncher.launch(intent)
        }
    }

}