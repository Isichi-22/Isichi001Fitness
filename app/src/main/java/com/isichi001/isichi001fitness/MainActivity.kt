package com.isichi001.isichi001fitness

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.FocusInteraction
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
import com.isichi001.isichi001fitness.ui.theme.Isichi001FitnessTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Isichi001FitnessTheme {
                HomeScreen()
                //FocusTimerScreen()
                //ReflectionScreen()

            }
        }
    }
}

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD6EAF8))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column {
            Text(
                text = "Hello, Laurreine 👋",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(text = "\"Stay focused and consistent!\"")
        }

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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatCard(title = "Tasks", value = "5")
            StatCard(title = "Focus Hours", value = "2.5h")
        }

        Column {
            Text(text = "Today's Tasks", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            TaskItem("Study for Algorithms Exam")
            TaskItem("Watch Networking Lecture")
            TaskItem("Group Project Research")
        }

        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(Color(0xFF3498DB))
        ) {
            Text("Add Task")
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

@Composable
fun FocusTimerScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD4EFDF))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Focus Timer ⏳",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))


        Text(
            text = "00:50:00",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )

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

        Text(text = "How are you feeling?", fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            Text(text = "😊")
            Text(text = "😴")
            Text(text = "😕")
        }

        Spacer(modifier = Modifier.height(25.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Write a quick note about your session...")
            }
        }
    }
}

@Composable
fun ReflectionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8DAEF))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Weekly Reflection",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text(text = "[Bar Chart Placeholder]")
            }
        }

        Column {
            Text(text = "Summary", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("• Total study time: 14h")
            Text("• Completed tasks: 12")
            Text("• Average focus: 78%")
        }

        Column {
            Text(text = "Insights", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("• Your best focus time was between 9–11am.")
            Text("• Try reducing evening sessions for better rest.")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() { HomeScreen() }

@Preview(showBackground = true)
@Composable
fun TimerPreview() { FocusTimerScreen() }

@Preview(showBackground = true)
@Composable
fun ReflectionPreview() { ReflectionScreen() }
