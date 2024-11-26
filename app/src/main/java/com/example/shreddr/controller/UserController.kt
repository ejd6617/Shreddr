package com.example.shreddr.controller

import android.content.ContentValues.TAG
import android.util.Log
import com.example.shreddr.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class UserController()
{

    // Function to delete user
    fun deleteUser(onResult: (Int) -> Unit) {
        val currentUser =  FirebaseAuth.getInstance().currentUser
        val database = FirebaseDatabase.getInstance()

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

    fun loginUser(email: String, password: String, onResult: (Int) -> Unit)
    {
        val auth = FirebaseAuth.getInstance()

        // Start the login process
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(0) // Return success code
                } else {
                    // If sign-in fails, return error code
                    onResult(-1) // Authentication failed
                }
            }
    }

    fun registerUser(email: String, password: String, onResult: (Int) -> Unit)
    {
        // the following function call returns a Task object. It then attaches a listener to that task to sense when it is done.
        if(email.isEmpty() || password.isEmpty())return

        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    val uid = user?.uid ?: return@addOnCompleteListener // Handle null UID. I think this creates the uid?

                    val database = FirebaseDatabase.getInstance()
                    val usersRef = database.getReference("users") //defines the path where this class will be stored

                    val newUser = User(uid, email, password)
                    usersRef.child(uid).setValue(newUser) //long ah function creates a new user class and stores it in the database
                        .addOnSuccessListener {
                            Log.d(TAG, "createUserWithUsername:success")
                            onResult(0) // Return success code
                        }
                        .addOnFailureListener { exception ->
                            Log.w(TAG, "createUserWithUsername:failure", exception)
                            onResult(-1) // Return error code
                        }
                } else {
                    Log.w(TAG, "createUserWithUsername:failure", task.exception)
                }
            }
    }


}