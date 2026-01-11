package com.example.portable

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.portable.databinding.ActivityListaDeFichasBinding

class ListaDeFichasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListaDeFichasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaDeFichasBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}