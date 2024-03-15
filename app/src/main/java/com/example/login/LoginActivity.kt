package com.example.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.login.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

       binding.register.setOnClickListener {
           val intent=Intent(this,SignupActivity::class.java)
           startActivity(intent)
       }

        binding.loginButton.setOnClickListener {
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // ADD INTENT TO MOVE TO THE MAIN SCREEN WHEN CREATED
                        val intent = Intent(this, Homepage::class.java)
                        startActivity(intent)
                        Toast.makeText(this, "Login successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Fill all the fields!!", Toast.LENGTH_SHORT).show()
            }
        }

    }
}