package com.example.posapp

import android.content.Context
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class IncidentRepository(private val context: Context) {

    companion object {
        private const val TAG = "IncidentRepository"
        private const val BASE_URL = "https://tdmis.app/geotrafficbackend/api.php"

//        private const val BASE_URL = "http://192.168.33.142/geotrafficbackend/api.php"

        // Use this for local testing: "http://10.0.2.2/geotrafficbackend/api.php" (Android emulator)
        // Use this for local testing: "http://192.168.x.x/geotrafficbackend/api.php" (Real device)
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    /**
     * Submit incident report to the backend
     */
    suspend fun submitIncidentReport(formState: IncidentFormState): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "========== STARTING INCIDENT SUBMISSION ==========")
                logFormData(formState)

                // Build multipart request
                val requestBody = buildMultipartRequest(formState)

                // Create request
                val request = Request.Builder()
                    .url(BASE_URL)
                    .post(requestBody)
                    .build()

                Log.d(TAG, "Request URL: ${request.url}")
                Log.d(TAG, "Request Method: ${request.method}")

                // Execute request
                val response = executeRequest(request)
                val responseBody = response.body?.string() ?: ""

                Log.d(TAG, "========== RESPONSE RECEIVED ==========")
                Log.d(TAG, "Response Code: ${response.code}")
                Log.d(TAG, "Response Message: ${response.message}")
                Log.d(TAG, "Response Body: $responseBody")
                Log.d(TAG, "======================================")

                if (response.isSuccessful) {
                    parseSuccessResponse(responseBody)
                } else {
                    Log.e(TAG, "HTTP Error: ${response.code} - $responseBody")
                    Result.failure(Exception("Server returned error: ${response.code}"))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception during submission: ${e.message}", e)
                Result.failure(Exception("Network error: ${e.message}"))
            }
        }
    }

    /**
     * Log all form data for debugging
     */
    private fun logFormData(formState: IncidentFormState) {
        Log.d(TAG, "========== FORM DATA ==========")
        Log.d(TAG, "Datetime: ${formState.datetime}")
        Log.d(TAG, "Latitude: ${formState.latitude}")
        Log.d(TAG, "Longitude: ${formState.longitude}")
        Log.d(TAG, "Altitude: ${formState.altitude}")
        Log.d(TAG, "Accuracy: ${formState.accuracy}")
        Log.d(TAG, "City: ${formState.city}")
        Log.d(TAG, "Division: ${formState.division}")
        Log.d(TAG, "Ward: ${formState.ward}")
        Log.d(TAG, "Cell: ${formState.cell}")
        Log.d(TAG, "Street: ${formState.street}")
        Log.d(TAG, "Other Street: ${formState.otherStreet}")
        Log.d(TAG, "Incident Category: ${formState.incidentCategory}")
        Log.d(TAG, "Incident Subcategory: ${formState.incidentSubcategory}")
        Log.d(TAG, "Incident Type (legacy): ${formState.incidentType}")
        Log.d(TAG, "Other Incident Type: ${formState.otherIncidentType}")
        Log.d(TAG, "Incident Details: ${formState.incidentDetails}")
        Log.d(TAG, "Image URI: ${formState.imageUri}")
        Log.d(TAG, "===============================")
    }

    /**
     * Build multipart request body
     */
    private fun buildMultipartRequest(formState: IncidentFormState): MultipartBody {
        Log.d(TAG, "Building multipart request...")

        val multipartBuilder = MultipartBody.Builder()
            .setType(MultipartBody.FORM)

        // Add all required fields
        multipartBuilder.apply {
            addFormDataPart("action", "create_incident")
            addFormDataPart("datetime", formState.datetime)
            addFormDataPart("latitude", formState.latitude)
            addFormDataPart("longitude", formState.longitude)
            addFormDataPart("altitude", formState.altitude)
            addFormDataPart("accuracy", formState.accuracy)
            addFormDataPart("city", formState.city)
            addFormDataPart("division", formState.division)
            addFormDataPart("ward", formState.ward)
            addFormDataPart("cell", formState.cell)
            addFormDataPart("street", formState.street)

            // Add incident category and subcategory (new fields)
            addFormDataPart("incidentCategory", formState.incidentCategory)
            addFormDataPart("incidentSubcategory", formState.incidentSubcategory)

            // Keep legacy incidentType for backward compatibility
            // Combine category and subcategory if both exist
            val combinedIncidentType = if (formState.incidentCategory == "Other") {
                formState.otherIncidentType
            } else if (formState.incidentSubcategory.isNotEmpty()) {
                "${formState.incidentCategory} - ${formState.incidentSubcategory}"
            } else {
                formState.incidentCategory
            }
            addFormDataPart("incidentType", combinedIncidentType)

            addFormDataPart("incidentDetails", formState.incidentDetails)
        }

        // Add conditional fields
        if (formState.street == "Others" && formState.otherStreet.isNotEmpty()) {
            multipartBuilder.addFormDataPart("otherStreet", formState.otherStreet)
            Log.d(TAG, "Added otherStreet: ${formState.otherStreet}")
        }

        if (formState.incidentCategory == "Other" && formState.otherIncidentType.isNotEmpty()) {
            multipartBuilder.addFormDataPart("otherIncidentType", formState.otherIncidentType)
            Log.d(TAG, "Added otherIncidentType: ${formState.otherIncidentType}")
        }

        // Add image if available
        formState.imageUri?.let { uri ->
            try {
                val file = getFileFromUri(uri)
                Log.d(TAG, "Image file created: ${file.name}, Size: ${file.length()} bytes")

                val mimeType = context.contentResolver.getType(uri) ?: "image/jpeg"
                Log.d(TAG, "Image MIME type: $mimeType")

                val mediaType = mimeType.toMediaTypeOrNull()
                val requestBody = file.asRequestBody(mediaType)
                multipartBuilder.addFormDataPart("image", file.name, requestBody)

                Log.d(TAG, "Image added to request successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Error adding image to request: ${e.message}", e)
            }
        } ?: Log.d(TAG, "No image to upload")

        Log.d(TAG, "Multipart request built successfully")
        return multipartBuilder.build()
    }

    /**
     * Parse successful response
     */
    private fun parseSuccessResponse(responseBody: String): Result<String> {
        return try {
            when {
                responseBody.contains("\"error\"", ignoreCase = true) -> {
                    Log.e(TAG, "API returned error in response: $responseBody")
                    Result.failure(Exception("Server error: Failed to submit incident"))
                }
                responseBody.contains("\"success\":false", ignoreCase = true) -> {
                    Log.e(TAG, "API returned success:false: $responseBody")
                    Result.failure(Exception("Submission failed. Please try again."))
                }
                responseBody.contains("\"id\"") || responseBody.contains("\"success\":true", ignoreCase = true) -> {
                    // Extract ID if present
                    val idPattern = "\"id\"[:\\s]*(\\d+)".toRegex()
                    val id = idPattern.find(responseBody)?.groupValues?.get(1)

                    if (id != null) {
                        Log.i(TAG, "✓ Incident created successfully with ID: $id")
                        Result.success("Incident reported successfully! (ID: $id)")
                    } else {
                        Log.i(TAG, "✓ Incident created successfully")
                        Result.success("Incident reported successfully!")
                    }
                }
                else -> {
                    Log.w(TAG, "Unexpected response format (assuming success): $responseBody")
                    Result.success("Incident reported successfully!")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing response: ${e.message}", e)
            Result.failure(Exception("Error processing server response"))
        }
    }

    /**
     * Convert URI to File
     */
    private fun getFileFromUri(uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IOException("Cannot open input stream for URI: $uri")

        // Get file extension from URI
        val extension = context.contentResolver.getType(uri)?.let { mimeType ->
            when {
                mimeType.contains("jpeg") || mimeType.contains("jpg") -> ".jpg"
                mimeType.contains("png") -> ".png"
                mimeType.contains("gif") -> ".gif"
                mimeType.contains("webp") -> ".webp"
                else -> ".jpg"
            }
        } ?: ".jpg"

        val tempFile = File.createTempFile("incident_image_", extension, context.cacheDir)

        inputStream.use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        Log.d(TAG, "Temporary file created: ${tempFile.absolutePath}")
        return tempFile
    }

    /**
     * Execute HTTP request asynchronously
     */
    private suspend fun executeRequest(request: Request): Response = suspendCoroutine { continuation ->
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "Request failed: ${e.message}", e)
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d(TAG, "Request completed with code: ${response.code}")
                continuation.resume(response)
            }
        })
    }

    // ============== AUTHENTICATION METHODS ==============

    /**
     * Login user
     */
    suspend fun loginUser(email: String, password: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "========== LOGIN REQUEST ==========")
            Log.d(TAG, "Username/Email: $email")
            Log.d(TAG, "Password: ${if (password.isNotEmpty()) "***" else "(empty)"}")

            val formBody = FormBody.Builder()
                .add("action", "login")
                .add("username", email)
                .add("password", password)
                .build()

            val request = Request.Builder()
                .url(BASE_URL)
                .post(formBody)
                .build()

            val response = executeRequest(request)
            val responseBody = response.body?.string() ?: ""

            Log.d(TAG, "Login Response Code: ${response.code}")
            Log.d(TAG, "Login Response Body: $responseBody")

            if (response.isSuccessful) {
                Result.success(responseBody)
            } else {
                Result.failure(Exception("Login failed: ${response.code} ${response.message}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Login exception: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Register new user
     */
    suspend fun registerUser(
        username: String,
        email: String,
        password: String,
        contact: String
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "========== REGISTER REQUEST ==========")
            Log.d(TAG, "Username: $username")
            Log.d(TAG, "Email: $email")
            Log.d(TAG, "Contact: $contact")
            Log.d(TAG, "Password: ${if (password.isNotEmpty()) "***" else "(empty)"}")

            val formBody = FormBody.Builder()
                .add("action", "register")
                .add("username", username)
                .add("email", email)
                .add("password", password)
                .add("contact", contact)
                .build()

            val request = Request.Builder()
                .url(BASE_URL)
                .post(formBody)
                .build()

            val response = executeRequest(request)
            val responseBody = response.body?.string() ?: ""

            Log.d(TAG, "Register Response Code: ${response.code}")
            Log.d(TAG, "Register Response Body: $responseBody")

            if (response.isSuccessful) {
                Result.success(responseBody)
            } else {
                Result.failure(Exception("Registration failed: ${response.code} ${response.message}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Register exception: ${e.message}", e)
            Result.failure(e)
        }
    }
}