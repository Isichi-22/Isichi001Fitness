package com.isichi001.StudyTimeApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import com.isichi001.StudyTimeApp.ui.theme.Isichi001FitnessTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Isichi001FitnessTheme {
                // Single, clean entry point
                FitnessAppNavigation()
            }
        }
    }
}

/* ------------------------- NAVIGATION + VIEWMODEL ROOT ------------------------- */
@Composable
fun FitnessAppNavigation() {
    // Build DB, Repository, and ViewModel once for the whole app
    val context = LocalContext.current
    val database = remember { StudyTimeDatabase.getDatabase(context) }
    val repository = remember { TaskRepository(database.taskDao()) }
    val taskViewModel: TaskViewModel = viewModel(
        factory = TaskViewModelFactory(repository)
    )

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
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
                viewModel = taskViewModel,
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
    viewModel: TaskViewModel,
    onNavigateToTimer: () -> Unit,
    onNavigateToReflection: () -> Unit,
    onLogout: () -> Unit
) {
    // Live tasks from the database
    val tasks by viewModel.allTasks.collectAsState()

    // State for "Add task" inputs
    var newTitle by remember { mutableStateOf("") }
    var newMinutesText by remember { mutableStateOf("") }

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

        // Progress section – based on real tasks
        Column {
            Text(text = "Today's Progress", fontWeight = FontWeight.SemiBold)
            val progress =
                if (tasks.isEmpty()) 0f
                else tasks.count { it.isCompleted }.toFloat() / tasks.size
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .padding(vertical = 8.dp),
                color = Color(0xFF3498DB)
            )
        }

        // Quick stats from real tasks
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val totalMinutes = tasks.sumOf { it.plannedMinutes }
            StatCard(title = "Tasks", value = tasks.size.toString())
            StatCard(title = "Focus Minutes", value = "${totalMinutes}m")
        }

        // Add Task section
        Column {
            Text(text = "Add Task", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = newTitle,
                onValueChange = { newTitle = it },
                label = { Text("Task title") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = newMinutesText,
                onValueChange = { newMinutesText = it },
                label = { Text("Planned minutes") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val minutes = newMinutesText.toIntOrNull() ?: 25
                    if (newTitle.isNotBlank()) {
                        viewModel.addTask(newTitle, minutes)
                        newTitle = ""
                        newMinutesText = ""
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color(0xFF3498DB))
            ) { Text("Add Task") }
        }

        // Task list – live from DB
        Column(
            modifier = Modifier
                .weight(1f, fill = true)
                .padding(top = 8.dp)
        ) {
            Text(text = "Today's Tasks", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))

            if (tasks.isEmpty()) {
                Text("No tasks yet. Add one above 😊")
            } else {
                LazyColumn {
                    items(tasks) { task ->
                        TaskItem(
                            task = task,
                            onCheckedChange = { checked ->
                                viewModel.updateTask(task.copy(isCompleted = checked))
                            },
                            onDelete = { viewModel.deleteTask(task) }
                        )
                    }
                }
            }
        }

        // Navigation + logout
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

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onLogout,
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
fun TaskItem(
    task: Task,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = onCheckedChange
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = task.title,
                    fontWeight = if (task.isCompleted) FontWeight.SemiBold else FontWeight.Normal
                )
                Text(
                    text = "${task.plannedMinutes} min",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            TextButton(onClick = onDelete) {
                Text("Delete")
            }
        }
    }
}

/* ------------------------- FOCUS TIMER SCREEN (unchanged for now) ------------------------- */
@Composable
fun FocusTimerScreen(onNavigateBack: () -> Unit) {
    // 25-minute timer by default
    val totalSeconds = 25 * 60

    var remainingSeconds by remember { mutableStateOf(totalSeconds) }
    var isRunning by remember { mutableStateOf(false) }

    // Timer logic – runs when isRunning changes
    LaunchedEffect(isRunning) {
        while (isRunning && remainingSeconds > 0) {
            delay(1000L)
            remainingSeconds--
        }
        if (remainingSeconds == 0) {
            isRunning = false
        }
    }

    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    val timeText = String.format("%02d:%02d", minutes, seconds)

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
        Text(timeText, fontSize = 40.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = { isRunning = true }) { Text("Start") }
            Button(onClick = { isRunning = false }) { Text("Pause") }
            Button(onClick = {
                isRunning = false
                remainingSeconds = totalSeconds
            }) { Text("Reset") }
        }

        Spacer(modifier = Modifier.height(25.dp))
        Button(onClick = onNavigateBack) { Text("Back to Home") }
    }
}

/* ------------------------- REFLECTION SCREEN ------------------------- */
@Composable
fun ReflectionScreen(onNavigateBack: () -> Unit) {

    val context = LocalContext.current
    val database = remember { StudyTimeDatabase.getDatabase(context) }
    val repository = remember { TaskRepository(database.taskDao()) }
    val taskViewModel: TaskViewModel = viewModel(
        factory = TaskViewModelFactory(repository)
    )

    // Read all tasks from DB
    val tasks by taskViewModel.allTasks.collectAsState()

    // Stats
    val totalTasks = tasks.size
    val completedTasks = tasks.count { it.isCompleted }
    val totalMinutes = tasks.sumOf { it.plannedMinutes }
    val completionPercent =
        if (totalTasks == 0) 0f
        else completedTasks.toFloat() / totalTasks

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8DAEF))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Weekly Reflection 🧠", fontSize = 22.sp, fontWeight = FontWeight.Bold)

        // Simple "bar chart"
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text("Completion Rate", fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(completionPercent)
                            .background(Color(0xFF9B59B6), RoundedCornerShape(8.dp))
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(String.format("Completed: %.0f%%", completionPercent * 100))
            }
        }

        // Summary from REAL DB data
        Column {
            Text("Summary", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))

            Text("• Total tasks: $totalTasks")
            Text("• Completed tasks: $completedTasks")
            Text("• Total planned time: ${totalMinutes} min")
            Text("• Completion rate: ${"%.0f".format(completionPercent * 100)}%")
        }

        Button(
            onClick = onNavigateBack,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(Color(0xFF9B59B6))
        ) { Text("Back to Home") }
    }
}


/* ------------------------- PREVIEWS ------------------------- */
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
