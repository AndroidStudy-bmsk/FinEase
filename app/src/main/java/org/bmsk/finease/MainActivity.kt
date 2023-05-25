package org.bmsk.finease

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.bmsk.finease.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.view = this
    }

    fun openShuffle() {

    }

    fun openVerifyOTP() {

    }
}