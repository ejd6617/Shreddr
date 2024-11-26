package com.example.shreddr.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth


//This is not done yet
class AddChordChartScreen {

    private var songName =  { mutableStateOf("") }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun addChordChartScreen(navController: NavController)
    {
        var songName = remember { mutableStateOf("") }
        val context = LocalContext.current
        var isTabVisible by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {Text("Add Chord Chart")},
                    actions =
                    {
                        IconButton(onClick = { isTabVisible = !isTabVisible })
                        {Icon(Icons.Filled.Menu, contentDescription = "Chord Chart Data")}
                    }
                )
            },
            content = { innerPadding ->
                Column(
                    modifier = androidx.compose.ui.Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    // Show the tab only when `isTabVisible` is true
                    if (isTabVisible) {
                        //com.example.shreddr.view.TabContent(navController, songName)
                    }
                    Spacer(modifier = androidx.compose.ui.Modifier.weight(1f)) // Filler space
                }
            }
        )
    }

    @Composable
    fun TabContent(navController: NavController)
    {
        val currentUser = FirebaseAuth.getInstance().currentUser

        Column(
            verticalArrangement = Arrangement.Center
        )
        {
            Text("Author: ${currentUser?.email}")


        }
    }


}
