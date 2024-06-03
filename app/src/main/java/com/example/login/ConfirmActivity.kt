package com.example.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.telecom.Call
import android.view.View
import android.widget.TextView
import com.google.android.gms.common.api.Response
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Retrofit
import javax.security.auth.callback.Callback



class ConfirmActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)

        val tochurch: TextView =findViewById(R.id.tochurch)
        val foruser: TextView =findViewById(R.id.foruser)
        val btn: TextView =findViewById(R.id.submitbtn)
        val titheview: TextView =findViewById(R.id.titheview)
        val offeriingview: TextView =findViewById(R.id.offeringview)
        val campmeetingview: TextView =findViewById(R.id.campmeetingview)
        val thirteenthview: TextView =findViewById(R.id.thirteenthview)
        val conferenceview: TextView =findViewById(R.id.conferenceview)
        val totalview: TextView =findViewById(R.id.totalview)
        val phoneno: TextView =findViewById(R.id.phonenumber)



        // Retrieve the church name from the temporaryChurchId passed from the previous activity
        val churchName = intent.getStringExtra("TEMP_CHURCH_NAME")
        // Set the text of the To EditText
        churchName?.let { tochurch.text = it }

        // Retrieve the current user's display name from Firebase Authentication
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userName = currentUser?.displayName

        // Set the retrieved user name in the "For" EditText
        userName?.let { foruser.text = it }

        foruser.text= "$userName"
        tochurch.text="$churchName"

        val titheAmount = intent.getIntExtra("Tithe", 0)
        val combinedOfferingAmount = intent.getIntExtra("combinedOffering", 0)
        val  campMeetingAmount = intent.getIntExtra("campMeeting", 0)
        val thirteenthSabbathAmount = intent.getIntExtra("thirteenthSabbath", 0)
        val conferenceDevelopmentAmount = intent.getIntExtra("conferenceDevelopment", 0)
        val totalAmount = intent.getIntExtra("total", 0)



        // Set the retrieved values in the TextViews if they are greater than zero
        val titheValueTextView = findViewById<TextView>(R.id.titheview)
        if (titheAmount > 0) {
            titheValueTextView.text = "Tithe: $titheAmount"
        } else {
            titheValueTextView.visibility = View.GONE // Hide the TextView if value is zero
        }

        val combinedOfferingValueTextView = findViewById<TextView>(R.id.offeringview)
        if (combinedOfferingAmount > 0) {
            combinedOfferingValueTextView.text = "Combined Offering: $combinedOfferingAmount"
        } else {
            combinedOfferingValueTextView.visibility = View.GONE // Hide the TextView if value is zero
        }

        val campMeetingValueTextView = findViewById<TextView>(R.id.campmeetingview)
        if (campMeetingAmount > 0) {
            campMeetingValueTextView.text = "Camp Meeting: $campMeetingAmount"
        } else {
            campMeetingValueTextView.visibility = View.GONE // Hide the TextView if value is zero
        }

        val thirteenthSabbathValueTextView = findViewById<TextView>(R.id.thirteenthview)
        if (thirteenthSabbathAmount > 0) {
            thirteenthSabbathValueTextView.text = "Thirteenth Sabbath: $thirteenthSabbathAmount"
        } else {
            thirteenthSabbathValueTextView.visibility = View.GONE // Hide the TextView if value is zero
        }

        val conferenceDevelopmentValueTextView = findViewById<TextView>(R.id.conferenceview)
        if (conferenceDevelopmentAmount > 0) {
            conferenceDevelopmentValueTextView.text = "Conference Development: $conferenceDevelopmentAmount"
        } else {
            conferenceDevelopmentValueTextView.visibility = View.GONE // Hide the TextView if value is zero
        }

        val totalValueTextView = findViewById<TextView>(R.id.totalview)
        if (totalAmount > 0) {
            totalValueTextView.text = "total: $totalAmount"
        } else {
            totalValueTextView.visibility = View.GONE // Hide the TextView if value is zero
        }


        btn.setOnClickListener {

        }
    }

}