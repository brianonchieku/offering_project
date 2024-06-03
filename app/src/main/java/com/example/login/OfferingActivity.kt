package com.example.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class OfferingActivity : AppCompatActivity() {

    private lateinit var radioGroup: RadioGroup
    private lateinit var spinner: Spinner
    private lateinit var proceedButton: Button
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private var homeChurch: String? = null
    private var temporaryChurchId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offering)

        radioGroup = findViewById(R.id.radioGroup)
        spinner = findViewById(R.id.spinner)
        proceedButton = findViewById(R.id.proceedbtn1)
        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()


        // Set the first radio button as checked and disable the spinner
        radioGroup.check(R.id.option1)
        findViewById<Spinner>(R.id.spinner).isEnabled = false

        getHomeChurch()
        spinner.isEnabled = false



        // Set up the spinner with church data
        populateSpinner()

        // Set the listener for the radio group
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.option1 -> {
                    // Handle the first radio button (Home church)
                    getHomeChurch()
                    spinner.isEnabled = false // Disable spinner
                }
                R.id.option2 -> {
                    // Handle the second radio button (Host church)
                    homeChurch = null // Set homeChurch to null
                    spinner.isEnabled = true // Enable spinner
                    spinner.setSelection(0) // Reset spinner selection to no default value
                }
            }
        }

        // Set click listener for the proceed button
        proceedButton.setOnClickListener {
            // Proceed with the selected church (homeChurch or spinner)
            val selectedChurch = homeChurch ?: temporaryChurchId
            // Your code to proceed with the selected church goes here
            showConfirmationDialog(selectedChurch)
        }
    }

    private fun populateSpinner() {
        // Query Firestore to get the list of church codes and names
        firestore.collection("church codes")
            .get()
            .addOnSuccessListener { documents ->
                val churchList = mutableListOf<String>()
                churchList.add("Select church...") // Add default prompt to spinner
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
                        if (position == 0) {
                            temporaryChurchId = null // Set temporaryChurchId to null when no selection
                        } else {
                            // Retrieve the selected church ID
                            temporaryChurchId = churchList[position]
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Set temporaryChurchId to null when nothing is selected
                        temporaryChurchId = null
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

    private fun getHomeChurch() {
        // Retrieve the current user's UID
        val userId = firebaseAuth.currentUser?.uid

        if (userId != null) {
            // Query Firestore to get the user's document
            firestore.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        // Get the church code from the user's document
                        homeChurch = document.getString("church code")
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
        } else {
            // User is not logged in
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showConfirmationDialog(selectedChurch: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm Church Selection")
        builder.setMessage("Are you sure you want to contribute to $selectedChurch?")

        builder.setPositiveButton("Proceed") { dialog, _ ->
            // Proceed with the selected church
            Toast.makeText(this, "Selected church: $selectedChurch", Toast.LENGTH_SHORT).show()
            // Add your code here to proceed with the selected church
            val intent= Intent(this,OfferingActivity2::class.java)
            intent.putExtra("TEMP_CHURCH_ID", selectedChurch)
            startActivity(intent)
            dialog.dismiss()

        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }
}
