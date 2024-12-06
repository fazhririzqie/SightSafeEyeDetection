package com.example.sightsafe.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sightsafe.databinding.BottomsheetprofileBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth

class ProfileBottomSheet : BottomSheetDialogFragment() {
    private var _binding: BottomsheetprofileBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomsheetprofileBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.Logout.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage("Apakah yakin akan logout?")
            .setPositiveButton("Iya") { dialog, id ->
                firebaseAuth.signOut()
                val intent = Intent(activity, WelcomeActivity::class.java)
                startActivity(intent)
                activity?.finish() // Close MainActivity to prevent returning to it
            }
            .setNegativeButton("Tidak") { dialog, id ->
                dialog.dismiss()
            }
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}