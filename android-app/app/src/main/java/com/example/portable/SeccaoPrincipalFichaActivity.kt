package com.example.portable

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.portable.databinding.ActivitySeccaoPrincipalFichaBinding

class SeccaoPrincipalFichaActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySeccaoPrincipalFichaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeccaoPrincipalFichaBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}