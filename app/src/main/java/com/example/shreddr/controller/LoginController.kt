package com.example.shreddr.controller

import android.util.Log
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

class LoginController(private val navController: NavController) {
    fun loginUser(email: String, password: String, onResult: (Int) -> Unit) {
        val auth = FirebaseAuth.getInstance()

        // Start the login process
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(0) // Return success code
                    navController.navigate("searchScreen")
                } else {
                    // If sign-in fails, return error code
                    onResult(-1) // Authentication failed
                }
            }
    }
}