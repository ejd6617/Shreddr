package com.example.shreddr.view

// Import packages
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
import com.example.shreddr.model.ChordLyricPairs
import com.google.firebase.auth.FirebaseAuth

class AddChordChartScreen(private val navController: NavController, private val chordChartController: ChordChartController) {

    // Initialize variables to store user inputs
    private var currentUser = FirebaseAuth.getInstance().currentUser
    private var songName = mutableStateOf("")
    private var artistName = mutableStateOf("")
    private val songKeys = listOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")
    private var chordsAndLyrics = mutableStateListOf(ChordLyricPairs())
    private var selectedKey = mutableStateOf("C")
    private var genre = mutableStateOf("")

    // Set up function to set up the add chord chart screen
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun addChordChartScreen()
    {

        val context = LocalContext.current
        var isTabVisible by remember { mutableStateOf(false) }
        val scrollState = rememberScrollState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {Text("Add Chord Chart", color = Color(0xFFFDDCA9))},
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF562717)
                    ),
                    actions =
                    {
                        IconButton(onClick = { isTabVisible = !isTabVisible })
                        {Icon(Icons.Filled.Menu, contentDescription = "Chord Chart Data", tint = Color(0xFFFDDCA9))}
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
    fun TabContent()
    {
        currentUser = FirebaseAuth.getInstance().currentUser

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.background(Color(0xFFFDDCA9))
        )
        {
            Text("Author: ${currentUser?.email}")
            OutlinedTextField(
               value = songName.value
                , onValueChange = { songName.value = it },
                label = { Text("Song Name") },
                modifier = Modifier.fillMaxWidth()
                    .background(Color.White),

            )
            OutlinedTextField(
                value = artistName.value,
                onValueChange = { artistName.value = it },
                label = { Text("Song Artist") },
                modifier = Modifier.fillMaxWidth().background(Color.White),
            )
            OutlinedTextField(
                value = genre.value,
                onValueChange = { genre.value = it },
                label = { Text("Genre") },
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
                value = selectedKey.value,
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
                            selectedKey.value = item

                        }
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ChordsAndLyrics()
    {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp))
        {
            chordsAndLyrics.forEachIndexed { index, pair ->
                // Chord Dropdown
                var chordExpanded by remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = chordExpanded,
                    onExpandedChange = {
                        chordExpanded = !chordExpanded
                    }
                ) {
                    TextField(
                        value = chordsAndLyrics[index].chords,
                        onValueChange = {},  // No change needed here, we use dropdown to change the value
                        readOnly = true,
                        label = { Text("Chord ${index + 1}") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = chordExpanded) },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = chordExpanded,
                        onDismissRequest = { chordExpanded = false }
                    ) {
                        songKeys.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
                                onClick = {
                                    chordsAndLyrics[index].changeChords(item)  // Update chord
                                    chordExpanded = false  // Close dropdown
                                }
                            )
                        }
                    }
                }

                // Lyrics TextField
                TextField(
                    value = chordsAndLyrics[index].lyrics,
                    onValueChange = { newLyrics ->
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

    // Function to add a new pair of chords and lyrics
    fun addPair()
    {
        chordsAndLyrics.add(ChordLyricPairs())
    }

    // Function to remove the last pair of chords and lyrics
    fun removePair()
    {
        chordsAndLyrics.removeLast()
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
                            navController.navigate("searchScreen")  // Navigate to the search screen
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
    fun uploadButton() {
        val context = LocalContext.current
        var uploadConfirmationTabVisible by remember { mutableStateOf(false) }

        FloatingActionButton(
            onClick = { uploadConfirmationTabVisible = true },
            containerColor = Color(0xFFC21717),
            contentColor = Color(0xFFFDDCA9)
        ) {
            Icon(Icons.Filled.Share, contentDescription = "Add")
        }

        if (uploadConfirmationTabVisible) {
            AlertDialog(
                onDismissRequest = { uploadConfirmationTabVisible = false },
                title = { Text("Confirmation") },
                text = { Text("Upload chord chart with entered fields?") },
                confirmButton = {
                    Button(
                        onClick = {
                            chordChartController.saveChordChart(
                                currentUser?.email.toString(),
                                songName.value,
                                artistName.value,
                                selectedKey.value,
                                chordsAndLyrics,
                                currentUser?.uid.toString(),
                                genre = genre.value
                            ) { errorCode ->
                                when (errorCode) {
                                    0 -> {
                                        Toast.makeText(
                                            context,
                                            "Chord Chart Successfully Uploaded!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        uploadConfirmationTabVisible = false
                                        navController.navigate("searchScreen")
                                    }


                                    -1 -> Toast.makeText(
                                        context,
                                        "Error in saving chord chart, check console",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    -2 -> Toast.makeText(
                                        context,
                                        "Error: The song name cannot be empty!",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    -3 -> Toast.makeText(
                                        context,
                                        "The song author cannot be empty!",
                                        Toast.LENGTH_SHORT
                                    ).show()
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
}