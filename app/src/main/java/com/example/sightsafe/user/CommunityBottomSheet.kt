package com.example.sightsafe.user

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sightsafe.R
import com.example.sightsafe.databinding.BottomsheetlayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CommunityBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomsheetlayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomsheetlayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutRSCicendo.setOnClickListener {
            openWebsite("https://www.cicendoeyehospital.org/id/")
        }

        binding.layoutJEC.setOnClickListener {
            openWebsite("https://jec.co.id/id")
        }

        binding.layoutAAO.setOnClickListener {
            openWebsite("https://www.aao.org/")
        }

        binding.layoutIABP.setOnClickListener {
            openWebsite("https://www.iapb.org/")
        }
    }

    private fun openWebsite(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}