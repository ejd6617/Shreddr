package com.example.shreddr.view

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shreddr.controller.ChordChartController
import com.example.shreddr.model.ChordChart

class ManageChordChartsScreen(private val navController: NavController, private val chordChartController: ChordChartController) {

    private var chordCharts = mutableStateListOf<ChordChart>()


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun manageChordChartsScreen() {
        chordChartController.getUserChordCharts() { chordCharts ->
           this.chordCharts = chordCharts.toMutableStateList()
        }


        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Manage Chord Charts") },

                )
            },
            content = { innerPadding ->
                LazyColumn(modifier = Modifier.padding(innerPadding))
                {
                    //for each chordChart
                    items(chordCharts.size) { index ->
                        chordChartItem(chordCharts[index])
                    }
                }
            },
            bottomBar = {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                )
                {
                    IconButton(onClick = { navController.navigate("searchScreen") })
                    {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Go back")
                    }
                }
            }
        )


    }

    @Composable
    fun chordChartItem(chart: ChordChart) {
        val context = LocalContext.current

        var showDialog by remember { mutableStateOf(false) }

        Column(modifier = Modifier.padding(16.dp))
        {
            Text("Chart Author: ${chart.author}")
            Text("Song Name: ${chart.name}", fontWeight = FontWeight.Bold)
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.Start)
            {
                IconButton(onClick = { showDialog = true })
                {Icon(Icons.Filled.Delete, contentDescription = "Delete")}
                IconButton(onClick = { })
                {Icon(Icons.Filled.Create, contentDescription = "Delete")}

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
                           chordChartController.deleteChordChart(chart.chartID) {onResult ->
                               when(onResult)
                               {
                                   0 -> {
                                       chordCharts.remove(chart)
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