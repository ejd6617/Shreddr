package com.example.shreddr.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ChordLyricPairs(first: String = "",second: String = "")
{

    var first by mutableStateOf(first)
    var second by mutableStateOf(second)

    fun changeChords(chords: String)
    {
        first = chords

    }

    fun changeLyrics(lyrics: String)
    {
        second = lyrics

    }



}