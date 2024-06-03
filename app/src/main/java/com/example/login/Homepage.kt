package com.example.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth

class Homepage : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        val offeringCard: CardView = findViewById(R.id.offeringcard)
        offeringCard.setOnClickListener {
            val intent = Intent(this, OfferingActivity::class.java)
            startActivity(intent)
        }

        val settingsCard: CardView = findViewById(R.id.settings)
        settingsCard.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        val logout: CardView = findViewById(R.id.logout)
        logout.setOnClickListener {
            firebaseAuth.signOut()
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        // Close the whole app when the back button is pressed
        finishAffinity()
    }
}