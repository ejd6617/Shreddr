package com.example.shreddr.view

import android.widget.Toast
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.example.shreddr.controller.ChordChartController
import com.example.shreddr.controller.ChordChartController.Companion.setSelectedChordChart
import com.example.shreddr.model.ChordChart

class ManageChordChartsScreen(private val navController: NavController, private val chordChartController: ChordChartController, private val editChordChartScreen: EditChordChartScreen)
{

    private var chordCharts = mutableStateListOf<ChordChart>()
    private var listUpdate = mutableStateOf(false)



    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun manageChordChartsScreen() {
        chordChartController.getUserChordCharts() { chordCharts ->
           this.chordCharts = chordCharts.toMutableStateList()

        }
        val scrollState = rememberScrollState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Manage Chord Charts", color = Color(0xFFFDDCA9)) },
                    modifier = Modifier.background(Color(0xFF562717)),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF562717)
                    )


                )
            },
            content = { innerPadding ->
                LazyColumn(modifier = Modifier.padding(innerPadding).background(Color(0xFFFDDCA9))) // Lazy columns allow for scrolling
                {
                    //for each chordChart
                    items(chordCharts.size) { index ->
                        chordChartItem(chordCharts[index])
                    }

                    if(listUpdate.value)
                    {
                        //detects if the list has been altered, and if so, triggers recomposition by changing value
                        listUpdate.value = false
                    }
                }
            },
            bottomBar = {

                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp).background(Color(0xFFFEA712)),
                    horizontalArrangement = Arrangement.SpaceBetween
                )
                {
                    IconButton(onClick = { navController.navigate("searchScreen") })
                    {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Go back", tint = Color(0xFFFDDCA9))
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
                IconButton(onClick = { showDialog = true }) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = Color(0xFFFDDCA9)
                    )
                }
                IconButton(onClick = {
                    editChordChartScreen.setValue(chart) //edits the fields for the edit chord chart screen
                    navController.navigate("editChordChartScreen")
                }) {
                    Icon(
                        Icons.Filled.Create,
                        contentDescription = "Edit Chart",
                        tint = Color(0xFFFDDCA9)
                    )
                }
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


}