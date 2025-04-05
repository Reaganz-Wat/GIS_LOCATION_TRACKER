package com.example.posapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.posapp.ui.theme.POSAPPTheme

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            POSAPPTheme {
                AppNavigation()
            }
        }


    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { Login(navController) }
        composable("forgot password") { ForgotPassword(navController) }
        composable("signup") { SignUp(navController) }
        composable("maindashboard") {Dashboard(navController)}
        composable("addIncident") { AddIncident(navController)}
    }
}