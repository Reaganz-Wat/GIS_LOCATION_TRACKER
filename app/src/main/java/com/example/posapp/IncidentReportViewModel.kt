//package com.example.posapp
//
//import android.Manifest
//import android.app.Application
//import android.content.Context
//import android.content.pm.PackageManager
//import android.location.Geocoder
//import android.location.Location
//import android.net.Uri
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.annotation.RequiresPermission
//import androidx.core.app.ActivityCompat
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationCallback
//import com.google.android.gms.location.LocationRequest
//import com.google.android.gms.location.LocationResult
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.location.Priority
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import java.time.LocalDateTime
//import java.time.format.DateTimeFormatter
//import java.util.Locale
//
//@RequiresApi(Build.VERSION_CODES.O)
//data class IncidentFormState @RequiresApi(Build.VERSION_CODES.O) constructor(
//    val datetime: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
//    val imageUri: Uri? = null,
//    val latitude: String = "",
//    val longitude: String = "",
//    val altitude: String = "",
//    val accuracy: String = "",
//    val city: String = "",     // Added city field
//    val division: String = "",
//    val ward: String = "",
//    val cell: String = "",
//    val street: String = "",
//    val otherStreet: String = "",
//    val incidentType: String = "",
//    val otherIncidentType: String = "",
//    val incidentDetails: String = "",
//    val isSubmitting: Boolean = false,
//    val isSuccess: Boolean = false,
//    val errorMessage: String? = null,
//    val isLocationLoading: Boolean = false,
//    val showDivisionDropdown: Boolean = false,
//    val showStreetDropdown: Boolean = false,
//    val showIncidentTypeDropdown: Boolean = false,
//    val showCityDropdown: Boolean = false  // Added flag for city dropdown
//)
//
//class IncidentReportViewModel(application: Application) : AndroidViewModel(application) {
//
//    private val repository = IncidentRepository(application.applicationContext)
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private val _formState = MutableStateFlow(IncidentFormState())
//    @RequiresApi(Build.VERSION_CODES.O)
//    val formState: StateFlow<IncidentFormState> = _formState.asStateFlow()
//
//    private lateinit var fusedLocationClient: FusedLocationProviderClient
//    private var locationCallback: LocationCallback? = null
//
//    init {
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplication())
//    }
//
//    // Division options from PDF
//    val divisionOptions = listOf("Ayivu Division", "Central Division")
//
//    // Street options from PDF
//    val streetOptions = listOf(
//        "Arua Avenue", "Hospital Road", "Adumi Road", "Duka Road", "Market Lane",
//        "Rhino-camp road", "Arua - Packwach road", "Onduparaka Road", "Wadrif Road",
//        "Mango Road", "School Road", "Weatherhead Park Lane", "Mvaradri - Oluko road",
//        "Ediofe Road", "Muni University road", "Others"
//    )
//
//    // Incident types from PDF
////    val incidentTypes = listOf(
////        "Wrong Parking", "Congested roads", "Road Accident Incidence", "Reckless Driving incidence",
////        "Road condition", "Offloading in non gazetted area", "Other"
////    )
//
//    val incidentTypes = listOf(
//        "Traffic Flow & Congestion",
//        "Road & Infrastructure Condition",
//        "Accidents & Safety",
//        "Public Transport & Mobility",
//        "Environmental & Hazards",
//        "Law & Order Issues",
//        "Other"
//    )
//
//    // List of Cities
//    val cities = listOf("Arua City")
//
//    // Add update function for city
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun updateCity(city: String) {
//        _formState.update { it.copy(city = city) }
//    }
//
//    // Add toggle function for city dropdown
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun toggleCityDropdown() {
//        _formState.update { it.copy(
//            showCityDropdown = !it.showCityDropdown,
//            showDivisionDropdown = false,
//            showStreetDropdown = false,
//            showIncidentTypeDropdown = false
//        ) }
//    }
//
//    // Update hideAllDropdowns to include city dropdown
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun hideAllDropdowns() {
//        _formState.update { it.copy(
//            showDivisionDropdown = false,
//            showStreetDropdown = false,
//            showIncidentTypeDropdown = false,
//            showCityDropdown = false
//        ) }
//    }
//
//    // Update other toggle functions to close city dropdown when they open
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun toggleDivisionDropdown() {
//        _formState.update { it.copy(
//            showDivisionDropdown = !it.showDivisionDropdown,
//            showStreetDropdown = false,
//            showIncidentTypeDropdown = false,
//            showCityDropdown = false
//        ) }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun updateDatetime(datetime: String) {
//        _formState.update { it.copy(datetime = datetime) }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun updateImage(uri: Uri?) {
//        _formState.update { it.copy(imageUri = uri) }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun updateLatitude(latitude: String) {
//        _formState.update { it.copy(latitude = latitude) }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun updateLongitude(longitude: String) {
//        _formState.update { it.copy(longitude = longitude) }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun updateAltitude(altitude: String) {
//        _formState.update { it.copy(altitude = altitude) }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun updateAccuracy(accuracy: String) {
//        _formState.update { it.copy(accuracy = accuracy) }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun updateDivision(division: String) {
//        _formState.update { it.copy(division = division) }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun updateWard(ward: String) {
//        _formState.update { it.copy(ward = ward) }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun updateCell(cell: String) {
//        _formState.update { it.copy(cell = cell) }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun updateStreet(street: String) {
//        _formState.update { it.copy(street = street) }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun updateOtherStreet(otherStreet: String) {
//        _formState.update { it.copy(otherStreet = otherStreet) }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun updateIncidentType(incidentType: String) {
//        _formState.update { it.copy(incidentType = incidentType) }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun updateOtherIncidentType(otherIncidentType: String) {
//        _formState.update { it.copy(otherIncidentType = otherIncidentType) }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun updateIncidentDetails(details: String) {
//        _formState.update { it.copy(incidentDetails = details) }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun updateLocation(latitude: String, longitude: String, altitude: String, accuracy: String) {
//        _formState.update {
//            it.copy(
//                latitude = latitude,
//                longitude = longitude,
//                altitude = altitude,
//                accuracy = accuracy,
//                isLocationLoading = false
//            )
//        }
//    }
//
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun toggleStreetDropdown() {
//        _formState.update { it.copy(
//            showStreetDropdown = !it.showStreetDropdown,
//            showDivisionDropdown = false,
//            showIncidentTypeDropdown = false
//        ) }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun toggleIncidentTypeDropdown() {
//        _formState.update { it.copy(
//            showIncidentTypeDropdown = !it.showIncidentTypeDropdown,
//            showDivisionDropdown = false,
//            showStreetDropdown = false
//        ) }
//    }
//
////    @RequiresApi(Build.VERSION_CODES.O)
////    fun hideAllDropdowns() {
////        _formState.update { it.copy(
////            showDivisionDropdown = false,
////            showStreetDropdown = false,
////            showIncidentTypeDropdown = false
////        ) }
////    }
//
//    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
//    @RequiresApi(Build.VERSION_CODES.O)
////    fun startLocationDetection() {
////        _formState.update { it.copy(isLocationLoading = true) }
////        // You will implement the actual location detection
////        // This is just a placeholder function that you'll replace
////        detectLocation()
////    }
//
//    // In your IncidentReportViewModel
//
////    fun startLocationDetection(context: Context) {
////        // Check if permission is granted before accessing location
////        if (ActivityCompat.checkSelfPermission(
////                context,
////                Manifest.permission.ACCESS_FINE_LOCATION
////            ) != PackageManager.PERMISSION_GRANTED &&
////            ActivityCompat.checkSelfPermission(
////                context,
////                Manifest.permission.ACCESS_COARSE_LOCATION
////            ) != PackageManager.PERMISSION_GRANTED
////        ) {
////            // Permission not granted - update state to stop loading
////            _formState.update { it.copy(isLocationLoading = false) }
////            return
////        }
////
////        // Set loading state
////        _formState.update { it.copy(isLocationLoading = true) }
////
////        // Your location detection code here
////        fusedLocationClient.lastLocation
////            .addOnSuccessListener { location: Location? ->
////                location?.let {
////                    _formState.update { state ->
////                        state.copy(
////                            latitude = it.latitude.toString(),
////                            longitude = it.longitude.toString(),
////                            altitude = it.altitude.toString(),
////                            accuracy = it.accuracy.toString(),
////                            isLocationLoading = false
////                        )
////                    }
////                } ?: run {
////                    _formState.update { it.copy(isLocationLoading = false) }
////                }
////            }
////            .addOnFailureListener {
////                _formState.update { it.copy(isLocationLoading = false) }
////            }
////    }
//
////    @RequiresApi(Build.VERSION_CODES.O)
////    private fun detectLocation() {
////        // This is a placeholder function that you'll implement later
////        // with your actual location detection logic
////        viewModelScope.launch {
////            // Simulate a delay for location detection
////            kotlinx.coroutines.delay(1000)
////
////            // For now, just use placeholder values
////            updateLocation(
////                latitude = "0.2959",
////                longitude = "32.6122",
////                altitude = "1200",
////                accuracy = "5"
////            )
////        }
////    }
//
////    @RequiresApi(Build.VERSION_CODES.O)
////    fun submitForm() {
////        viewModelScope.launch {
////            _formState.update { it.copy(isSubmitting = true, errorMessage = null) }
////
////            try {
////                // Here you will implement your repository call to save the data
////                // For now, we'll simulate a successful submission after a delay
////                saveIncidentToDatabase()
////
////                // Simulate API call delay
////                kotlinx.coroutines.delay(1500)
////
////                _formState.update { it.copy(isSubmitting = false, isSuccess = true) }
////            } catch (e: Exception) {
////                _formState.update {
////                    it.copy(
////                        isSubmitting = false,
////                        errorMessage = e.message ?: "An unknown error occurred"
////                    )
////                }
////            }
////        }
////    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun submitForm() {
//        viewModelScope.launch {
//            _formState.update { it.copy(isSubmitting = true, errorMessage = null) }
//
//            try {
//                // Call repository to submit the form
//                val result = repository.submitIncidentReport(formState.value)
//
//                if (result.isSuccess) {
//                    _formState.update { it.copy(isSubmitting = false, isSuccess = true) }
//                } else {
//                    val exception = result.exceptionOrNull() ?: Exception("Unknown error occurred")
//                    _formState.update {
//                        it.copy(
//                            isSubmitting = false,
//                            errorMessage = exception.message ?: "An unknown error occurred"
//                        )
//                    }
//                }
//            } catch (e: Exception) {
//                _formState.update {
//                    it.copy(
//                        isSubmitting = false,
//                        errorMessage = e.message ?: "An unknown error occurred"
//                    )
//                }
//            }
//        }
//    }
//
//    private suspend fun saveIncidentToDatabase() {
//        // You will implement this function with your repository
//        // This is just a placeholder that you'll replace with actual implementation
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun resetForm() {
//        _formState.update {
//            IncidentFormState(
//                datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
//            )
//        }
//    }
//
//    suspend fun login(email: String, password: String): Result<String> {
//        return withContext(Dispatchers.IO) {
//            repository.loginUser(email, password)
//        }
//    }
//
//
//    suspend fun register(username: String, email: String, password: String, contact: String): Result<String> {
//        return withContext(Dispatchers.IO) {
//            repository.registerUser(username, email, password, contact)
//        }
//    }
//
//
//
//
//
//
//
//
//
//
//    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun detectLocation() {
//        val context = getApplication<Application>().applicationContext
//
//        // Check if we have location permissions
//        if (ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // If we don't have permissions, update the state to show an error
//            _formState.update {
//                it.copy(
//                    isLocationLoading = false,
//                    errorMessage = "Location permissions not granted. Please grant location permissions in settings."
//                )
//            }
//            return
//        }
//
//        // Create a location request
//        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
//            .setWaitForAccurateLocation(true)
//            .setMinUpdateIntervalMillis(5000)
//            .setMaxUpdateDelayMillis(15000)
//            .build()
//
//        // Initialize location callback
//        locationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult) {
//                locationResult.lastLocation?.let { location ->
//                    // Update with actual location data
//                    updateLocationWithGeocoding(location)
//
//                    // Remove location updates after getting location once
//                    fusedLocationClient.removeLocationUpdates(this)
//                    locationCallback = null
//                }
//            }
//        }
//
//        // Request location updates
//        locationCallback?.let {
//            try {
//                fusedLocationClient.requestLocationUpdates(
//                    locationRequest,
//                    it,
//                    context.mainLooper
//                )
//            } catch (e: Exception) {
//                _formState.update { state ->
//                    state.copy(
//                        isLocationLoading = false,
//                        errorMessage = "Failed to get location: ${e.message}"
//                    )
//                }
//            }
//        }
//
//        // Also try to get last known location for faster response
//        try {
//            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
//                if (location != null) {
//                    updateLocationWithGeocoding(location)
//                }
//            }.addOnFailureListener { e ->
//                _formState.update { it.copy(
//                    isLocationLoading = false,
//                    errorMessage = "Failed to get last location: ${e.message}"
//                ) }
//            }
//        } catch (e: Exception) {
//            _formState.update { it.copy(
//                isLocationLoading = false,
//                errorMessage = "Error accessing location: ${e.message}"
//            ) }
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun updateLocationWithGeocoding(location: Location) {
//        viewModelScope.launch {
//            val context = getApplication<Application>().applicationContext
//            try {
//                // Update the location fields
//                val latitude = location.latitude.toString()
//                val longitude = location.longitude.toString()
//                val altitude = if (location.hasAltitude()) location.altitude.toString() else "Not available"
//                val accuracy = if (location.hasAccuracy()) location.accuracy.toString() else "Not available"
//
//                // Try to get city name using Geocoder
//                var cityName = ""
//
//                withContext(Dispatchers.IO) {
//                    try {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                            Geocoder(context, Locale.getDefault()).getFromLocation(
//                                location.latitude,
//                                location.longitude,
//                                1
//                            ) { addresses ->
//                                if (addresses.isNotEmpty()) {
//                                    cityName = addresses[0].locality ?: ""
//                                }
//                            }
//                        } else {
//                            @Suppress("DEPRECATION")
//                            val addresses = Geocoder(context, Locale.getDefault()).getFromLocation(
//                                location.latitude,
//                                location.longitude,
//                                1
//                            )
//                            if (addresses != null && addresses.isNotEmpty()) {
//                                cityName = addresses[0].locality ?: ""
//                            }
//                        }
//                    } catch (e: Exception) {
//                        // Just ignore geocoding errors, we'll leave city blank
//                    }
//                }
//
//                // Update the form state with all location information
//                _formState.update {
//                    it.copy(
//                        latitude = latitude,
//                        longitude = longitude,
//                        altitude = altitude,
//                        accuracy = accuracy,
//                        city = cityName,
//                        isLocationLoading = false
//                    )
//                }
//            } catch (e: Exception) {
//                _formState.update {
//                    it.copy(
//                        isLocationLoading = false,
//                        errorMessage = "Error processing location: ${e.message}"
//                    )
//                }
//            }
//        }
//    }
//
//    // Add a cleanup method to remove location updates when ViewModel is cleared
//    override fun onCleared() {
//        super.onCleared()
//        locationCallback?.let {
//            fusedLocationClient.removeLocationUpdates(it)
//        }
//    }
//}





package com.example.posapp

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
data class IncidentFormState @RequiresApi(Build.VERSION_CODES.O) constructor(
    val datetime: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
    val imageUri: Uri? = null,
    val latitude: String = "",
    val longitude: String = "",
    val altitude: String = "",
    val accuracy: String = "",
    val city: String = "",
    val division: String = "",
    val ward: String = "",
    val cell: String = "",
    val street: String = "",
    val otherStreet: String = "",
    val incidentType: String = "",
    val otherIncidentType: String = "",
    val incidentDetails: String = "",
    val isSubmitting: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val isLocationLoading: Boolean = false,
    val showDivisionDropdown: Boolean = false,
    val showStreetDropdown: Boolean = false,
    val showIncidentTypeDropdown: Boolean = false,
    val showCityDropdown: Boolean = false
)

class IncidentReportViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = IncidentRepository(application.applicationContext)

    @RequiresApi(Build.VERSION_CODES.O)
    private val _formState = MutableStateFlow(IncidentFormState())
    @RequiresApi(Build.VERSION_CODES.O)
    val formState: StateFlow<IncidentFormState> = _formState.asStateFlow()

    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    private var locationCallback: LocationCallback? = null

    // Division options
    val divisionOptions = listOf("Ayivu Division", "Central Division")

    // Street options
    val streetOptions = listOf(
        "Arua Avenue", "Hospital Road", "Adumi Road", "Duka Road", "Market Lane",
        "Rhino-camp road", "Arua - Packwach road", "Onduparaka Road", "Wadrif Road",
        "Mango Road", "School Road", "Weatherhead Park Lane", "Mvaradri - Oluko road",
        "Ediofe Road", "Muni University road", "Others"
    )

    // Incident types
    val incidentTypes = listOf(
        "Traffic Flow & Congestion",
        "Road & Infrastructure Condition",
        "Accidents & Safety",
        "Public Transport & Mobility",
        "Environmental & Hazards",
        "Law & Order Issues",
        "Other"
    )

    // List of Cities
    val cities = listOf("Arua City")

    // ============== LOCATION DETECTION - WORKING VERSION ==============

    @RequiresPermission(anyOf = [
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ])
    @RequiresApi(Build.VERSION_CODES.O)
    fun startLocationDetection(context: Context) {
        Log.d("LocationDetection", "========== STARTING LOCATION DETECTION ==========")

        // Set loading state
        _formState.update { it.copy(isLocationLoading = true, errorMessage = null) }

        // Step 1: Check if location services are enabled
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        Log.d("LocationDetection", "GPS enabled: $isGpsEnabled")
        Log.d("LocationDetection", "Network enabled: $isNetworkEnabled")

        if (!isGpsEnabled && !isNetworkEnabled) {
            Log.e("LocationDetection", "❌ Location services are DISABLED")
            _formState.update {
                it.copy(
                    isLocationLoading = false,
                    errorMessage = "Location services are disabled. Please enable GPS in your device settings."
                )
            }
            return
        }

        // Step 2: Check permissions
        val hasFineLocation = ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarseLocation = ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        Log.d("LocationDetection", "Fine location permission: $hasFineLocation")
        Log.d("LocationDetection", "Coarse location permission: $hasCoarseLocation")

        if (!hasFineLocation && !hasCoarseLocation) {
            Log.e("LocationDetection", "❌ No location permissions")
            _formState.update {
                it.copy(
                    isLocationLoading = false,
                    errorMessage = "Location permission denied"
                )
            }
            return
        }

        Log.d("LocationDetection", "✓ Permissions OK. Starting location request...")

        // Step 3: Request location updates (more reliable than one-time location)
        requestLocationUpdates(context)
    }

    @RequiresPermission(anyOf = [
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ])
    @RequiresApi(Build.VERSION_CODES.O)
    private fun requestLocationUpdates(context: Context) {
        // Create location request
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            5000L // Update every 5 seconds
        ).apply {
            setMinUpdateIntervalMillis(2000L)
            setMaxUpdateDelayMillis(10000L)
            setWaitForAccurateLocation(false)
        }.build()

        Log.d("LocationDetection", "Location request created with HIGH_ACCURACY priority")

        // Create location callback
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                Log.d("LocationDetection", "📍 Location result received!")

                locationResult.lastLocation?.let { location ->
                    Log.d("LocationDetection", "✓ Got location!")
                    Log.d("LocationDetection", "  Latitude: ${location.latitude}")
                    Log.d("LocationDetection", "  Longitude: ${location.longitude}")
                    Log.d("LocationDetection", "  Accuracy: ${location.accuracy}m")

                    updateLocationFields(location)

                    // Stop location updates after getting first location
                    stopLocationUpdates()
                } ?: run {
                    Log.e("LocationDetection", "❌ Location result was null")
                }
            }

            override fun onLocationAvailability(availability: LocationAvailability) {
                Log.d("LocationDetection", "Location availability: ${availability.isLocationAvailable}")
                if (!availability.isLocationAvailable) {
                    _formState.update {
                        it.copy(
                            isLocationLoading = false,
                            errorMessage = "Location is not available. Please ensure GPS is enabled and try going outside."
                        )
                    }
                    stopLocationUpdates()
                }
            }
        }

        // Request location updates
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback!!,
                Looper.getMainLooper()
            ).addOnSuccessListener {
                Log.d("LocationDetection", "✓ Successfully started location updates")
            }.addOnFailureListener { exception ->
                Log.e("LocationDetection", "❌ Failed to start location updates: ${exception.message}")
                _formState.update {
                    it.copy(
                        isLocationLoading = false,
                        errorMessage = "Failed to start location updates: ${exception.message}"
                    )
                }
            }

            // Also try to get last location as a quick fallback
            tryLastLocation()

        } catch (e: SecurityException) {
            Log.e("LocationDetection", "❌ Security exception: ${e.message}")
            _formState.update {
                it.copy(
                    isLocationLoading = false,
                    errorMessage = "Location permission error"
                )
            }
        }
    }

    @RequiresPermission(anyOf = [
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ])
    @RequiresApi(Build.VERSION_CODES.O)
    private fun tryLastLocation() {
        Log.d("LocationDetection", "Trying to get last known location...")

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    Log.d("LocationDetection", "✓ Got last known location")
                    Log.d("LocationDetection", "  Latitude: ${location.latitude}")
                    Log.d("LocationDetection", "  Longitude: ${location.longitude}")

                    updateLocationFields(location)
                    stopLocationUpdates()
                } else {
                    Log.d("LocationDetection", "Last location is null, waiting for fresh location...")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("LocationDetection", "Failed to get last location: ${exception.message}")
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateLocationFields(location: Location) {
        Log.d("LocationDetection", "Updating form fields with location data...")

        // Get the original accuracy
        val originalAccuracy = if (location.hasAccuracy()) location.accuracy else 0f

        // Limit accuracy to maximum 1 meter
        val limitedAccuracy = if (originalAccuracy > 1.0f) 1.0f else originalAccuracy

        Log.d("LocationDetection", "Original accuracy: ${originalAccuracy}m, Limited to: ${limitedAccuracy}m")

        _formState.update { state ->
            state.copy(
                latitude = String.format("%.6f", location.latitude),
                longitude = String.format("%.6f", location.longitude),
                altitude = if (location.hasAltitude()) String.format("%.2f", location.altitude) else "",
                accuracy = String.format("%.2f", limitedAccuracy), // Use limited accuracy
                isLocationLoading = false,
                errorMessage = null
            )
        }

        Log.d("LocationDetection", "✓ Form fields updated successfully!")
    }

    private fun stopLocationUpdates() {
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
            locationCallback = null
            Log.d("LocationDetection", "Stopped location updates")
        }
    }

    // ============== END OF LOCATION DETECTION ==============

    // UPDATE FUNCTIONS
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateCity(city: String) {
        _formState.update { it.copy(city = city) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateDatetime(datetime: String) {
        _formState.update { it.copy(datetime = datetime, errorMessage = null) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateImage(uri: Uri?) {
        _formState.update { it.copy(imageUri = uri) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateLatitude(latitude: String) {
        _formState.update { it.copy(latitude = latitude) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateLongitude(longitude: String) {
        _formState.update { it.copy(longitude = longitude) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateAltitude(altitude: String) {
        _formState.update { it.copy(altitude = altitude) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateAccuracy(accuracy: String) {
        _formState.update { it.copy(accuracy = accuracy) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateDivision(division: String) {
        _formState.update { it.copy(division = division) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateWard(ward: String) {
        _formState.update { it.copy(ward = ward) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateCell(cell: String) {
        _formState.update { it.copy(cell = cell) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateStreet(street: String) {
        _formState.update { it.copy(street = street) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateOtherStreet(otherStreet: String) {
        _formState.update { it.copy(otherStreet = otherStreet) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateIncidentType(incidentType: String) {
        _formState.update { it.copy(incidentType = incidentType) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateOtherIncidentType(otherIncidentType: String) {
        _formState.update { it.copy(otherIncidentType = otherIncidentType) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateIncidentDetails(details: String) {
        _formState.update { it.copy(incidentDetails = details) }
    }

    // DROPDOWN FUNCTIONS
    @RequiresApi(Build.VERSION_CODES.O)
    fun toggleCityDropdown() {
        _formState.update {
            it.copy(
                showCityDropdown = !it.showCityDropdown,
                showDivisionDropdown = false,
                showStreetDropdown = false,
                showIncidentTypeDropdown = false
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toggleDivisionDropdown() {
        _formState.update {
            it.copy(
                showDivisionDropdown = !it.showDivisionDropdown,
                showStreetDropdown = false,
                showIncidentTypeDropdown = false,
                showCityDropdown = false
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toggleStreetDropdown() {
        _formState.update {
            it.copy(
                showStreetDropdown = !it.showStreetDropdown,
                showDivisionDropdown = false,
                showIncidentTypeDropdown = false,
                showCityDropdown = false
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toggleIncidentTypeDropdown() {
        _formState.update {
            it.copy(
                showIncidentTypeDropdown = !it.showIncidentTypeDropdown,
                showDivisionDropdown = false,
                showStreetDropdown = false,
                showCityDropdown = false
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun hideAllDropdowns() {
        _formState.update {
            it.copy(
                showDivisionDropdown = false,
                showStreetDropdown = false,
                showIncidentTypeDropdown = false,
                showCityDropdown = false
            )
        }
    }

    // FORM SUBMISSION
    @RequiresApi(Build.VERSION_CODES.O)
    fun submitForm() {
        viewModelScope.launch {
            _formState.update { it.copy(isSubmitting = true, errorMessage = null) }

            try {
                val result = repository.submitIncidentReport(formState.value)

                if (result.isSuccess) {
                    _formState.update { it.copy(isSubmitting = false, isSuccess = true) }
                } else {
                    val exception = result.exceptionOrNull() ?: Exception("Unknown error occurred")
                    _formState.update {
                        it.copy(
                            isSubmitting = false,
                            errorMessage = exception.message ?: "An unknown error occurred"
                        )
                    }
                }
            } catch (e: Exception) {
                _formState.update {
                    it.copy(
                        isSubmitting = false,
                        errorMessage = e.message ?: "An unknown error occurred"
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun resetForm() {
        _formState.update {
            IncidentFormState(
                datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            )
        }
    }

    // AUTH FUNCTIONS
    suspend fun login(email: String, password: String): Result<String> {
        return withContext(Dispatchers.IO) {
            repository.loginUser(email, password)
        }
    }

    suspend fun register(username: String, email: String, password: String, contact: String): Result<String> {
        return withContext(Dispatchers.IO) {
            repository.registerUser(username, email, password, contact)
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopLocationUpdates()
    }
}
