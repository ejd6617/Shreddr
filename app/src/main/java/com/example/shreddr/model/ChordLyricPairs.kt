package com.example.shreddr.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ChordLyricPairs(chords: String = "",lyrics: String = "")
{

    private var chords by mutableStateOf(chords)
    private var lyrics by mutableStateOf(lyrics)

    fun changeChords(chords: String)
    {
        this.chords = chords

    }

    fun changeLyrics(lyrics: String)
    {
        this.lyrics = lyrics

    }

    fun getChords(): String
    {
        return chords

    }

    fun getLyrics(): String
    {
        return lyrics
    }

}