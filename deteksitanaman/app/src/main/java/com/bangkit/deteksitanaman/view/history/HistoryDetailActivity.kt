package com.bangkit.deteksitanaman.view.history

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bangkit.deteksitanaman.R
import com.bangkit.deteksitanaman.databinding.ActivityHistoryDetailBinding
import com.bangkit.deteksitanaman.utils.ViewModelFactory
import com.bangkit.deteksitanaman.view.viewmodel.ScanViewModel
import com.bumptech.glide.Glide

class HistoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryDetailBinding
    private val viewModel by viewModels<ScanViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryDetailBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupViewByData()
        binding.backLayout.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupViewByData() {
        val Id = intent.getIntExtra("SCAN_HISTORY_ID",0)
        Log.d("ScanHistoryDetail", "Id: $Id")

        viewModel.scanHistory.observe(this) { scanHistory ->
            val scanHistoryItem = scanHistory.find { it.id == Id }
            with(binding) {
                Glide.with(this@HistoryDetailActivity)
                    .load(scanHistoryItem?.scanImage)
                    .into(plantImage)
                namaBunga.text = scanHistoryItem?.plantName
                descPlant.text = scanHistoryItem?.plantDesc
                latinBunga.text = scanHistoryItem?.plantType
            }
        }

        viewModel.getScanHistory()
    }
}