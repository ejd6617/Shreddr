package com.example.shreddr.view

import android.content.ContentValues.TAG
import android.util.Log
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.example.shreddr.controller.LoginController



@Composable
fun loginScreen(navController: NavController) {
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
        loginButton(navController, username, password)
        Spacer(modifier = Modifier.height(16.dp)) // Add some space between fields
        registerNewAccountButton(navController)
    }
}

@Composable
fun loginButton(navController: NavController, email: String, password: String)
{
    val loginController = remember { LoginController(navController) }
    val context = LocalContext.current

    Button(
        onClick =
        {
           loginController.loginUser(email, password) { errorCode ->
                if (errorCode == 0) {
                    // Login successful
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
fun registerNewAccountButton(navController: NavController)
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