package com.example.shreddr.controller

// Import packages
import com.example.shreddr.model.ChordChart
import com.example.shreddr.model.ChordLyricPairs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class ChordChartController() {

    // Create database reference
    private val databaseInstance = FirebaseDatabase.getInstance()
    private val chordChartRef = databaseInstance.getReference("chord_charts")

    // Function to save a chord chart
    fun saveChordChart(currentUserName: String, songName: String, artistName: String, selectedKey: String, chordsAndLyrics: List<ChordLyricPairs>, userID: String, genre: String, onResult: (Int) -> Unit) {
        // Early return if the chart's name or artist is empty
        if (songName == "") {
            onResult(-2)  // Song name cannot be empty
            return
        } else if (artistName == "") {
            onResult(-3)  // Artist name cannot be empty
            return
        }

        // Creates a new Chord Chart object
        val chart = ChordChart(currentUserName, songName, artistName, selectedKey, chordsAndLyrics, userID, "", genre)
        // Proceed with the Firebase operation only if the inputs are valid
        val songId = chordChartRef.push().key
        chart.chartID = songId ?: ""
        songId?.let {
            chordChartRef.child(it).setValue(chart)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        println("Chord chart saved successfully")
                        onResult(0)  // Successful save
                    } else {
                        println("Failed to save chord chart: ${task.exception}")
                        onResult(-1)  // Failed save
                    }
                }
        } ?: run {
            // Handle the case where `songId` is null (e.g., push failed)
            println("Failed to generate song ID")
            onResult(-1)
        }
    }

    // Return a list of chord charts associated with a user
    fun getUserChordCharts(onResult: (List<ChordChart>) -> Unit) {
        val userChartRef = FirebaseDatabase.getInstance().reference.child("chord_charts") //get a reference to the chord charts section of the database
        val currentUser = FirebaseAuth.getInstance().currentUser//get the current user

        // Gather a list of all chord charts that match the currently logged in user's ID
        userChartRef.orderByChild("userId").equalTo(currentUser?.uid).addValueEventListener(object :
            ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userChordCharts = mutableListOf<ChordChart>()
                for (dataSnapshot in snapshot.children)
                {
                    val chart = dataSnapshot.getValue(ChordChart::class.java)
                    if (chart != null)
                    {
                        userChordCharts.add(chart)
                    }
                }
                onResult(userChordCharts)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
            }
        }
        )
    }

    // Delete a selected chord chart by ID
    fun deleteChordChart(chartId: String, onDeleteResult: (Int) -> Unit) {
        val chartRef = FirebaseDatabase.getInstance().reference.child("chord_charts")
        val chartToDeleteRef = chartRef.child(chartId)

        chartToDeleteRef.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onDeleteResult(0)
                //chart removal successful
                return@addOnCompleteListener
            } else {
                onDeleteResult(-1)
                return@addOnCompleteListener
            }
        }

    }

    // Query the database of chord charts by a set of parameters
    fun getChordChartsByCriteria(name: String, artist: String, genre: String, onResult: (List<ChordChart>) -> Unit) {
        var query: Query = chordChartRef

        // Execute the query
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val filteredChordCharts = mutableListOf<ChordChart>()

                for (dataSnapshot in snapshot.children) {
                    val chart = dataSnapshot.getValue(ChordChart::class.java)
                    if (chart != null) {
                        // Check if the chart matches all criteria
                        val matchesName = name.isEmpty() || chart.name.contains(name, ignoreCase = true)
                        val matchesArtist = artist.isEmpty() || chart.artist.contains(artist, ignoreCase = true)
                        val matchesGenre = genre.isEmpty() || chart.genre.contains(genre, ignoreCase = true)

                        // If the chart matches all criteria, add to the list of matching chord charts
                        if (matchesName && matchesArtist && matchesGenre) {
                            filteredChordCharts.add(chart)
                        }
                    }
                }

                // Return the result of the query
                onResult(filteredChordCharts)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
            }
        })
    }

    // Tool to manage which chord chart is selected
    companion object {
        private var selectedChordChart = ChordChart()
        fun setSelectedChordChart(chart: ChordChart) {
            selectedChordChart = chart
        }
        fun getSelectedChordChart(): ChordChart {
            return selectedChordChart
        }
    }
}
