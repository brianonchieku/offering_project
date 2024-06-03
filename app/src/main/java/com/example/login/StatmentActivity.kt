package com.example.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StatmentActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var statementsAdapter: recviewadapter
    private lateinit var statementsList: MutableList<StatementsData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statment)

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.recview)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize the statementsList and add some data
        statementsList = mutableListOf()
        statementsList.add(StatementsData("100", "50", "20", "30", "40", "240"))

        // Initialize the adapter with the statementsList
        statementsAdapter = recviewadapter(statementsList)
        recyclerView.adapter = statementsAdapter
    }
}