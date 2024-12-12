package com.example.shreddr.model

import com.google.firebase.database.FirebaseDatabase

data class ChordChart(
    val author: String? = null,
    val name: String,
    val artist: String,
    var key: String,
    val chordsAndLyrics: List<ChordLyricPairs> = listOf(),
    val userId: String? = null,
    var chartID: String
)
{
    fun transpose(newKey: String = "") {
        for (chordLyricPair in chordsAndLyrics) {
            chordLyricPair.transpose(key, newKey)
            this.key = newKey;
        }
    }

    constructor() : this(null, "", "", "", listOf(), null, "")



}


