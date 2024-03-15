package com.example.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

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

        val tithe = intent.getStringExtra("tithe")
        val titheAmount = intent.getIntExtra("titheAmount", 0)
        if (titheAmount > 0) {
            // Display titheAmount
            titheview.text="$tithe: $$titheAmount"
        }else{
            titheview.visibility = View.GONE
        }

        val combinedOffering = intent.getStringExtra("combinedOffering")
        val combinedOfferingAmount = intent.getIntExtra("combinedOfferingAmount", 0)
        if (combinedOfferingAmount > 0) {
            // Display combinedOfferingAmount
            offeriingview.text="$combinedOffering: $$combinedOfferingAmount"
        }else{
            offeriingview.visibility = View.GONE
        }

        val campMeeting = intent.getStringExtra("campMeeting")
        val campMeetingAmount = intent.getIntExtra("campMeetingAmount", 0)
        if (campMeetingAmount > 0) {
            // Display combinedOfferingAmount
            campmeetingview.text="$campMeeting: $$campMeetingAmount"
        }else{
            campmeetingview.visibility = View.GONE
        }

        val thirteenthSabbath = intent.getStringExtra("thirteenthSabbath")
        val thirteenthSabbathAmount = intent.getIntExtra("thirteenthSabbathAmount", 0)
        if (thirteenthSabbathAmount > 0) {
            // Display combinedOfferingAmount
            thirteenthview.text="$thirteenthSabbath: $$thirteenthSabbathAmount"
        }else{
            thirteenthview.visibility = View.GONE
        }

        val conference = intent.getStringExtra("conference")
        val conferenceFundAmount = intent.getIntExtra("conferenceFundAmount", 0)
        if (conferenceFundAmount > 0) {
            // Display combinedOfferingAmount
            conferenceview.text="$conference: $$conferenceFundAmount"
        }





    }
}