package com.example.shreddr.model

// Import packages
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ChordLyricPairs(chords: String = "",lyrics: String = "")
{

    // Initialize a list of possible chords according to music theory
    private val possibleChords = listOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")

    // Initialize chords and lyrics
    var chords by mutableStateOf(chords)
    var lyrics by mutableStateOf(lyrics)

    // Set up function to edit chords
    fun changeChords(chords: String)
    {
        this.chords = chords

    }

    // Set up a function to edit lyrics
    fun changeLyrics(lyrics: String)
    {
        this.lyrics = lyrics

    }

    // Set up a function to transpose the chord from an old key to a new key
    fun transpose(originalKey: String = "", newKey: String = ""){
        var step = possibleChords.indexOf(newKey) - possibleChords.indexOf(originalKey);
        if (step < 0) {
            step += 12
        }
        this.chords = possibleChords[(possibleChords.indexOf(this.chords) + step)%12]
    }


}