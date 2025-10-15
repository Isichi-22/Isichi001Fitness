package com.isichi001.isichi001fitness

//import android.R.attr.name
import android.R.attr.text
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.isichi001.isichi001fitness.ui.theme.Isichi001FitnessTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Isichi001FitnessTheme {
                //Scaffold- layout component for material design layout
                // top app bar, bottom bar, drawer ...
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Greeting(
                    //   name = "Android",
                    //   modifier = Modifier.padding(innerPadding)
                    //)
                    Testlayout(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
@Composable
fun Testlayout(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Row (modifier = modifier.fillMaxSize()){
        Column (modifier = Modifier.fillMaxHeight().width(100.dp).background(Color.Red),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            repeat(6){
                //clickable image
                val context = LocalContext.current
                val dice_num = 1 + it
                //display a dice
                Image(
                    painter = painterResource(image_ids[it]),
                    contentDescription = "Dice $it")
                modifier.clickable{
                    Toast.makeText(context, "Click dice $it", Toast.LENGTH_SHORT).show()

                }


                Image(painter = painterResource(image_ids[it]),
                    contentDescription = "Dice $it")

                Image(painter = painterResource(image_ids[it]),
                    contentDescription = "Dice $it")

                Image(painter = painterResource(image_ids[it]),
                    contentDescription = "Dice $it")

                Image(painter = painterResource(image_ids[it]),
                    contentDescription = "Dice $it")


            }
        }
        Column (modifier = Modifier.fillMaxHeight().width(100.dp).background(Color.Yellow),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Column 2")
        }
        Column (modifier = Modifier.fillMaxHeight().fillMaxWidth().background(Color.Green),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
           // Text(text = "Column 3")
            Button(onClick =  {
                Toast.makeText(context, "You clicked me", Toast.LENGTH_SHORT).show()
            })  {
                    Text("Click me")
                }
            }
        }
    }


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Surface(color = Color.Green) {
        Text(
            text = "Hello, $name!",
            modifier = modifier .padding(24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Isichi001FitnessTheme {
        Greeting("Android")
    }
}

private val image_ids = listOf (
    R.drawable.dice_1,
    R.drawable.dice_2,
    R.drawable.dice_3,
    R.drawable.dice_4,
    R.drawable.dice_5,
    R.drawable.dice_6
)

