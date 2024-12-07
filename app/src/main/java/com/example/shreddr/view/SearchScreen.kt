package com.example.shreddr.view

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.example.shreddr.controller.UserController


class SearchScreen(
    private val navController: NavController,
    private val userController: UserController,
    private var context: Context?)
{
    @Composable
    fun DeleteUserButton() // implementation of the delete user button
    {
        context = LocalContext.current
        var showDialog by remember { mutableStateOf(false) }
        Button(
            onClick = {
                showDialog = true

            },
            modifier = Modifier.padding(16.dp).fillMaxWidth().shadow(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = RoundedCornerShape(8.dp)
        ) {Text("Delete Account", color = Color.White)}

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Confirmation") },
                text = { Text("Are you sure you would like to delete your account?") },
                confirmButton = {
                    Button(
                        onClick = {
                            //this syntax is everywhere - what's going on is that Firebase API calls are asyncronous operations, so we need to wait for them to finish before continuing.
                            //the deleteUser function with it's last argument essentailly makes the API call an syncronous operation, so that the progrom waits for it's completeion before continuing.
                            //if this wasnt present, the function would return multiple times with different values, so this makes the app run smoother
                            userController.deleteUser {
                                    errorCode  ->
                                //this is switch statement syntax
                                when (errorCode) {
                                    0 -> navController.navigate("signInWindow")
                                    -1 -> Toast.makeText(
                                        context,
                                        "No user logged in",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    -2 -> Toast.makeText(
                                        context,
                                        "Error in deleting from database",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    -3 -> Toast.makeText(
                                        context,
                                        "Error in deleting from auth",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    1 -> Toast.makeText(
                                        context,
                                        "Something else happened",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            showDialog = false
                        }) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("No")
                    }
                }
            )
        }

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun searchScreen()
    {
        // Remember the state to control visibility of the tab
        var isTabVisible by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Search") },
                    actions = {
                        IconButton(onClick = { isTabVisible = !isTabVisible }) {
                            Icon(Icons.Filled.MoreVert, contentDescription = "Toggle Tab")
                        }
                    }
                )
            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    // Show the tab only when `isTabVisible` is true
                    if (isTabVisible) {
                        TabContent()
                    }
                    Spacer(modifier = Modifier.weight(1f)) // Filler space
                }
            },
            bottomBar = {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = {navController.navigate("manageChordChartScreen") }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                    FloatingActionButton(onClick = {
                        navController.navigate("addChordChartScreen") }) {
                        Icon(Icons.Filled.Add, contentDescription = "Add")
                    }
                }
            }
        )
    }

    @Composable
    fun TabContent() {
        val userData = FirebaseAuth.getInstance().currentUser

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text ="Account Management", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            Text("${userData?.email}")
            Spacer(modifier = Modifier.height(16.dp))
            LogoutButton()
            DeleteUserButton()
        }
    }

    @Composable
    fun LogoutButton()
    {
        Button(
            onClick = {
                navController.navigate("signInWindow")// navigates back to the login screen
            },
            modifier = Modifier.padding(16.dp).fillMaxWidth().shadow(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = RoundedCornerShape(8.dp)
        ) {Text("Log Out", color = Color.White)}

    }

}











