package com.example.shreddr.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shreddr.controller.ChordChartController
import com.example.shreddr.model.ChordChart
import com.example.shreddr.model.ChordLyricPairs
import com.google.firebase.auth.FirebaseAuth

class EditChordChartScreen(private val navController:  NavController, private val  chordChartController: ChordChartController) {

    private  var author : String? = FirebaseAuth.getInstance().currentUser?.email
    private  var name = mutableStateOf("")
    private  var artist = mutableStateOf("")
    private  var key = mutableStateOf("")
    private  var chordsAndLyrics = mutableStateListOf<ChordLyricPairs>()
    private val songKeys = listOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")
    private var chartID = ""

    fun setValue(chart: ChordChart)
    {
        name = mutableStateOf(chart.getName())
        artist = mutableStateOf(chart.getArtist())
        key = mutableStateOf(chart.getKey())
        chordsAndLyrics =  mutableStateListOf(*chart.getChordsAndLyrics().toTypedArray()) // i have no idea why this works but it does???
        chartID = chart.getID()

    }





    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun editChordChartScreen()
    {


        var isTabVisible by remember { mutableStateOf(false) }
        val scrollState = rememberScrollState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Edit Chord Chart", color = Color(0xFFFDDCA9)) },
                    modifier = Modifier.background(Color(0xFF562717)),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF562717)
                    ),
                    actions =
                    {
                        IconButton(onClick = { isTabVisible = !isTabVisible })
                        { Icon(Icons.Filled.Menu, contentDescription = "Chord Chart Data", tint = Color(0xFFFDDCA9)) }
                    }
                )
            },
            content = { innerPadding ->
                Column(
                    modifier = androidx.compose.ui.Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .background(Color(0xFFFDDCA9))
                ) {
                    // Show the tab only when `isTabVisible` is true
                    if (isTabVisible) {

                        TabContent()
                    }
                    ChordsAndLyrics()
                    Spacer(modifier = androidx.compose.ui.Modifier.weight(1f)) // Filler space
                }
            },
            bottomBar = {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp).background(Color(0xFFFEA712)),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    backButton()
                    uploadButton()
                }
            },
            containerColor = Color(0xFF562717)
        )
    }

   @Composable
   fun backButton()
   {
       var backTabVisible by remember { mutableStateOf(false) }

       // FloatingActionButton to trigger the dialog visibility
       IconButton(onClick = { backTabVisible = true }) {
           Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go back", tint = Color(0xFFFDDCA9))
       }

       // Show AlertDialog when backTabVisible is true
       if (backTabVisible) {
           AlertDialog(
               onDismissRequest = { backTabVisible = false },
               title = { Text("Confirmation") },
               text = { Text("Go back to the search screen and discard all changes?") },
               confirmButton = {
                   Button(
                       onClick = {
                           navController.navigate("manageChordChartScreen")  // Navigate to the search screen
                           backTabVisible = false  // Close the dialog
                       }) {
                       Text("Yes")
                   }
               },
               dismissButton = {
                   Button(onClick = { backTabVisible = false }) {
                       Text("No")
                   }
               }
           )
       }

   }

    @Composable
    fun ChordsAndLyrics()
    {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp))
        {
            chordsAndLyrics.forEachIndexed { index, _ ->
                //display a text field for each member in the pair
                //first is chord, second is lyrics
                TextField(
                    value = chordsAndLyrics[index].getChords(), onValueChange = { newChord ->
                        chordsAndLyrics[index].changeChords(newChord)
                    },

                    label = { Text("Chord ${index + 1}") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )

                TextField(
                    value = chordsAndLyrics[index].getLyrics(), onValueChange = { newLyrics ->
                        chordsAndLyrics[index].changeLyrics(newLyrics)
                    },
                    label = { Text("Lyrics ${index + 1}") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = { addPair() },
                modifier = Modifier.fillMaxWidth(),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color(0xFFC21717), contentColor = Color(0xFFFDDCA9))
            )
            {  Text("Add Another Pair")}
            Button(onClick = { removePair() }, modifier = Modifier.fillMaxWidth(),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color(0xFFC21717), contentColor = Color(0xFFFDDCA9)))
            {
                Text("Remove Pair")
            }

        }

    }

    fun addPair()
    {
        chordsAndLyrics.add(ChordLyricPairs())
    }

    fun removePair()
    {
        chordsAndLyrics.removeLast()
    }


    @Composable
    fun uploadButton()
    {
        val context = LocalContext.current
        var uploadConfirmationTabVisible by remember {mutableStateOf(false)}

        FloatingActionButton(onClick = { uploadConfirmationTabVisible = true }, containerColor = Color(0xFFC21717), contentColor = Color(0xFFFDDCA9)) {
            Icon(Icons.Filled.Share, contentDescription = "Add")
        }

        if(uploadConfirmationTabVisible)
        {
            AlertDialog(
                onDismissRequest = { uploadConfirmationTabVisible = false },
                title = { Text("Confirmation") },
                text = { Text("Reupload chord chart with entered fields?") },
                confirmButton = {
                    Button(
                        onClick = {
                            chordChartController.saveChordChart(author.toString(), name.value, artist.value, key.value, chordsAndLyrics, FirebaseAuth.getInstance().currentUser?.uid.toString(),){onResult ->
                                when(onResult)// outer swtich statement
                                {
                                    0 ->{ //if the upload succeeds, proceed to delete the old chord chart
                                        chordChartController.deleteChordChart(chartID){onDeleteResult ->
                                            when(onDeleteResult)
                                            {
                                             0-> { //if the delete succeeds, navigate to the manage chord charts screen
                                                 Toast.makeText(context, "Chart uploaded successfully!", Toast.LENGTH_SHORT).show()
                                                 navController.navigate("manageChordChartScreen")
                                             }
                                             -1 -> { // if it fails, iunoo fix bug
                                                 Toast.makeText(context, "Failed to Delete old chart", Toast.LENGTH_SHORT).show()
                                             }
                                            }
                                        }
                                    }
                                    //various other error cases
                                    -1 -> Toast.makeText( context,  "Error in saving chord chart, check console",  Toast.LENGTH_SHORT).show()
                                    -2 ->Toast.makeText( context,  "Error: The song name cannot be empty!",  Toast.LENGTH_SHORT).show()
                                    -3 -> Toast.makeText( context,  "The song author cannot be empty!",  Toast.LENGTH_SHORT).show()
                                }

                            }
                            uploadConfirmationTabVisible = false  // Close the dialog
                        }) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    Button(onClick = { uploadConfirmationTabVisible = false }) {
                        Text("No")
                    }
                }
            )
        }


    }

    @Composable
    fun TabContent()
    {


        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.background(Color(0xFFFDDCA9))
        )
        {
            Text("Author: ${author}")
            OutlinedTextField(
                value = name.value,
                onValueChange = { name.value = it },
                label = { Text("Song Name") },
                modifier = Modifier.fillMaxWidth()
                    .background(Color.White),

                )
            OutlinedTextField(
                value = artist.value,
                onValueChange = { artist.value = it },
                label = { Text("Song Artist") },
                modifier = Modifier.fillMaxWidth().background(Color.White),
            )
            Spacer(modifier = Modifier.padding(2.dp))
            keyMenu()



        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun keyMenu() {
        var keyExpanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = keyExpanded,
            onExpandedChange = {
                keyExpanded = !keyExpanded
            }
        ) {
            TextField(
                value = key.value,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = keyExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = keyExpanded,
                onDismissRequest = { keyExpanded = false }
            ) {
                songKeys.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            key.value = item

                        }
                    )
                }
            }
        }
    }





}