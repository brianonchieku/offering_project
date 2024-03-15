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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth= FirebaseAuth.getInstance()
        db= FirebaseFirestore.getInstance()


        binding.signupButton.setOnClickListener {
            val fullname=binding.fullname.text.toString()
            val email=binding.email.text.toString()
            val churchcode=binding.churchcode.text.toString()
            val pass=binding.password.text.toString()
            val confirmpass=binding.confpassword.text.toString()


            // Validate input fields
            if (fullname.isEmpty() || email.isEmpty() || pass.isEmpty() || confirmpass.isEmpty()|| churchcode.isEmpty()) {
                // Display error message if any of the fields are empty
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            } else if (pass != confirmpass) {
                // Display error message if passwords do not match
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                // Check if the church code exists in Firestore
                val churchCodesCollection = FirebaseFirestore.getInstance().collection("church_codes")
                churchCodesCollection.document(churchcode).get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document != null && document.exists()) {
                            // Church code exists, proceed with user registration
                            firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { registrationTask ->
                                if (registrationTask.isSuccessful) {
                                    // User registration successful
                                    //ADD INTENT TO MOVE TO THE MAIN SCREEN WHEN CREATED LATER
                                    val intent = Intent(this, Homepage::class.java)
                                    startActivity(intent)
                                    Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show()
                                    val users= hashMapOf(
                                        "Full Name" to fullname,
                                        "Email" to email,
                                        "Church Code" to churchcode
                                    )

                                    db.collection("users")
                                        .add(users)

                                } else {
                                    // User registration failed, display error message
                                    Toast.makeText(this, "Registration failed: ${registrationTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            // Church code does not exist, display error message
                            Toast.makeText(this, "Invalid church code", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Error occurred while querying Firestore, display error message
                        Toast.makeText(this, "Error validating church code", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.alreadybtn.setOnClickListener {
            val intent= Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}