package com.example.shreddr

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shreddr.ui.theme.ShreddrTheme
import com.example.shreddr.SecondScreen
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

data class User( // user class to store the user data
    val uid: String? = null,
    val email: String? = null,
    val password: String? = null
)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)





        setContent {
            ShreddrTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "signInWindow") {
                    composable("signInWindow") { loginScreen(navController) }
                    composable("second_screen") { secondScreen(navController) }
                    composable("main_screen") { MainScreen(navController) }
                }
            }
        }
    }
}

    @Composable
    fun registerButton(navController: NavController, email: String, password: String) {



        Button(
            onClick = {
                // the following function call returns a Task object. It then attaches a listener to that task to sense when it is done.
                if(email.isEmpty() || password.isEmpty())return@Button

                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = FirebaseAuth.getInstance().currentUser
                            val uid = user?.uid ?: return@addOnCompleteListener // Handle null UID. I think this creates the uid?

                            val database = FirebaseDatabase.getInstance()
                            val usersRef = database.getReference("users")

                            val newUser = User(uid, email, password) // Assuming you have a User data class
                            usersRef.child(uid).setValue(newUser)
                                .addOnSuccessListener {
                                    Log.d(TAG, "createUserWithUsername:success")
                                    navController.navigate("second_screen")
                                }
                                .addOnFailureListener { exception ->
                                    Log.w(TAG, "createUserWithUsername:failure", exception)
                                }
                        } else {
                            Log.w(TAG, "createUserWithUsername:failure", task.exception)
                        }
                    }
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Register", color = Color.White)
        }
    }
@Composable
fun goBackButton(navController: NavController)
{
    Button(
        onClick = {
            navController.navigateUp()// goes back to the last visited place in the stack

        },
        modifier = Modifier
            .padding(16.dp)
            .width(300.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
        shape = RoundedCornerShape(8.dp)
    ) { Text("Go Back", color = Color.White)}
}






    @Composable
    fun MainScreen(navController: NavController) {
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Column {
            Spacer(modifier = Modifier.height(300.dp))
            OutlinedTextField(

                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.width(300.dp)
            )
            Spacer(modifier = Modifier.height(16.dp)) // Add some space between fields
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(), // Hide password
                modifier = Modifier.width(300.dp)
            )
            Spacer(modifier = Modifier.height(16.dp)) // Add some space between fields
            registerButton(navController, username, password)
            Spacer(modifier = Modifier.height(8.dp)) // Add some space between fields
            goBackButton(navController)

        }
    }