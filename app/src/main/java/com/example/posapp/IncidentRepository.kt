package com.example.posapp

import android.content.Context
import android.net.Uri
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

    private val baseUrl = "YOUR_BASE_API_URL" // Replace with your actual API base URL

    suspend fun submitIncidentReport(formState: IncidentFormState): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                // Build multipart request
                val requestBody = buildMultipartRequest(formState)

                // Create request
                val request = Request.Builder()
                    .url("$baseUrl/incidents")
                    .post(requestBody)
                    .build()

                // Execute request
                val response = executeRequest(request)

                if (response.isSuccessful) {
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
}