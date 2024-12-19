package com.example.shreddr.model

data class ChordChart(
    // Contains attributes to store all chord chart attributes
    val author: String? = null,
    val name: String,
    val artist: String,
    var key: String,
    val chordsAndLyrics: List<ChordLyricPairs> = listOf(),
    val userId: String? = null,
    var chartID: String,
    var genre: String
)
{
    // Function to transpose a chord chart given a desired key
    fun transpose(newKey: String = "") {

        // For each chord-lyric pair, call the transpose function given old key and new key
        for (chordLyricPair in chordsAndLyrics) {
            chordLyricPair.transpose(key, newKey)
        }
        this.key = newKey;
    }

    constructor() : this(null, "", "", "", listOf(), null, "", "")
}


