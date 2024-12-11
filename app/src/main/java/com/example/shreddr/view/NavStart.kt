package com.example.shreddr.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shreddr.controller.ChordChartController
import com.example.shreddr.controller.UserController


import com.example.shreddr.ui.theme.ShreddrTheme

//this class controls the navigation flow between screens. It also defines the start screen "signInWindow"
class NavStart : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ShreddrTheme {
                //initalizing nav controller
                val navController = rememberNavController()


                //initalizing controllers

                val chordChartController = ChordChartController()
                val userController = UserController(chordChartController)


                //initalizing screens
                val loginScreen = LoginScreen(navController, userController, null)
                val registrationScreen = RegistrationScreen(navController, userController, null)
                val addChordChartScreen = AddChordChartScreen(navController, chordChartController)
                val editChordChartScreen = EditChordChartScreen(navController, chordChartController)
                val manageChordChartsScreen = ManageChordChartsScreen(navController, chordChartController, editChordChartScreen)
                val searchScreen = SearchScreen(navController, userController, null)




                NavHost(navController = navController, startDestination = "signInWindow") { //assigns the start screen to the "signInWindow" tag
                    composable("signInWindow") { loginScreen.loginScreen() } // defines the "signInWindow" tag as the LoginScreen function of the LoginScreen.kt class.
                    composable("registrationWindow") { registrationScreen.registrationScreen() } // defines the "registrationWindow" tag as the RegistrationScreen function of the RegistrationScreen.kt class
                    composable("searchScreen") { searchScreen.searchScreen() } // defines the "searchScreen" tag as the searchScreen function of the com.example.shreddr.view.SearchScreen.kt class
                    composable("testScreen") { testScreen(navController) } // defines the "testScreen" tag as the testScreen function of the testScreen.kt class (this is to test if navigation is working correctly)
                    composable("addChordChartScreen") { addChordChartScreen.addChordChartScreen() } // defines the "addChordChartScreen" tag as the addChordChartScreen function of the AddChordChartScreen")
                    composable("manageChordChartScreen") { manageChordChartsScreen.manageChordChartsScreen() } // defines the "manageChordChartScreen" tag as the manageChordChartsScreen function of the ManageChordChartsScreen")
                    composable("editChordChartScreen") { editChordChartScreen.editChordChartScreen() } // defines the "editChordChartScreen" tag as the editChordChartScreen function of the EditChordChartScreen")
                }
            }
        }
    }
}