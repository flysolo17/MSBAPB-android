package com.danica.msbapb

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.danica.msbapb.databinding.ActivityMainBinding
import com.danica.msbapb.utils.UiState

import com.danica.msbapb.viewmodels.AuthViewModel
import com.danica.msbapb.viewmodels.LocationViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.prefs.Preferences
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding :ActivityMainBinding

    private val authViewModel by viewModels<AuthViewModel>()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val locationViewModel by  viewModels<LocationViewModels>()
    private var locationUpdatesJob: Job? = null
    private var userID : Int? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationViewModel.getAllLocations()
        locationUpdatesJob =  lifecycleScope.launch {
            authViewModel.authRepository.getUID()
                .catch {
                   Toast.makeText(this@MainActivity, "No user Found!", Toast.LENGTH_LONG).show()
                }
                .collect {
                    if (it != 0) {
                        authViewModel.getUserProfile(it)
                    } else {
                        Toast.makeText(this@MainActivity,"user not found!",Toast.LENGTH_LONG).show()
                        finish()
                    }
            }
        }
        observers()
    }


    private fun observers() {
        authViewModel.users.observe(this) {
            when(it) {
                is UiState.FAILED -> Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
                is UiState.LOADING -> {}
                is UiState.SUCCESS -> {
                    val toolbar = binding.appBarMain.toolbar
                    setSupportActionBar(toolbar)
                    setUpNav()
                }
            }
        }

    }
    private fun setUpNav() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.bottom_map,
                R.id.bottom_news,
                R.id.bottom_library,
                R.id.bottom_logout,
                R.id.menu_profile,
                R.id.menu_incident,
                R.id.menu_about,
                R.id.bottom_logout,
            ),binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navigation.setupWithNavController(navController)
        binding.appBarMain.bottomNavigation.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _: NavController?, destination: NavDestination, _: Bundle? ->
            when (destination.id) {
                R.id.bottom_map -> {
                    showBottomNav()
                    invalidateOptionsMenu()
                }
                R.id.bottom_news -> {
                    showBottomNav()
                    invalidateOptionsMenu()
                }
                R.id.bottom_library -> {
                    showBottomNav()
                    invalidateOptionsMenu()
                }
                R.id.bottom_logout -> {
                    showBottomNav()
                    invalidateOptionsMenu()
                }
                else -> {
                    hideBottomNav()
                    invalidateOptionsMenu()
                }
            }
        }

    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun showBottomNav() {
        binding.appBarMain.bottomAppbar.performShow(true)
        binding.appBarMain.bottomAppbar.hideOnScroll = true
    }

    private fun hideBottomNav() {
        binding.appBarMain.bottomAppbar.performHide(true)
        binding.appBarMain.bottomAppbar.hideOnScroll = false
    }



    override fun onStop() {
        locationUpdatesJob?.cancel()
        super.onStop()
    }
}