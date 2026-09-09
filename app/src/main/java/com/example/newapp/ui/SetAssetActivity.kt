package com.example.newapp.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.newapp.databinding.ActivityAssetBinding
import com.example.newapp.utils.PreferenceManager

class SetAssetActivity: AppCompatActivity() {

    private var _binding: ActivityAssetBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityAssetBinding.inflate(
            LayoutInflater.from(this)
        )

        setContentView(binding.root)

        PreferenceManager.getInstance(this)
            .getValue(PreferenceManager.KEY_ASSET_NO)?.let {
                navigateToCSRActivity()
            }

        binding.btnSave.setOnClickListener {
            PreferenceManager.getInstance(this).add(
                PreferenceManager.KEY_ASSET_NO, binding.etAssetNo.text.toString()
            )
            navigateToCSRActivity()
        }

        binding.etAssetNo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.btnSave.isEnabled = binding.etAssetNo.text.isNotEmpty()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

    }

    private fun navigateToCSRActivity() {
        startActivity(Intent(this@SetAssetActivity, QCSRActivity::class.java))
        finish()
    }
}