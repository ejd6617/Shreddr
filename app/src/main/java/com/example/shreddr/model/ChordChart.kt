package com.example.shreddr.model

import com.google.firebase.database.FirebaseDatabase

data class ChordChart(
    val author: String? = null,
    val name: String,
    val artist: String,
    val key: String,
    val chordsAndLyrics: List<Pair<String, String>> = listOf()
)


