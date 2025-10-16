package com.isichi001.isichi001fitness

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.isichi001.isichi001fitness.ui.theme.Isichi001FitnessTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Isichi001FitnessTheme {
                // ✅ Single, clean entry point
                FitnessAppNavigation()
            }
        }
    }
}

/* ------------------------- NAVIGATION GRAPH ------------------------- */
@Composable
fun FitnessAppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onNavigateToTimer = { navController.navigate("focusTimer") },
                onNavigateToReflection = { navController.navigate("reflection") }
            )
        }
        composable("focusTimer") {
            FocusTimerScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable("reflection") {
            ReflectionScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}

/* ------------------------- HOME SCREEN ------------------------- */
@Composable
fun HomeScreen(
    onNavigateToTimer: () -> Unit,
    onNavigateToReflection: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD6EAF8))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Greeting
        Column {
            Text(
                text = "Hello, Laurreine 👋",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(text = "\"Stay focused and consistent!\"")
        }

        // Progress section
        Column {
            Text(text = "Today's Progress", fontWeight = FontWeight.SemiBold)
            LinearProgressIndicator(
                progress = 0.6f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .padding(vertical = 8.dp),
                color = Color(0xFF3498DB)
            )
        }

        // Row with quick stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatCard(title = "Tasks", value = "5")
            StatCard(title = "Focus Hours", value = "2.5h")
        }

        // Task list
        Column {
            Text(text = "Today's Tasks", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            TaskItem("Study for Algorithms Exam")
            TaskItem("Watch Networking Lecture")
            TaskItem("Group Project Research")
        }

        // Navigation buttons
        Column {
            Button(
                onClick = onNavigateToTimer,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color(0xFF3498DB))
            ) { Text("Go to Focus Timer") }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onNavigateToReflection,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color(0xFF9B59B6))
            ) { Text("Go to Reflection") }
        }
    }
}

@Composable
fun StatCard(title: String, value: String) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .padding(4.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, fontWeight = FontWeight.SemiBold)
            Text(text = value, fontSize = 20.sp, color = Color(0xFF3498DB))
        }
    }
}

@Composable
fun TaskItem(task: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = task,
            modifier = Modifier.padding(10.dp)
        )
    }
}

/* ------------------------- FOCUS TIMER SCREEN ------------------------- */
@Composable
fun FocusTimerScreen(onNavigateBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD4EFDF))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Focus Timer ⏳", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))
        Text("00:25:00", fontSize = 40.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = { }) { Text("Start") }
            Button(onClick = { }) { Text("Pause") }
            Button(onClick = { }) { Text("Reset") }
        }

        Spacer(modifier = Modifier.height(25.dp))
        Button(onClick = onNavigateBack) { Text("Back to Home") }
    }
}

/* ------------------------- REFLECTION SCREEN ------------------------- */

@Composable
fun ReflectionScreen(onNavigateBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8DAEF))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Weekly Reflection 🧠", fontSize = 22.sp, fontWeight = FontWeight.Bold)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text("[Bar Chart Placeholder]")
            }
        }

        Column {
            Text("Summary", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("• Total study time: 14h")
            Text("• Completed tasks: 12")
            Text("• Average focus: 78%")
        }

        Button(
            onClick = onNavigateBack,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(Color(0xFF9B59B6))
        ) { Text("Back to Home") }
    }
}

/* ------------------------- PREVIEWS ------------------------- */
// Previews call the screens with stub lambdas so they compile
@Preview(showBackground = true)
@Composable
fun HomePreview() { HomeScreen({}, {}) }

@Preview(showBackground = true)
@Composable
fun TimerPreview() { FocusTimerScreen({}) }

@Preview(showBackground = true)
@Composable
fun ReflectionPreview() { ReflectionScreen({}) }
