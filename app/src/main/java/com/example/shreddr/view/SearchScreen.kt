package com.example.shreddr.view

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shreddr.controller.ChordChartController
import com.example.shreddr.controller.ChordChartController.Companion.setSelectedChordChart
import com.google.firebase.auth.FirebaseAuth
import com.example.shreddr.controller.UserController
import com.example.shreddr.model.ChordChart


class SearchScreen(
    private val navController: NavController,
    private val userController: UserController,
    private var context: Context?,
    private var chordChartController: ChordChartController,
    private val editChordChartScreen: EditChordChartScreen)
{
    private var searchName = mutableStateOf("")
    private var searchGenre = mutableStateOf("")
    private var searchArtist = mutableStateOf("")
    private var chordCharts = mutableStateListOf<ChordChart>()
    private var listUpdate = mutableStateOf(false)


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
                            userController.deleteUser() {
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
    fun searchScreen() {
        // Track if data has been updated
        var isDataUpdated by remember { mutableStateOf(false) }

        // This state will hold the fetched chord charts
        val chordCharts = remember { mutableStateListOf<ChordChart>() }

        // This will be triggered when the search button is clicked
        var searchKey by remember { mutableStateOf("") }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Search", color = Color(0xFFFDDCA9)) },
                    modifier = Modifier.background(Color(0xFF562717)),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF562717)
                    ),
                    actions = {
                        IconButton(onClick = { /* Handle toggle */ }) {
                            Icon(Icons.Filled.MoreVert, contentDescription = "Toggle Tab", tint = Color(0xFFFDDCA9))
                        }
                    }
                )
            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .background(Color(0xFFFDDCA9))
                ) {
                    Text("Search Criteria", fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFDDCA9)),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Search input fields (Name, Genre, Artist)
                        OutlinedTextField(
                            value = searchName.value,
                            onValueChange = { searchName.value = it },
                            label = { Text("Name") },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp),
                            colors = outlinedTextFieldColors(
                                focusedBorderColor = Color(0xFFE76219),
                                unfocusedContainerColor = Color(0xFFE76219),
                                focusedContainerColor = Color(0xFFE76219),
                                unfocusedTextColor = Color(0xFFFDDCA9),
                                focusedTextColor = Color(0xFFFDDCA9),
                            )
                        )

                        OutlinedTextField(
                            value = searchGenre.value,
                            onValueChange = { searchGenre.value = it },
                            label = { Text("Genre") },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp),
                            colors = outlinedTextFieldColors(
                                focusedBorderColor = Color(0xFFE76219),
                                unfocusedContainerColor = Color(0xFFE76219),
                                focusedContainerColor = Color(0xFFE76219),
                                unfocusedTextColor = Color(0xFFFDDCA9),
                                focusedTextColor = Color(0xFFFDDCA9),
                            )
                        )

                        OutlinedTextField(
                            value = searchArtist.value,
                            onValueChange = { searchArtist.value = it },
                            label = { Text("Artist") },
                            modifier = Modifier
                                .weight(1f),
                            colors = outlinedTextFieldColors(
                                focusedBorderColor = Color(0xFFE76219),
                                unfocusedContainerColor = Color(0xFFE76219),
                                focusedContainerColor = Color(0xFFE76219),
                                unfocusedTextColor = Color(0xFFFDDCA9),
                                focusedTextColor = Color(0xFFFDDCA9),
                            )
                        )

                        // Search Button to trigger fetch and refresh data
                        IconButton(
                            onClick = {
                                // Set data updated flag to trigger recomposition
                                isDataUpdated = true

                                // Fetch chord charts based on search criteria
                                chordChartController.getChordChartsByCriteria(name = searchName.value, genre = searchGenre.value, artist = searchArtist.value) { fetchedChordCharts ->
                                    // Update state with new fetched data
                                    chordCharts.clear()
                                    chordCharts.addAll(fetchedChordCharts)
                                }
                            },
                            modifier = Modifier.weight(1f / 4f)
                        ) {
                            Icon(Icons.Filled.Search, contentDescription = "Search")
                        }
                    }

                    // Wait until the data is updated and trigger a recomposition
                    LaunchedEffect(isDataUpdated) {
                        if (isDataUpdated) {
                            // Set to false to stop refreshing (prevents unnecessary recomposition)
                            isDataUpdated = false
                        }
                    }

                    // Display the fetched chord charts in a LazyColumn
                    LazyColumn(modifier = Modifier.padding(innerPadding).background(Color(0xFFFDDCA9))) {
                        items(chordCharts.size) { index ->
                            chordChartItem(chordCharts[index])
                        }
                    }
                }
            },
            bottomBar = {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp).background(Color(0xFFFEA712)),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { navController.navigate("manageChordChartScreen") }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings", tint = Color(0xFF562717))
                    }
                    FloatingActionButton(onClick = {
                        navController.navigate("addChordChartScreen") }, containerColor = Color(0xFFC21717), contentColor = Color(0xFFFDDCA9)) {
                        Icon(Icons.Filled.Add, contentDescription = "Add")
                    }
                }
            },
            containerColor = Color(0xFF562717)
        )
    }



    @Composable
    fun chordChartItem(chart: ChordChart) {
        val context = LocalContext.current
        val updateScreen = remember { mutableStateOf(false) }

        var showDialog by remember { mutableStateOf(false) }

        Column(modifier = Modifier.padding(16.dp).border(2.dp, Color.Black, shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp)).background(Color(0xFFE76219)))
        {
            Text("  Chart Author: ${chart.author}", color = Color(0xFFFDDCA9))
            Text("  Song Name: ${chart.name}", fontWeight = FontWeight.Bold, color = Color(0xFFFDDCA9))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Start
            ) {

                // Fixed navigation with argument passing
                setSelectedChordChart(chart)
                IconButton(onClick = {
                    setSelectedChordChart(chart)
                    navController.navigate("displayChordChartScreen")
                }) {
                    Icon(Icons.Filled.ExitToApp, contentDescription = "View Chart", tint = Color(0xFFFDDCA9))
                }
            }
        }

        if (showDialog)
        {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Confirmation") },
                text = { Text("Are you sure you want to delete this chart?\nChart: ${chart.name}\nThis action cannot be undone.") },
                confirmButton = {
                    Button(
                        onClick = {
                            chordChartController.deleteChordChart(chart.chartID) { onResult ->
                                when(onResult)
                                {
                                    0 -> {
                                        chordCharts = chordCharts.filter { it != chart }.toMutableStateList()//instead of removing from list, filter instead to trigger recomposition
                                        listUpdate.value = true//triggers recomposition
                                        Toast.makeText(context, "Chart deleted successfully", Toast.LENGTH_SHORT).show()

                                    }
                                    -1 -> {
                                        Toast.makeText(context, "Failed to delete chart", Toast.LENGTH_SHORT).show()
                                    }

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











