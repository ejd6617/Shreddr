package com.example.shreddr.controller

import androidx.compose.runtime.mutableStateListOf
import com.example.shreddr.model.ChordChart
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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
        chart.chartID = songId ?: ""//sets the ID of the chart as a attribute of the chart
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

    fun getUserChordCharts(onResult: (List<ChordChart>) -> Unit)
    {
        val userChartRef = FirebaseDatabase.getInstance().reference.child("chord_charts") //get a reference to the chord charts section of the database
        val currentUser = FirebaseAuth.getInstance().currentUser//get the current user

        //gather a list of all chord charts that match the currently logged in user's ID
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

    fun deleteChordChart(chartId: String, onDeleteResult: (Int) -> Unit)
    {
        val chartRef = FirebaseDatabase.getInstance().reference.child("chord_charts")
        val chartToDeleteRef = chartRef.child(chartId)

        chartToDeleteRef.removeValue().addOnCompleteListener {task ->

            if (task.isSuccessful)
            {
                onDeleteResult(0)
                //chart removal successful
                return@addOnCompleteListener
            }
            else
            {
                onDeleteResult(-1)
                return@addOnCompleteListener

            }
        }

    }
}