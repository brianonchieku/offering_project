package com.example.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class OfferingActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offering2)

        val toEditText: TextView=findViewById(R.id.toedittext)
        val forEditText: TextView = findViewById(R.id.foredittext)
        val titheEditText = findViewById<EditText>(R.id.tithe)
        val offeringEditText = findViewById<EditText>(R.id.combinedoffering)
        val campEditText = findViewById<EditText>(R.id.campmeeting)
        val thirteenthEditText = findViewById<EditText>(R.id.thirteenthsabbath)
        val conferenceEditText = findViewById<EditText>(R.id.confrencedevelopment)
        val totalEditText = findViewById<TextView>(R.id.total)
        val confirmbtn= findViewById<Button>(R.id.confirmbtn)

        // Retrieve the church name from the temporaryChurchId passed from the previous activity
        val temporaryChurchId = intent.getStringExtra("TEMP_CHURCH_ID")
        val churchInfo = temporaryChurchId?.split("_")
        val churchName = churchInfo?.get(0)

        // Set the text of the To EditText
        churchName?.let { toEditText.text = it }


        // Retrieve the current user's display name from Firebase Authentication
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userName = currentUser?.displayName

        // Set the retrieved user name in the "For" EditText
        userName?.let { forEditText.text = it }

        forEditText.text= "$userName"
        toEditText.text="$churchName"

// Set the default value to zero
        titheEditText.setText("0")
        offeringEditText.setText("0")
        campEditText.setText("0")
        thirteenthEditText.setText("0")
        conferenceEditText.setText("0")
        totalEditText.text = "0"

        // Retrieve values from EditText fields
        val tithe = titheEditText.text.toString().toIntOrNull() ?: 0
        val combinedOffering = offeringEditText.text.toString().toIntOrNull() ?: 0
        val campMeeting = campEditText.text.toString().toIntOrNull() ?: 0
        val thirteenthSabbath = thirteenthEditText.text.toString().toIntOrNull() ?: 0
        val conferenceFund = conferenceEditText.text.toString().toIntOrNull() ?: 0

// Calculate total
        val total = tithe + combinedOffering + campMeeting + thirteenthSabbath + conferenceFund

// Set total to TextView
        totalEditText.text = "Total: $total"

        confirmbtn.setOnClickListener {
            val intent= Intent(this,ConfirmActivity::class.java)
            intent.putExtra("TEMP_CHURCH_NAME", churchName)
            if (tithe > 0) {
                intent.putExtra("Tithe", tithe)
                intent.putExtra("titheAmount", tithe)
            }
            if (combinedOffering > 0) {
                intent.putExtra("combinedOffering", combinedOffering)
                intent.putExtra("combinedOfferingAmount", combinedOffering)
            }
            if (campMeeting > 0) {
                intent.putExtra("campMeeting", campMeeting)
                intent.putExtra("campMeetingAmount", campMeeting)
            }
            if (thirteenthSabbath > 0) {
                intent.putExtra("thirteenthSabbath", thirteenthSabbath)
                intent.putExtra("thirteenthSabbathAmount", thirteenthSabbath)
            }
            if (conferenceFund > 0) {
                intent.putExtra("thirteenthSabbath", thirteenthSabbath)
                intent.putExtra("conferenceDevelopmentAmount", conferenceFund)
            }
            startActivity(intent)
        }

    }
}