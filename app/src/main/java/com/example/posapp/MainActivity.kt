package com.example.posapp

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.posapp.ui.theme.POSAPPTheme

class MainActivity : ComponentActivity() {

    private lateinit var airPlainModeReceiver: AirPlaneModeReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            POSAPPTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        // Initialize and register
        airPlainModeReceiver = AirPlaneModeReceiver()
        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_SCREEN_OFF)
        }
        registerReceiver(airPlainModeReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Destroing the app", "App being destroyed")
        unregisterReceiver(airPlainModeReceiver)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text("Testing")
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Arrow Back")
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) {
    innerPadding -> MainContent(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun MainContent(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column (modifier = modifier.padding(10.dp)) {

        Text("This is the start of the service, its for testing purposes")

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(onClick = {
            val serviceIntent = Intent(context, CountDownService::class.java)
            context.startService(serviceIntent)
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Start service")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    POSAPPTheme {
        Greeting("Android")
    }
}