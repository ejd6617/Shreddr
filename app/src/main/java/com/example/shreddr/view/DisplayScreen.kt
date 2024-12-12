package com.example.shreddr.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.example.shreddr.controller.ChordChartController.Companion.getSelectedChordChart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.foundation.shape.RoundedCornerShape

class DisplayScreen(private val navController: NavController) {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    @Composable
    fun displayScreen() {
        val chordChart = getSelectedChordChart()
        val showDialog = remember { mutableStateOf(false) }
        val dropdownExpanded = remember { mutableStateOf(false) }
        val selectedKey = remember { mutableStateOf("") }
        val possibleKeys = listOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(chordChart.name, color = Color(0xFFFDDCA9)) },
                    modifier = Modifier.background(Color(0xFF562717)),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF562717)
                    ),
                    actions = {
                        Button(
                            onClick = { showDialog.value = true },

                            modifier = Modifier
                                .background(Color(0xFF562717))
                                .padding(8.dp),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color(0xFFC21717), contentColor = Color(0xFFFDDCA9))
                        ) {
                            Text("Transpose", color = Color(0xFFFDDCA9))
                        }
                    }
                )
            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .background(Color(0xFFFDDCA9)),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Display the artist and key
                    Text("Artist: ${chordChart.artist}", color = Color.Black)
                    Text("Key: ${chordChart.key}", color = Color.Black)
                    Text("Genre: ${chordChart.genre}", color = Color.Black)
                    Spacer(modifier = Modifier.padding(8.dp))

                    // Display the chords and lyrics
                    chordChart.chordsAndLyrics.forEach { pair ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Column{
                                Box(
                                    modifier = Modifier
                                        .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
                                        .padding(8.dp)
                                        // .clickable { selectedChord = pair.chords }
                                        .clickable {},
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(pair.chords, color = Color.Black)
                                }
                                Text(pair.lyrics, color = Color.Black)
                            }
                        }
                    }
                }

                if (showDialog.value) {
                    AlertDialog(
                        onDismissRequest = { showDialog.value = false },
                        title = { Text("Select a Key") },
                        text = {
                            Column {
                                Button(
                                    onClick = { dropdownExpanded.value = !dropdownExpanded.value },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(if (selectedKey.value.isEmpty()) "Select Key" else selectedKey.value)
                                }
                                DropdownMenu(
                                    expanded = dropdownExpanded.value,
                                    onDismissRequest = { dropdownExpanded.value = false }
                                ) {
                                    possibleKeys.forEach { key ->
                                        DropdownMenuItem(
                                            text = { Text(text = key) },
                                            onClick = {
                                                selectedKey.value = key
                                                chordChart.transpose(key)
                                                dropdownExpanded.value = false
                                            }
                                        )
                                    }
                                }
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = { showDialog.value = false }
                            ) {
                                Text("Close")
                            }
                        }
                    )
                }
            },
            bottomBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(Color(0xFFFEA712)),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .background(Color(0xFFFEA712))
                            .padding(8.dp),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color(0xFFC21717), contentColor = Color(0xFFFDDCA9))
                    ) {
                        Text("Back")
                    }
                }
            },
            containerColor = Color(0xFF562717)
        )
    }
}
