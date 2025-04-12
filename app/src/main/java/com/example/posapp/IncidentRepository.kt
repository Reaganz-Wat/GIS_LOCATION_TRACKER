package com.example.posapp

import android.content.Context
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class IncidentRepository(private val context: Context) {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val baseUrl = "http://192.168.100.235/geotrafficbackend/api.php" // Replace with your actual API base URL

    suspend fun submitIncidentReport(formState: IncidentFormState): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                // Build multipart request
                val requestBody = buildMultipartRequest(formState)

                // Create request
                val request = Request.Builder()
                    .url("$baseUrl")
                    .post(requestBody)
                    .build()

                // Execute request
                val response = executeRequest(request)

                if (response.isSuccessful) {
                    response.body?.let { Log.d("Body of the responsessss", it.string()) }
                    Result.success("Incident reported successfully")
                } else {
                    val errorBody = response.body?.string() ?: "Unknown error occurred"
                    Result.failure(Exception("API Error: ${response.code} - $errorBody"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    private fun buildMultipartRequest(formState: IncidentFormState): MultipartBody {
        val multipartBuilder = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("datetime", formState.datetime)
            .addFormDataPart("latitude", formState.latitude)
            .addFormDataPart("longitude", formState.longitude)
            .addFormDataPart("altitude", formState.altitude)
            .addFormDataPart("accuracy", formState.accuracy)
            .addFormDataPart("city", formState.city)
            .addFormDataPart("division", formState.division)
            .addFormDataPart("ward", formState.ward)
            .addFormDataPart("cell", formState.cell)
            .addFormDataPart("street", formState.street)
            .addFormDataPart("incidentType", formState.incidentType)
            .addFormDataPart("incidentDetails", formState.incidentDetails)
            .addFormDataPart("other_street", formState.otherStreet)
            .addFormDataPart("action", "create_incident")

        // Add conditional fields
        if (formState.street == "Others") {
            multipartBuilder.addFormDataPart("otherStreet", formState.otherStreet)
        }

        if (formState.incidentType == "Other") {
            multipartBuilder.addFormDataPart("otherIncidentType", formState.otherIncidentType)
        }

        // Add image if available
        formState.imageUri?.let { uri ->
            val file = getFileFromUri(uri)
            val mediaType =
                (context.contentResolver.getType(uri) ?: "image/jpeg").toMediaTypeOrNull()
            val requestBody = RequestBody.create(mediaType, file)
            multipartBuilder.addFormDataPart("image", file.name, requestBody)
        }

        return multipartBuilder.build()
    }

    private fun getFileFromUri(uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)
        tempFile.outputStream().use { fileOut ->
            inputStream?.copyTo(fileOut)
        }
        return tempFile
    }

    private suspend fun executeRequest(request: Request): Response = suspendCoroutine { continuation ->
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                continuation.resume(response)
            }
        })
    }



    suspend fun loginUser(
        email: String,
        password: String
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            // Create form body request instead of JSON
            val formBody = FormBody.Builder()
                .add("action", "login")
                .add("username", email)
                .add("password", password)
                .build()

            val request = Request.Builder()
                .url(baseUrl)
                .post(formBody)
                .build()

            val response = executeRequest(request)

            val responseBody = response.body?.string()
            Log.d("Login Response", responseBody ?: "No response body")

            if (response.isSuccessful) {
                Result.success(responseBody ?: "No response body")
            } else {
                Result.failure(Exception("Error: ${response.code} ${response.message}"))
            }

        } catch (e: Exception) {
            Log.e("Login Error", "Exception: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun registerUser(
        username: String,
        email: String,
        password: String,
        contact: String
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            // Create form body request instead of JSON
            val formBody = FormBody.Builder()
                .add("action", "register")
                .add("username", username)
                .add("password", password)
                .add("email", email)
                .add("contact", contact)
                .build()

            val request = Request.Builder()
                .url(baseUrl)
                .post(formBody)
                .build()

            val response = executeRequest(request)

            val responseBody = response.body?.string()
            Log.d("Register Response", responseBody ?: "No response body")

            if (response.isSuccessful) {
                Result.success(responseBody ?: "No response body")
            } else {
                Result.failure(Exception("Error: ${response.code} ${response.message}"))
            }

        } catch (e: Exception) {
            Log.e("Register Error", "Exception: ${e.message}")
            Result.failure(e)
        }
    }



}