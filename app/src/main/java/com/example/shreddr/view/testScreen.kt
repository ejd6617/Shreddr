package com.example.shreddr.view

// Import packages
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shreddr.R

@Composable
fun testScreen(navController: NavController)
{
    Box(modifier = Modifier.fillMaxSize()) { //Creates a Box object to store the image and other composable objects
        Image(
            painter = painterResource(id = R.drawable.ternip),
            contentDescription = "background",
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )
        // Other composable objects (like the button) can go here
        Column(modifier = Modifier.align(Alignment.Center)) {
            secondButton(navController)
        }
    }

}

@Composable
fun secondButton(navController: NavController)
{
   Button(
       onClick = { navController.navigate("signInWindow") },
       modifier = Modifier.padding(16.dp).fillMaxWidth().shadow(8.dp),
       colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
       shape = RoundedCornerShape(8.dp)
   ) {Text("Go Back!", color = Color.Black)}
}