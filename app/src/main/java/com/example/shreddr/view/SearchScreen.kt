import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shreddr.controller.DeleteAccountController
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun searchScreenfunction(navController : NavController)
    {
        // Remember the state to control visibility of the tab
        var isTabVisible by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Search") },
                    actions = {
                        IconButton(onClick = { isTabVisible = !isTabVisible }) {
                            Icon(Icons.Filled.MoreVert, contentDescription = "Toggle Tab")
                        }
                    }
                )
            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    // Show the tab only when `isTabVisible` is true
                    if (isTabVisible) {
                        TabContent(navController)
                    }
                    Spacer(modifier = Modifier.weight(1f)) // Filler space
                }
            }
        )
    }

@Composable
fun TabContent(navController: NavController) {
    val userData = FirebaseAuth.getInstance().currentUser

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text ="Account Management", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        Text("${userData?.email}")
        Spacer(modifier = Modifier.height(16.dp))
        LogoutButton(navController)
        Spacer(modifier = Modifier.padding(2.dp))
        DeleteUserButton(navController)
    }
}


@Composable
fun LogoutButton(navController: NavController)
{
    Button(
        onClick = {
            navController.navigate("signInWindow")// navigates back to the login screen
        },
        modifier = Modifier.padding(16.dp).fillMaxWidth().shadow(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
        shape = RoundedCornerShape(8.dp)
    ) {Text("Log Out", color = Color.White)}

}



@Composable
fun DeleteUserButton(navController: NavController)
{
    val context = LocalContext.current
    val deleteController = remember { DeleteAccountController() }
    var showDialog by remember { mutableStateOf(false) }
    var errorCode  = 0
    Button(
        onClick = {
            showDialog = true

        },
        modifier = Modifier.padding(16.dp).fillMaxWidth().shadow(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
        shape = RoundedCornerShape(8.dp)
    ) {Text("Delete Account", color = Color.White)}

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmation") },
            text = { Text("Are you sure you would like to delete your account?") },
            confirmButton = {
                Button(
                    onClick = {
                        deleteController.deleteUser() {
                            errorCode  ->

                            when (errorCode) {
                                0 -> navController.navigate("signInWindow")
                                -1 -> Toast.makeText(
                                    context,
                                    "No user logged in",
                                    Toast.LENGTH_SHORT
                                ).show()

                                -2 -> Toast.makeText(
                                    context,
                                    "Error in deleting from database",
                                    Toast.LENGTH_SHORT
                                ).show()

                                -3 -> Toast.makeText(
                                    context,
                                    "Error in deleting from auth",
                                    Toast.LENGTH_SHORT
                                ).show()

                                1 -> Toast.makeText(
                                    context,
                                    "Something else happened",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        showDialog = false
                    }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("No")
                }
            }
        )
    }

}



