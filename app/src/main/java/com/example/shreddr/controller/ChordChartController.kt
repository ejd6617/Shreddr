package com.example.shreddr.controller

import com.example.shreddr.model.ChordChart
import com.google.firebase.database.FirebaseDatabase

class ChordChartController () {
    private val databaseInstance = FirebaseDatabase.getInstance()
    private val chordChartRef = databaseInstance.getReference("chord_charts")

    fun saveChordChart(chart: ChordChart, onResult: (Int) -> Unit) {
        // Early return if the chart's name or artist is empty
        if (chart.name.isEmpty()) {
            onResult(-2)  // Song name cannot be empty
            return
        } else if (chart.artist.isEmpty()) {
            onResult(-3)  // Artist name cannot be empty
            return
        }

        // Proceed with the Firebase operation only if the inputs are valid
        val songId = chordChartRef.push().key
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
}