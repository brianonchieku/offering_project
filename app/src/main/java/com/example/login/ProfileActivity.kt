package com.example.login

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var nameTextView: TextView
    private lateinit var changeTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var codeTextView: TextView
    private lateinit var passwordTextView: TextView
    private lateinit var saveButton: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var passlayout: LinearLayout
    private lateinit var oldPasswordEditText: EditText
    private lateinit var newPasswordEditText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        val currentUser: FirebaseUser? = firebaseAuth.currentUser

        nameTextView = findViewById(R.id.name)
        emailTextView = findViewById(R.id.email)
        codeTextView = findViewById(R.id.code)
        saveButton = findViewById(R.id.savebtn)
        changeTextView = findViewById(R.id.changepassword)
        passlayout = findViewById(R.id.passlayout)
        oldPasswordEditText = findViewById(R.id.oldpassword)
        newPasswordEditText = findViewById(R.id.newpassword)



        // Check if the user is logged in
        if (currentUser != null) {
            val userId = currentUser.uid
            // Retrieve user's name from Firestore based on the UID
            firestore.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        // Get the name from the document and set it to the TextView
                        val userName = document.getString("name")
                        val emailAddress= document.getString("email")
                        val churchCode= document.getString("church code")
                        nameTextView.text = userName
                        emailTextView.text = emailAddress
                        codeTextView.text = churchCode
                    } else {
                        // User document not found or name field doesn't exist
                        nameTextView.text = "Name not found"
                    }
                }
                .addOnFailureListener { exception ->
                    // Error retrieving user document
                    nameTextView.text = "Error fetching name"
                }
        } else {
            // User not logged in
            nameTextView.text = "User not logged in"
        }


        // Add click listener to the Save button
        saveButton.setOnClickListener {
            updateData()
            changePassword()
        }


        changeTextView.setOnClickListener {
            // Toggle the visibility of passLayout
            if (passlayout.visibility == View.VISIBLE) {
                passlayout.visibility = View.INVISIBLE
            } else {
                passlayout.visibility = View.VISIBLE
            }

        }

        nameTextView.setOnClickListener {
            val parentLayout = nameTextView.parent as ViewGroup
            val editText = EditText(this)
            editText.layoutParams = nameTextView.layoutParams
            editText.text = Editable.Factory.getInstance().newEditable(nameTextView.text) // Convert to Editable
            parentLayout.removeView(nameTextView)
            parentLayout.addView(editText)

            // Request focus and show keyboard
            editText.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)

            // Set a listener to handle saving the edited name
            editText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Save the edited name here
                    val updatedName = editText.text.toString()
                    // Update the UI or perform any necessary actions
                    // For example, update the TextView with the edited name
                    nameTextView.text = updatedName

                    // Remove the EditText and add back the TextView
                    parentLayout.removeView(editText)
                    parentLayout.addView(nameTextView)
                    true
                } else {
                    false
                }
            }
        }

        codeTextView.setOnClickListener {
            val parentLayout = codeTextView.parent as ViewGroup
            val editText = EditText(this)
            editText.layoutParams = codeTextView.layoutParams
            editText.text = Editable.Factory.getInstance().newEditable(codeTextView.text) // Convert to Editable
            parentLayout.removeView(codeTextView)
            parentLayout.addView(editText)

            // Request focus and show keyboard
            editText.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)

            // Set a listener to handle saving the edited name
            editText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Save the edited name here
                    val updatedName = editText.text.toString()
                    // Update the UI or perform any necessary actions
                    // For example, update the TextView with the edited name
                    codeTextView.text = updatedName

                    // Remove the EditText and add back the TextView
                    parentLayout.removeView(editText)
                    parentLayout.addView(codeTextView)
                    true
                } else {
                    false
                }
            }
        }


    }

    private fun changePassword() {
        val currentUser = firebaseAuth.currentUser
        val oldPassword = oldPasswordEditText.text.toString()
        val newPassword = newPasswordEditText.text.toString()

        if (currentUser != null) {
            // Reauthenticate the user first with their old password
            val credential = EmailAuthProvider.getCredential(currentUser.email!!, oldPassword)
            currentUser.reauthenticate(credential)
                .addOnSuccessListener {
                    // Reauthentication successful, update the password
                    currentUser.updatePassword(newPassword)
                        .addOnSuccessListener {
                            // Password updated successfully
                            Toast.makeText(
                                this,
                                "Password updated successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            // Clear the password fields
                            oldPasswordEditText.text.clear()
                            newPasswordEditText.text.clear()
                        }
                        .addOnFailureListener { exception ->
                            // Error updating password
                            Toast.makeText(
                                this,
                                "Failed to update password: ${exception.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
                .addOnFailureListener { exception ->
                    // Reauthentication failed
                    Toast.makeText(
                        this,
                        "Failed to authenticate: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateData(){

        val newName = nameTextView.text.toString()
        val newCode = codeTextView.text.toString()
        val userId = firebaseAuth.currentUser?.uid

        if (userId != null) {
            firestore.collection("users")
                .document(userId)
                .update(mapOf("name" to newName, "church code" to newCode))
                .addOnSuccessListener {
                    // Update successful
                    // You can add a toast or any other message here
                    Toast.makeText(this, "Details updated successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    // Error updating document
                    // You can add a toast or any other message here
                    Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show()
                }
        }

    }
}
