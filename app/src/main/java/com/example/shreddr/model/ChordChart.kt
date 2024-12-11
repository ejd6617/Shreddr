package com.example.shreddr.model

import com.google.firebase.database.FirebaseDatabase

data class ChordChart(
    val author: String? = null,
    val name: String,
    val artist: String,
    val key: String,
    val chordsAndLyrics: List<ChordLyricPairs> = listOf(),
    val userId: String? = null,
    var chartID: String
)
{
    constructor() : this(null, "", "", "", listOf(), null, "")




}


