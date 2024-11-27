package com.example.sightsafe.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sightsafe.R
import com.example.sightsafe.databinding.FragmentSettingBinding
import com.example.sightsafe.user.LoginUser
import com.google.firebase.auth.FirebaseAuth

class FragmentSetting : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize the binding property
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Now you can safely use the binding property
        binding.logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(activity, LoginUser::class.java)
            startActivity(intent)
            activity?.finish() // Close MainActivity to prevent returning to it
        }
    }


}