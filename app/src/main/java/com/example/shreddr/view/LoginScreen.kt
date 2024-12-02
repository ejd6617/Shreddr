package com.example.shreddr.view


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shreddr.R
import com.example.shreddr.controller.UserController

class LoginScreen(
    private val navController: NavController,
    private val userController: UserController,
    private var context: Context?
) {

    @Composable
    fun loginScreen() {

        background()


    }

    @Composable
    fun loginButton( email: String, password: String)
    {
        context = LocalContext.current

        Button(
            onClick =
            {
                userController.loginUser(email, password) { errorCode ->
                    if (errorCode == 0) {
                        // Login successful
                        navController.navigate("searchScreen")
                    } else {
                        // Handle login failure
                        Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            shape = RoundedCornerShape(8.dp)
        ) { Text("Login", color = Color.White) }

    }

    @Composable
    fun registerNewAccountButton()
    {
        Button(
            onClick = {
                navController.navigate("registrationWindow") //navigates to the Registration Screen

            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            shape = RoundedCornerShape(8.dp)
        ) { Text("Register New Account", color = Color.White)}

    }

    @Composable
    fun background()
    {
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Box(modifier = Modifier.fillMaxSize()) {
            // Background image
            val backgroundImage: Painter = painterResource(id = R.drawable.background)
            Image(
                painter = backgroundImage,
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize()
            )
            Column{
                Spacer(modifier = Modifier.height(50.dp))


                // Logo Image centered above the text fields
                Image(
                    painter = painterResource(id = R.drawable.logo), // Your logo image
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(300.dp) // Adjust size as needed
                        .align(Alignment.CenterHorizontally) // Center the image horizontally
                )
                OutlinedTextField(

                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth()
                        .background(Color.White),


                    )
                Spacer(modifier = Modifier.height(16.dp)) // Add some space between fields
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(), // Hide password
                    modifier = Modifier.fillMaxWidth()
                        .background(Color.White)
                )
                Spacer(modifier = Modifier.height(16.dp)) // Add some space between fields
                loginButton(username, password)
                Spacer(modifier = Modifier.height(16.dp)) // Add some space between fields
                registerNewAccountButton()

            }
        }
    }




}


