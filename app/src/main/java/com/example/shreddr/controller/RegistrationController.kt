package com.example.shreddr.controller

import android.content.ContentValues.TAG
import android.util.Log
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.example.shreddr.model.User

class RegistrationController(
    private val navController: NavController,

)


{
    fun registerUser(email: String, password: String)
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
                            navController.navigate("searchScreen")
                        }
                        .addOnFailureListener { exception ->
                            Log.w(TAG, "createUserWithUsername:failure", exception)
                            //probably put some kind of popup error message here
                        }
                } else {
                    Log.w(TAG, "createUserWithUsername:failure", task.exception)
                }
            }
    }
}