package com.example.posapp

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncidentReportScreen(navController: NavHostController) {
    val viewModel: IncidentReportViewModel = viewModel()
    val formState by viewModel.formState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Road Traffic Data Reporting") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        TrafficIncidentForm(
            modifier = Modifier.padding(innerPadding),
            viewModel = viewModel,
            formState = formState,
            onNavigateBack = { navController.popBackStack() }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrafficIncidentForm(
    modifier: Modifier = Modifier,
    viewModel: IncidentReportViewModel,
    formState: IncidentFormState,
    onNavigateBack: () -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    // Image picker
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.updateImage(uri)
    }

    // Location permission
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val locationGranted = permissions.entries.all { it.value }
        if (locationGranted) {
            viewModel.startLocationDetection()
        } else {
            Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    // Success dialog
    if (formState.isSuccess) {
        AlertDialog(
            onDismissRequest = { viewModel.resetForm() },
            title = { Text("Success") },
            text = { Text("Incident report submitted successfully!") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.resetForm()
                        onNavigateBack()
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

    // Error dialog
    formState.errorMessage?.let { errorMsg ->
        AlertDialog(
            onDismissRequest = {
                viewModel.updateDatetime(formState.datetime)  // Reset error state
            },
            title = { Text("Error") },
            text = { Text(errorMsg) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.updateDatetime(formState.datetime)  // Reset error state
                }) {
                    Text("OK")
                }
            }
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Form header
            Text(
                text = "Road Traffic Data Tool",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Divider()

            // Date and time section
            FormSection(title = "Time when the incident is reported") {
                OutlinedTextField(
                    value = formState.datetime,
                    onValueChange = { viewModel.updateDatetime(it) },
                    label = { Text("Date and Time (yyyy-MM-dd HH:mm)") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) }
                )
            }

            // Photo upload section
            FormSection(title = "Photo") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { imagePickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (formState.imageUri != null) {
                        AsyncImage(
                            model = formState.imageUri,
                            contentDescription = "Selected image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.baseline_cloud_upload_24),
                                contentDescription = "Upload photo",
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Click here to upload file (< 10MB)",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Location section
            FormSection(title = "Location of the incident using GPS") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value = formState.latitude,
                        onValueChange = { viewModel.updateLatitude(it) },
                        label = { Text("Latitude (°)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = formState.longitude,
                        onValueChange = { viewModel.updateLongitude(it) },
                        label = { Text("Longitude (°)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value = formState.altitude,
                        onValueChange = { viewModel.updateAltitude(it) },
                        label = { Text("Altitude (m)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = formState.accuracy,
                        onValueChange = { viewModel.updateAccuracy(it) },
                        label = { Text("Accuracy (m)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        } else {
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                        }
                        locationPermissionLauncher.launch(permissions)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !formState.isLocationLoading
                ) {
                    if (formState.isLocationLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Detecting location...")
                    } else {
                        Icon(Icons.Default.LocationOn, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Get Current Location")
                    }
                }
            }

            // City section (static for Arua City)
//            FormSection(title = "City") {
//                OutlinedTextField(
//                    value = "Arua City",
//                    onValueChange = { /* Read-only */ },
//                    readOnly = true,
//                    modifier = Modifier.fillMaxWidth(),
//                    leadingIcon = { Icon(imageVector = Icons.Default.LocationOn, contentDescription = null) }
//                )
//            }

            // Replace the static city section with a dropdown
            FormSection(title = "City") {
                ExposedDropdownMenuBox(
                    expanded = formState.showCityDropdown,
                    onExpandedChange = { viewModel.toggleCityDropdown() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = formState.city,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Select City") },
                        leadingIcon = { Icon(imageVector = Icons.Default.LocationOn, contentDescription = null) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = formState.showCityDropdown) },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = formState.showCityDropdown,
                        onDismissRequest = { viewModel.hideAllDropdowns() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        viewModel.cities.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    viewModel.updateCity(option)
                                    viewModel.hideAllDropdowns()
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }
            }

            // Division dropdown - FIXED IMPLEMENTATION
            FormSection(title = "Division from where the Incident is reported from") {
                ExposedDropdownMenuBox(
                    expanded = formState.showDivisionDropdown,
                    onExpandedChange = { viewModel.toggleDivisionDropdown() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = formState.division,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Select Division") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = formState.showDivisionDropdown) },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = formState.showDivisionDropdown,
                        onDismissRequest = { viewModel.hideAllDropdowns() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        viewModel.divisionOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    viewModel.updateDivision(option)
                                    viewModel.hideAllDropdowns()
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }
            }

            // Ward field
            FormSection(title = "Ward from where the Incident is reported from") {
                OutlinedTextField(
                    value = formState.ward,
                    onValueChange = { viewModel.updateWard(it) },
                    label = { Text("Enter Ward") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Cell field
            FormSection(title = "Cell from where the Incident is reported from") {
                OutlinedTextField(
                    value = formState.cell,
                    onValueChange = { viewModel.updateCell(it) },
                    label = { Text("Enter Cell") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Street dropdown - FIXED IMPLEMENTATION
            FormSection(title = "Street from where the Incident is reported from") {
                ExposedDropdownMenuBox(
                    expanded = formState.showStreetDropdown,
                    onExpandedChange = { viewModel.toggleStreetDropdown() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = formState.street,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Select Street") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = formState.showStreetDropdown) },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = formState.showStreetDropdown,
                        onDismissRequest = { viewModel.hideAllDropdowns() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        viewModel.streetOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    viewModel.updateStreet(option)
                                    viewModel.hideAllDropdowns()
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }

                // Show "Other" field if "Others" is selected
                if (formState.street == "Others") {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = formState.otherStreet,
                        onValueChange = { viewModel.updateOtherStreet(it) },
                        label = { Text("If other, specify") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Incident type dropdown - FIXED IMPLEMENTATION
            FormSection(title = "Nature of the problem/Incident observed") {
                ExposedDropdownMenuBox(
                    expanded = formState.showIncidentTypeDropdown,
                    onExpandedChange = { viewModel.toggleIncidentTypeDropdown() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = formState.incidentType,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Select Incident Type") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = formState.showIncidentTypeDropdown) },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = formState.showIncidentTypeDropdown,
                        onDismissRequest = { viewModel.hideAllDropdowns() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        viewModel.incidentTypes.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    viewModel.updateIncidentType(option)
                                    viewModel.hideAllDropdowns()
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }

                // Show "Other" field if "Other" is selected
                if (formState.incidentType == "Other") {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = formState.otherIncidentType,
                        onValueChange = { viewModel.updateOtherIncidentType(it) },
                        label = { Text("If other, specify") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Incident details
            FormSection(title = "Details of the problem or incident") {
                OutlinedTextField(
                    value = formState.incidentDetails,
                    onValueChange = { viewModel.updateIncidentDetails(it) },
                    label = { Text("Enter incident details") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 5
                )
            }

            Button(
                onClick = { viewModel.submitForm() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(5.dp),
                enabled = !formState.isSubmitting
            ) {
                if (formState.isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Submitting...")
                } else {
                    Text("Submit")
                }
            }

            // Thank you message
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = "Thank you for using Muni University E-Citizen Traffic Data Reporting Platform to report traffic issues.",
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.height(72.dp)) // Space for the FAB
        }
    }
}

@Composable
fun FormSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
        )
        content()
    }
}