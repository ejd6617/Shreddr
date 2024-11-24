package com.example.shreddr.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


import com.example.shreddr.ui.theme.ShreddrTheme
import com.google.firebase.FirebaseApp
import searchScreenfunction

//this class controls the navigation flow between screens. It also defines the start screen "signInWindow"
class NavStart : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ShreddrTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "signInWindow") { //assigns the start screen to the "signInWindow" tag
                    composable("signInWindow") { loginScreen(navController) } // defines the "signInWindow" tag as the loginScreen function of the LoginScreen.kt class (note: you do assign functions, not classes. None of our view "classes" actually have a class body, only functions.
                    composable("registrationWindow") { RegistrationScreen(navController) } // defines the "registrationWindow" tag as the RegistrationScreen function of the RegistrationScreen.kt class
                    composable("searchScreen") { searchScreenfunction(navController) } // defines the "searchScreen" tag as the searchScreen function of the SearchScreen.kt class
                    composable("testScreen") { testScreen(navController) } // defines the "testScreen" tag as the testScreen function of the testScreen.kt class (this is to test if navigation is working correctly)

                }
            }
        }
    }
}