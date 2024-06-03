package com.example.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class OfferingActivity2 : AppCompatActivity() {

    private lateinit var titheEditText: EditText
    private lateinit var combinedOfferingEditText: EditText
    private lateinit var campMeetingEditText: EditText
    private lateinit var thirteenthSabbathEditText: EditText
    private lateinit var conferenceDevelopmentEditText: EditText
    private lateinit var mashinaniEditText: EditText
    private lateinit var totalTextView: TextView
    private lateinit var confirmButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offering2)

        val toEditText: TextView=findViewById(R.id.toedittext)
        val forEditText: TextView = findViewById(R.id.foredittext)
        titheEditText = findViewById(R.id.tithe)
        combinedOfferingEditText = findViewById(R.id.combinedoffering)
        campMeetingEditText = findViewById(R.id.campmeeting)
        thirteenthSabbathEditText = findViewById(R.id.thirteenthsabbath)
        conferenceDevelopmentEditText = findViewById(R.id.confrencedevelopment)
        totalTextView = findViewById(R.id.total)
        confirmButton = findViewById(R.id.confirmbtn)


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

        updateTotal()

        // Add text change listeners to EditText fields
        titheEditText.addTextChangedListener(contributionTextWatcher)
        combinedOfferingEditText.addTextChangedListener(contributionTextWatcher)
        campMeetingEditText.addTextChangedListener(contributionTextWatcher)
        thirteenthSabbathEditText.addTextChangedListener(contributionTextWatcher)
        conferenceDevelopmentEditText.addTextChangedListener(contributionTextWatcher)

        confirmButton.setOnClickListener {
            val intent = Intent(this, ConfirmActivity::class.java)
            val churchName = toEditText.text.toString()

            // Check if the tithe amount is greater than zero and pass it to the next activity
            val titheAmount = titheEditText.text.toString().toIntOrNull()
            if (titheAmount != null && titheAmount > 0) {
                intent.putExtra("Tithe", titheAmount)
            }

            // Check if the combined offering amount is greater than zero and pass it
            val combinedOfferingAmount = combinedOfferingEditText.text.toString().toIntOrNull()
            if (combinedOfferingAmount != null && combinedOfferingAmount > 0) {
                intent.putExtra("combinedOffering", combinedOfferingAmount)
            }

            // Check if the camp meeting amount is greater than zero and pass it
            val campMeetingAmount = campMeetingEditText.text.toString().toIntOrNull()
            if (campMeetingAmount != null && campMeetingAmount > 0) {
                intent.putExtra("campMeeting", campMeetingAmount)
            }

            // Check if the thirteenth sabbath amount is greater than zero and pass it
            val thirteenthSabbathAmount = thirteenthSabbathEditText.text.toString().toIntOrNull()
            if (thirteenthSabbathAmount != null && thirteenthSabbathAmount > 0) {
                intent.putExtra("thirteenthSabbath", thirteenthSabbathAmount)
            }

            // Check if the conference development amount is greater than zero and pass it
            val conferenceDevelopmentAmount = conferenceDevelopmentEditText.text.toString().toIntOrNull()
            if (conferenceDevelopmentAmount != null && conferenceDevelopmentAmount > 0) {
                intent.putExtra("conferenceDevelopment", conferenceDevelopmentAmount)
            }

            val totalAmount = totalTextView.text.toString().toIntOrNull()
            if (totalAmount != null && totalAmount > 0) {
                intent.putExtra("total", totalAmount)
            }

            // Start the next activity with the collected data
            intent.putExtra("TEMP_CHURCH_NAME", churchName)
            startActivity(intent)
        }
    }
    private val contributionTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            // Update total whenever text changes in any of the EditText fields
            updateTotal()
        }
    }
    private fun updateTotal() {
        val tithe = titheEditText.text.toString().toIntOrNull() ?: 0
        val combinedOffering = combinedOfferingEditText.text.toString().toIntOrNull() ?: 0
        val campMeeting = campMeetingEditText.text.toString().toIntOrNull() ?: 0
        val thirteenthSabbath = thirteenthSabbathEditText.text.toString().toIntOrNull() ?: 0
        val conferenceDevelopment = conferenceDevelopmentEditText.text.toString().toIntOrNull() ?: 0

        val total = tithe + combinedOffering + campMeeting + thirteenthSabbath + conferenceDevelopment
        totalTextView.text = total.toString()
    }
}