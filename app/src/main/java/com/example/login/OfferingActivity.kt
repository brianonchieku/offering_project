package com.example.login

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class OfferingActivity : AppCompatActivity() {

    private lateinit var radioGroup: RadioGroup
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    // Variable to hold the temporarily selected church ID
    private var temporaryChurchId: String? = null

    companion object {
        // Variable to hold the selected home church
        var selectedHomeChurch: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offering)

        radioGroup = findViewById(R.id.radioGroup)
        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        // Find the spinner view
        val spinner = findViewById<Spinner>(R.id.spinner)
        spinner.contentDescription="choose church"

        populateSpinner(spinner)


        // Button click listener for the "Proceed" button
        val proceedButton: Button = findViewById(R.id.proceedbtn1)
        proceedButton.setOnClickListener {
            // Display confirmation dialog when the "Proceed" button is clicked
            showConfirmationDialog()
        }

        // Check if a home church is selected from the radio group
        if (selectedHomeChurch != null) {
            // Use the selected home church in the confirmation dialog
            temporaryChurchId = selectedHomeChurch
            selectedHomeChurch = null // Reset selected home church after using it
        }

        // Set a listener for the radio group
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            // Get the selected radio button
            val radioButton = findViewById<RadioButton>(checkedId)

            // Check which radio button is selected
            when (radioButton.id) {
                R.id.option1 -> {
                    // Disable the spinner when the first option is selected
                    spinner.isEnabled = false
                    // If the first option is selected, retrieve and display the church name
                    retrieveUserChurch()
                }
                R.id.option2 -> {
                    // If the second option is selected, handle accordingly
                    spinner.isEnabled = true
                }
            }
        }
    }

    private fun retrieveUserChurch() {
        // Get the current user's UID
        val userId = firebaseAuth.currentUser?.uid

        if (userId != null) {
            // Query Firestore to get the user's document
            firestore.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        // Get the church code from the user's document
                        val churchCode = document.getString("Church Code")
                        // Retrieve and display the associated church name
                        retrieveChurchName(churchCode)
                    } else {
                        // User document does not exist
                        Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    // Error retrieving user document
                    Toast.makeText(
                        this,
                        "Error retrieving user data: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun retrieveChurchName(churchCode: String?) {
        if (churchCode != null) {
            // Query Firestore to get the church name corresponding to the church code
            firestore.collection("church codes").document(churchCode)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val churchName = document.getString("church name")
                        val churchInfo= "$churchCode - $churchName"
                        selectedHomeChurch = churchInfo // Assign the retrieved church name to selectedHomeChurch variable
                        // Display toast message with the retrieved church name
                        Toast.makeText(
                            this,
                            "Tithing records will be recorded for $churchName",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // Church code document does not exist
                        Toast.makeText(this, "Invalid church code", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    // Error retrieving church code document
                    Toast.makeText(
                        this,
                        "Error retrieving church information: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun populateSpinner(spinner: Spinner) {
        // Query Firestore to get the list of church codes and names
        firestore.collection("church codes")
            .get()
            .addOnSuccessListener { documents ->
                val churchList = mutableListOf<String>()
                for (document in documents) {
                    val churchCode = document.id
                    val churchName = document.getString("church name")
                    if (churchName != null) {
                        churchList.add("$churchCode - $churchName")
                    }
                }
                // Create an adapter and set it to the spinner
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, churchList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter

                // Set a listener for the spinner
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                        // Retrieve the selected church ID
                        val selectedChurchId = churchList[position]
                        // Update temporaryChurchId with the selected church ID
                        temporaryChurchId = selectedChurchId
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Do nothing if nothing is selected
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Error retrieving church data
                Toast.makeText(
                    this,
                    "Error retrieving church information: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    // Function to display a confirmation dialog
    private fun showConfirmationDialog() {
        val selectedChurch = temporaryChurchId ?: return

        // Create an AlertDialog Builder
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm Church Selection")
        builder.setMessage("Are you sure you want to contribute to $selectedChurch?")
        builder.setPositiveButton("Confirm") { dialog, _ ->
            // User confirmed selection, proceed with contributions
            val intent= Intent(this,OfferingActivity2::class.java)
            intent.putExtra("TEMP_CHURCH_ID", temporaryChurchId)
            startActivity(intent)
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            // User canceled selection, do nothing
            dialog.dismiss()
        }
        // Create and show the AlertDialog
        val dialog = builder.create()
        dialog.show()
    }
}



