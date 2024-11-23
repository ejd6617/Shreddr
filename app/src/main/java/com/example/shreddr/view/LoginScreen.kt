package com.example.shreddr.view

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
            modifier = Modifier.width(300.dp)
        )
        Spacer(modifier = Modifier.height(16.dp)) // Add some space between fields
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(), // Hide password
            modifier = Modifier.width(300.dp)
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

    Button(
        onClick =
        {
            loginController.loginUser(email, password) //calls the login function in the controller
        },
        modifier = Modifier
            .padding(16.dp)
            .width(300.dp),
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
            .width(300.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
        shape = RoundedCornerShape(8.dp)
    ) { Text("Register New Account", color = Color.White)}

}