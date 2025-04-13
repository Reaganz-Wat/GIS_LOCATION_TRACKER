//package com.example.posapp
//
//import android.widget.Toast
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.RectangleShape
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun Dashboard(navHostController: NavHostController) {
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.munilogo1),
//                            contentDescription = "Muni University Logo",
//                            modifier = Modifier
//                                .size(48.dp)  // Reduced size to match your topbar height
//                                .clip(CircleShape)
//                                .border(1.dp, Color.White, CircleShape),  // Optional white border for better visibility
//                            contentScale = ContentScale.Fit  // This ensures the entire logo fits within the circle
//                        )
//                        Spacer(modifier = Modifier.width(10.dp))
//                        Text(
//                            text = "Geo",
//                            style = MaterialTheme.typography.headlineMedium,
//                            color = Color.White
//                        )
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.primaryContainer,
//                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
//                ),
//                actions = {
//                    IconButton(onClick = { /* Menu action */ }) {
//                        Icon(
//                            imageVector = Icons.Default.MoreVert,
//                            contentDescription = "More options",
//                            tint = Color.White
//                        )
//                    }
//                }
//            )
//        },
//        containerColor = Color.White
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .padding(paddingValues)
//                .fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            // Logo Section
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                // Government logo placeholder
//                Image(
//                    painter = painterResource(id = R.drawable.baseline_add_location_24),
//                    contentDescription = "Government Logo",
//                    modifier = Modifier.size(72.dp)
//                )
//
//                // SafePal info logo
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text(
//                        text = "UNFPA",
//                        style = MaterialTheme.typography.titleMedium,
//                        fontWeight = FontWeight.Bold
//                    )
//                    Box(
//                        modifier = Modifier
//                            .size(40.dp)
//                            .background(Color(0xFFF8E8B0), shape = CircleShape),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = "INFO",
//                            color = Color(0xFFFF9800),
//                            style = MaterialTheme.typography.bodySmall,
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
//                }
//            }
//
//            // Support Message
//            Text(
//                text = "We are here for you . .",
//                style = MaterialTheme.typography.titleLarge,
//                fontWeight = FontWeight.Medium,
//                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
//            )
//
//            Text(
//                text = "Talk to us if you or someone you know has been sexually assaulted",
//                style = MaterialTheme.typography.bodyLarge,
//                textAlign = TextAlign.Center,
//                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
//            )
//
//            // Report Button
//            val context = LocalContext.current
//            Box(
//                modifier = Modifier
//                    .padding(24.dp)
//                    .size(200.dp)
//                    .clip(CircleShape)
//                    .background(MaterialTheme.colorScheme.primaryContainer)
//                    .clickable {
//                        Toast.makeText(context, "Navigating to report screen", Toast.LENGTH_SHORT).show()
//                        navHostController.navigate("addIncident")
//                    },
//                contentAlignment = Alignment.Center
//            ) {
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Phone,
//                        contentDescription = "Report",
//                        tint = Color.White,
//                        modifier = Modifier.size(48.dp)
//                    )
//                    Text(
//                        text = "REPORT",
//                        color = Color.White,
//                        style = MaterialTheme.typography.titleLarge,
//                        fontWeight = FontWeight.Bold,
//                        modifier = Modifier.padding(top = 8.dp)
//                    )
//                }
//            }
//
//            // Discover More Section
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//                    .clickable { /* Discover more action */ },
//                colors = CardDefaults.cardColors(
//                    containerColor = Color.White
//                ),
//                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
//            ) {
//                Row(
//                    modifier = Modifier
//                        .padding(16.dp)
//                        .fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.baseline_map_24),
//                        contentDescription = "Health Information",
//                        modifier = Modifier
//                            .size(60.dp)
//                            .clip(RoundedCornerShape(4.dp)),
//                        contentScale = ContentScale.Crop
//                    )
//
//                    Column(
//                        modifier = Modifier.padding(start = 16.dp)
//                    ) {
//                        Text(
//                            text = "Discover more",
//                            style = MaterialTheme.typography.titleMedium,
//                            fontWeight = FontWeight.Bold
//                        )
//                        Text(
//                            text = "Watch videos, learn about your health & play fun quizzes",
//                            style = MaterialTheme.typography.bodyMedium,
//                            color = Color.Gray
//                        )
//                    }
//                }
//            }
//
//            // Additional Resources Section
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                colors = CardDefaults.cardColors(
//                    containerColor = Color(0xFFF5F5F5)
//                ),
//                shape = RoundedCornerShape(12.dp)
//            ) {
//                Column(
//                    modifier = Modifier.padding(16.dp)
//                ) {
//                    Text(
//                        text = "Resources",
//                        style = MaterialTheme.typography.titleMedium,
//                        fontWeight = FontWeight.Bold,
//                        modifier = Modifier.padding(bottom = 8.dp)
//                    )
//
//                    ResourceItem(
//                        icon = Icons.Default.LocationOn,
//                        title = "Find Help Near You",
//                        description = "Locate support centers in your area"
//                    )
//
//                    Divider(modifier = Modifier.padding(vertical = 8.dp))
//
//                    ResourceItem(
//                        icon = Icons.Default.Call,
//                        title = "Helpline",
//                        description = "24/7 confidential support"
//                    )
//
//                    Divider(modifier = Modifier.padding(vertical = 8.dp))
//
//                    ResourceItem(
//                        icon = Icons.Default.Notifications,
//                        title = "Educational Materials",
//                        description = "Learn about safety and prevention"
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun ResourceItem(icon: ImageVector, title: String, description: String) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp)
//            .clickable { },
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Icon(
//            imageVector = icon,
//            contentDescription = title,
//            tint = Color(0xFFE91E63),
//            modifier = Modifier
//                .size(36.dp)
//                .padding(end = 16.dp)
//        )
//
//        Column {
//            Text(
//                text = title,
//                style = MaterialTheme.typography.titleSmall,
//                fontWeight = FontWeight.Bold
//            )
//            Text(
//                text = description,
//                style = MaterialTheme.typography.bodySmall,
//                color = Color.Gray
//            )
//        }
//    }
//}
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun PreviewThis() {
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.munilogo1),
//                            contentDescription = "Muni University Logo",
//                            modifier = Modifier
//                                .size(48.dp)  // Reduced size to match your topbar height
//                                .clip(CircleShape)
//                                .border(1.dp, Color.White, CircleShape),  // Optional white border for better visibility
//                            contentScale = ContentScale.Fit  // This ensures the entire logo fits within the circle
//                        )
//                        Spacer(modifier = Modifier.width(10.dp))
//                        Text(
//                            text = "Geo",
//                            style = MaterialTheme.typography.headlineMedium,
//                            color = Color.White
//                        )
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.primaryContainer,
//                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
//                ),
//                actions = {
//                    IconButton(onClick = { /* Menu action */ }) {
//                        Icon(
//                            imageVector = Icons.Default.MoreVert,
//                            contentDescription = "More options",
//                            tint = Color.White
//                        )
//                    }
//                }
//            )
//        },
//        containerColor = Color.White
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .padding(paddingValues)
//                .fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            // Logo Section
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                // Government logo placeholder
//                Image(
//                    painter = painterResource(id = R.drawable.baseline_add_location_24),
//                    contentDescription = "Government Logo",
//                    modifier = Modifier.size(72.dp)
//                )
//            }
//
//            // Support Message
//            Text(
//                text = "Geo is here for you ...",
//                style = MaterialTheme.typography.titleLarge,
//                fontWeight = FontWeight.Medium,
//                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
//            )
//
//            Text(
//                text = "Help us report incidents happening in your area including locations",
//                style = MaterialTheme.typography.bodyLarge,
//                textAlign = TextAlign.Center,
//                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
//            )
//
//            // Report Button
//            val context = LocalContext.current
//            Box(
//                modifier = Modifier
//                    .padding(24.dp)
//                    .size(200.dp)
//                    .clip(CircleShape)
//                    .background(MaterialTheme.colorScheme.primaryContainer)
//                    .clickable {
//                        Toast.makeText(context, "Navigating to report screen", Toast.LENGTH_SHORT).show()
//                    },
//                contentAlignment = Alignment.Center
//            ) {
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Phone,
//                        contentDescription = "Report",
//                        tint = Color.White,
//                        modifier = Modifier.size(48.dp)
//                    )
//                    Text(
//                        text = "REPORT",
//                        color = Color.White,
//                        style = MaterialTheme.typography.titleLarge,
//                        fontWeight = FontWeight.Bold,
//                        modifier = Modifier.padding(top = 8.dp)
//                    )
//                }
//            }
//
//            // Discover More Section
////            Card(
////                modifier = Modifier
////                    .fillMaxWidth()
////                    .padding(16.dp)
////                    .clickable { /* Discover more action */ },
////                colors = CardDefaults.cardColors(
////                    containerColor = Color.White
////                ),
////                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
////            ) {
////                Row(
////                    modifier = Modifier
////                        .padding(16.dp)
////                        .fillMaxWidth(),
////                    verticalAlignment = Alignment.CenterVertically
////                ) {
////                    Image(
////                        painter = painterResource(id = R.drawable.baseline_map_24),
////                        contentDescription = "Health Information",
////                        modifier = Modifier
////                            .size(60.dp)
////                            .clip(RoundedCornerShape(4.dp)),
////                        contentScale = ContentScale.Crop
////                    )
////
////                    Column(
////                        modifier = Modifier.padding(start = 16.dp)
////                    ) {
////                        Text(
////                            text = "Discover more",
////                            style = MaterialTheme.typography.titleMedium,
////                            fontWeight = FontWeight.Bold
////                        )
////                        Text(
////                            text = "Watch videos, learn about your health & play fun quizzes",
////                            style = MaterialTheme.typography.bodyMedium,
////                            color = Color.Gray
////                        )
////                    }
////                }
////            }
////
////            // Additional Resources Section
////            Card(
////                modifier = Modifier
////                    .fillMaxWidth()
////                    .padding(16.dp),
////                colors = CardDefaults.cardColors(
////                    containerColor = Color(0xFFF5F5F5)
////                ),
////                shape = RoundedCornerShape(12.dp)
////            ) {
////                Column(
////                    modifier = Modifier.padding(16.dp)
////                ) {
////                    Text(
////                        text = "Resources",
////                        style = MaterialTheme.typography.titleMedium,
////                        fontWeight = FontWeight.Bold,
////                        modifier = Modifier.padding(bottom = 8.dp)
////                    )
////
////                    ResourceItem(
////                        icon = Icons.Default.LocationOn,
////                        title = "Find Help Near You",
////                        description = "Locate support centers in your area"
////                    )
////
////                    Divider(modifier = Modifier.padding(vertical = 8.dp))
////
////                    ResourceItem(
////                        icon = Icons.Default.Call,
////                        title = "Helpline",
////                        description = "24/7 confidential support"
////                    )
////
////                    Divider(modifier = Modifier.padding(vertical = 8.dp))
////
////                    ResourceItem(
////                        icon = Icons.Default.Notifications,
////                        title = "Educational Materials",
////                        description = "Learn about safety and prevention"
////                    )
////                }
////            }
//        }
//    }
//}












package com.example.posapp

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dashboard(navHostController: NavHostController) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val surfaceColor = MaterialTheme.colorScheme.surface
    var showMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val activity = (context as? Activity)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.munilogo1),
                            contentDescription = "Geo Traffic Safety Logo",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color.White, CircleShape),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Geo Traffic",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor,
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options",
                            tint = Color.White
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = {showMenu = false},
                        modifier = Modifier
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text("Exit")
                            },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Exit App")
                            },
                            onClick = {
                                showMenu = false
                                activity?.finish()
                            }
                        )
                    }
                }
            )
        },
        containerColor = surfaceColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Hero section with map illustration
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .padding(16.dp)
            ) {
                // Map background image
                Image(
                    painter = painterResource(id = R.drawable.navimage),
                    contentDescription = "Location Map",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Semi-transparent overlay with text
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Color.Black.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Column {
                        Text(
                            text = "Report Incidents in Real-Time",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Help make your community safer",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            // Main message
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Geo Traffic is here for you",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Help us improve road safety by reporting traffic incidents and hazards in your area",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                }
            }

            // Report Button
            val context = LocalContext.current
            Box(
                modifier = Modifier
                    .padding(24.dp)
                    .size(160.dp)
                    .clip(CircleShape)
                    .background(primaryColor)
                    .clickable {
                        navHostController.navigate("addIncident")
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Report Incident",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = "REPORT",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DashboardPreview() {
    MaterialTheme {
        Dashboard(rememberNavController())
    }
}