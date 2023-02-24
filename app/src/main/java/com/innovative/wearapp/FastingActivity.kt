package com.innovative.wearapp

import android.app.Activity
import android.os.Bundle
import com.innovative.wearapp.databinding.ActivityFastingBinding

class FastingActivity : Activity() {

    private lateinit var binding: ActivityFastingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFastingBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}