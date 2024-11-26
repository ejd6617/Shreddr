package com.example.shreddr.view


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shreddr.controller.UserController

class RegistrationScreen(
    private val navController: NavController,
    private val userController: UserController,
    private var context: Context?
) {


    @Composable
    fun registrationScreen() {
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }



        Column {
            Spacer(modifier = Modifier.height(300.dp))
            OutlinedTextField(

                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp)) // Add some space between fields
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(), // Hide password
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp)) // Add some space between fields
            registerButton(username, password)
            Spacer(modifier = Modifier.height(8.dp)) // Add some space between fields
            goBackButton()


        }
    }

    @Composable
    fun registerButton(email: String, password: String) {


        Button(
            onClick = {

                userController.registerUser(email, password)  { errorCode ->
                    if (errorCode == 0) {
                        // Login successful
                        navController.navigate("searchScreen")
                    } else {
                        // Handle login failure
                        Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show()
                    }
                }//calls the register user function from the controller
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Register", color = Color.White)
        }
    }

    @Composable
    fun goBackButton() {
        Button(
            onClick = {
                navController.navigate("signInWindow")// goes back to the sign in window

            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            shape = RoundedCornerShape(8.dp)
        ) { Text("Go Back", color = Color.White) }
    }
}