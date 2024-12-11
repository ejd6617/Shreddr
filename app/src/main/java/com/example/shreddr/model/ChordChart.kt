package com.example.shreddr.model

import com.google.firebase.database.FirebaseDatabase

data class ChordChart(
    private val author: String? = null,
    private val name: String,
    private val artist: String,
    private val key: String,
    private val chordsAndLyrics: List<ChordLyricPairs> = listOf(),
    private val userId: String? = null,
    private var chartID: String
)
{
    constructor() : this(null, "", "", "", listOf(), null, "")


   fun setID(id: String)
   {
       chartID = id
   }

    fun getID(): String
    {
        return chartID
    }

    fun getName() : String
    {
        return name
    }

    fun getArtist() : String
    {
        return artist
    }

    fun getKey() : String
    {
        return key
    }

    fun getChordsAndLyrics() : List<ChordLyricPairs>
    {
        return chordsAndLyrics
    }

    fun getAuthor() : String?
    {
        return author
    }


}


