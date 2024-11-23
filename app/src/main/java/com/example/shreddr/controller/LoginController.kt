package com.example.shreddr.controller

import android.util.Log
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

class LoginController(private val navController: NavController) {
     fun loginUser(email: String, password: String): Boolean {
        return try {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            Log.d("LoginController", "signInWithEmail:success") // searches for the credentials in the database, and if it finds them, it navigates to the next page
            navController.navigate("testScreen") // Navigate after successful login
            true
        } catch (exception: Exception) {
            Log.w("LoginController", "signInWithEmail:failure", exception)
            // Handle login error (e.g., show error message)
            false
        }
    }
}