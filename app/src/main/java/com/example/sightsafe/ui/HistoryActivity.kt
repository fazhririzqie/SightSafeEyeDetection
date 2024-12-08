package com.example.sightsafe.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sightsafe.adapter.Result
import com.example.sightsafe.adapter.ResultAdapter
import com.example.sightsafe.databinding.ActivityHistoryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var database: DatabaseReference
    private lateinit var resultList: MutableList<Result>
    private lateinit var adapter: ResultAdapter
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth and Database
        firebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.currentUser?.uid ?: return
        database = FirebaseDatabase.getInstance().reference.child("users").child(userId).child("results")

        // Initialize RecyclerView
        resultList = mutableListOf()
        adapter = ResultAdapter(resultList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Fetch data from Firebase
        fetchResultsFromFirebase()
    }

    private fun fetchResultsFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                resultList.clear()
                for (dataSnapshot in snapshot.children) {
                    val result = dataSnapshot.getValue(Result::class.java)
                    result?.let { resultList.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HistoryActivity, "Failed to load results", Toast.LENGTH_SHORT).show()
            }
        })
    }
}