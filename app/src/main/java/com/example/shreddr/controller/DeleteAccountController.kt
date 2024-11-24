package com.example.shreddr.controller
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseUser
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext


class DeleteAccountController {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    // Function to delete user
    fun deleteUser(onResult: (Int) -> Unit) {
        val currentUser: FirebaseUser? = auth.currentUser

        if (currentUser != null)
        {
            val userId = currentUser.uid
            // Reference to the location of the user in Firebase Realtime Database
            val userRef = database.getReference("users").child(userId)

            // Delete the user data from the database
            userRef.removeValue().addOnCompleteListener { databaseTask ->
                if (databaseTask.isSuccessful)
                {
                    // After successfully removing data from the database, delete the user from Firebase Auth
                    currentUser.delete().addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful)
                        {
                            // User has been deleted successfully
                            onResult(0) // Success
                        }
                        else {
                            // Error deleting user from Firebase Authentication
                            onResult(-3) // Error number 3: error in deleting from auth
                        }
                    }
                }
                else {
                    // Error deleting data from the Realtime Database
                    onResult(-2) // Error number 2: error in deleting from database
                }
            }
        }
        else {

            // No user is logged in
            onResult(-1) // Error number 1: no user logged in
        }
    }

}