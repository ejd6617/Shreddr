package com.example.shreddr.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shreddr.controller.ChordChartController.Companion.getSelectedChordChart
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import coil.compose.SubcomposeAsyncImage

class DisplayScreen(private val navController: NavController) {

    fun getImageUrl(chord: String): String{
        when (chord) {
            "C" -> return "https://chordgenerator.net/C.png?p=x32010&f=-32-1-&s=2&b=false"
            "C#" -> return "https://chordgenerator.net/C%23.png?p=xx3121&f=--3121&s=2&b=false"
            "D" -> return "https://chordgenerator.net/D.png?p=xx0232&f=---132&s=2&b=false"
            "D#" -> return "https://chordgenerator.net/D%23.png?p=xx1343&f=--1243&s=2&b=false"
            "E" -> return "https://chordgenerator.net/E.png?p=022100&f=-231--&s=2&b=false"
            "F" -> return "https://chordgenerator.net/F.png?p=133211&f=134211&s=2&b=false"
            "F#" -> return "https://chordgenerator.net/F%23.png?p=133211&f=134211&s=2&b=false"
            "G" -> return "https://chordgenerator.net/G.png?p=320033&f=21--34&s=2&b=false"
            "G#" -> return "https://chordgenerator.net/G%23.png?p=xx1114&f=--1114&s=2&b=false"
            "A" -> return "https://chordgenerator.net/A.png?p=x02220&f=--123-&s=2&b=false"
            "A#" -> return "https://chordgenerator.net/A%23.png?p=xx3331&f=--2341&s=2&b=false"
            "B" -> return "https://chordgenerator.net/B.png?p=x2444x&f=-1333-&s=2&b=false"
        }
        return ""
    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    @Composable
    fun displayScreen() {
        val chordChart = getSelectedChordChart()
        val showDialog = remember { mutableStateOf(false) }
        val dropdownExpanded = remember { mutableStateOf(false) }
        val selectedKey = remember { mutableStateOf("") }
        val possibleKeys = listOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")

        // State to handle image popup
        val showImageDialog = remember { mutableStateOf(false) }
        val imageUrl = remember { mutableStateOf("") }

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
                            Column {
                                Box(
                                    modifier = Modifier
                                        .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
                                        .padding(8.dp)
                                        .clickable(onClick = {
                                            imageUrl.value = getImageUrl(pair.chords) // Assuming the chord object has an image URL
                                            showImageDialog.value = true
                                        }),
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


                if (showImageDialog.value) {
                    Dialog(onDismissRequest = { showImageDialog.value = false }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .background(Color.White, shape = RoundedCornerShape(8.dp))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                SubcomposeAsyncImage(
                                    model = imageUrl.value,
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.fillMaxWidth(),
                                    loading = {
                                        Text("Loading...", color = Color.Gray, modifier = Modifier.padding(8.dp))
                                    }
                                )

                                Spacer(modifier = Modifier.padding(8.dp))

                                Button(onClick = { showImageDialog.value = false }) {
                                    Text("Close")
                                }
                            }
                        }
                    }
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
