package com.example.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import android.widget.Toast
import com.example.login.databinding.ActivityLoginBinding
import com.example.login.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var displayName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Retrieve the current user's display name
        val currentUser = auth.currentUser
        displayName = currentUser?.displayName



        binding.signupButton.setOnClickListener {
            val fullname = binding.fullname.text.toString()
            val email = binding.email.text.toString()
            val churchcode = binding.churchcode.text.toString()
            val pass = binding.password.text.toString()
            val confirmpass = binding.confpassword.text.toString()


            // Validate input fields
            if (fullname.isEmpty() || email.isEmpty() || pass.isEmpty() || confirmpass.isEmpty() || churchcode.isEmpty()) {
                // Display error message if any of the fields are empty
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            } else if (pass != confirmpass) {
                // Display error message if passwords do not match
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                // Check if the email is already registered
                firebaseAuth.fetchSignInMethodsForEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val signInMethods = task.result?.signInMethods
                            if (signInMethods != null && signInMethods.isNotEmpty()) {
                                // Email is already registered
                                Toast.makeText(
                                    this,
                                    "User already exists with this email",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                // Check if the church code exists in Firestore
                                val churchCodesCollection =
                                    FirebaseFirestore.getInstance().collection("church codes")
                                // Query Firestore to find the document with the entered church code
                                churchCodesCollection
                                    .whereEqualTo("church_code", churchcode)
                                    .get()
                                    .addOnSuccessListener { querySnapshot ->
                                        if (!querySnapshot.isEmpty) {
                                            // Church code exists, retrieve the church name from the document
                                            val churchName = querySnapshot.documents[0].getString("church name")
                                            if (churchName != null) {
                                                // Church name retrieved, perform further validation or actions
                                                println("Church code $churchcode is valid for $churchName")

                                                firebaseAuth.createUserWithEmailAndPassword(email, pass)
                                                    .addOnCompleteListener { task ->
                                                        if (task.isSuccessful) {
                                                            val user = firebaseAuth.currentUser
                                                            val userId = user?.uid
                                                            // Create a new document in Firestore with user data
                                                            val userData = hashMapOf(
                                                                "name" to fullname,
                                                                "email" to email,
                                                                "church code" to churchcode
                                                            )
                                                            userId?.let {
                                                                db.collection("users").document(it)
                                                                    .set(userData)
                                                                    .addOnSuccessListener {
                                                                        Log.d(TAG, "DocumentSnapshot successfully written!")
                                                                    }
                                                                    .addOnFailureListener { e ->
                                                                        Log.w(TAG, "Error writing document", e)
                                                                    }
                                                            }

                                                            // Update display name in Firebase Auth
                                                            val profileUpdates = UserProfileChangeRequest.Builder()
                                                                .setDisplayName(fullname)
                                                                .build()
                                                            user?.updateProfile(profileUpdates)
                                                                ?.addOnCompleteListener { profileTask ->
                                                                    if (profileTask.isSuccessful) {
                                                                        Log.d(TAG, "User profile updated.")
                                                                    } else {
                                                                        Log.w(TAG, "Error updating user profile.", profileTask.exception)
                                                                    }
                                                                }
                                                            binding.fullname.text = null
                                                            binding.email.text = null
                                                            binding.churchcode.text = null
                                                            binding.password.text = null
                                                            binding.confpassword.text = null


                                                            // Navigate to the homepage after successful registration
                                                            val intent = Intent(this, Homepage::class.java)
                                                            startActivity(intent)
                                                            Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show()
                                                        } else {
                                                            // User registration failed, display error message
                                                            Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                                        }
                                                    }


                                            } else {
                                                // Error: Church name not found in the document
                                                println("Error: Church name not found")
                                            }
                                        } else {
                                            // Church code not found in the collection
                                            println("Error: Church code $churchcode not found")
                                        }
                                    }
                                    .addOnFailureListener { exception ->
                                        // Error retrieving data from Firestore
                                        println("Error: ${exception.message}")
                                    }
                            }
                        }
                    }


                }

            }

        binding.alreadybtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)


        }


    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}

