package com.isichi001.isichi001fitness

import android.R.attr.onClick
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
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

/* ------------------------- NAVIGATION ------------------------- */
@Composable
fun FitnessAppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"  // 👈 start from login page
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { navController.navigate("home") },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate("home") },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }
        composable("home") {
            HomeScreen(
                onNavigateToTimer = { navController.navigate("focusTimer") },
                onNavigateToReflection = { navController.navigate("reflection") },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
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
    onNavigateToReflection: () -> Unit,
    onLogout: () -> Unit
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
            Button(
                onClick = { onLogout() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color(0xFFE74C3C))
            ) {
                Text("Logout")
            }

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
fun TimerPreview() { FocusTimerScreen({}) }

@Preview(showBackground = true)
@Composable
fun ReflectionPreview() { ReflectionScreen({}) }
/* ------------------------- LOGIN SCREEN ------------------------- */
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var email = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome Back 👋", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onLoginSuccess,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(Color(0xFF1976D2))
        ) { Text("Login") }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = onNavigateToRegister) {
            Text("Don't have an account? Register")
        }
    }
}

/* ------------------------- REGISTER SCREEN ------------------------- */
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var name = remember { mutableStateOf("") }
    var email = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF3E0))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Create Account ✨", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("Full Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRegisterSuccess,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(Color(0xFFF57C00))
        ) { Text("Register") }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = onNavigateToLogin) {
            Text("Already have an account? Login")
        }
    }
}
