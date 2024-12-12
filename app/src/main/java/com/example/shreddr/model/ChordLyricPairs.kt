package com.example.shreddr.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ChordLyricPairs(chords: String = "",lyrics: String = "")
{

    private val possibleChords = listOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")

    var chords by mutableStateOf(chords)
    var lyrics by mutableStateOf(lyrics)

    fun changeChords(chords: String)
    {
        this.chords = chords

    }

    fun changeLyrics(lyrics: String)
    {
        this.lyrics = lyrics

    }

    fun transpose(originalKey: String = "", newKey: String = ""){
        var step = possibleChords.indexOf(newKey) - possibleChords.indexOf(originalKey);
        if (step < 0) {
            step += 12
        }
        this.chords = possibleChords[(possibleChords.indexOf(this.chords) + step)%12]
    }


}