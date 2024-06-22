package com.bangkit.deteksitanaman.view.history

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.deteksitanaman.R
import com.bangkit.deteksitanaman.databinding.ActivityHistoryBinding
import com.bangkit.deteksitanaman.utils.ViewModelFactory
import com.bangkit.deteksitanaman.view.adapters.AllHistoryAdapter
import com.bangkit.deteksitanaman.view.viewmodel.ScanViewModel

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private val viewModel by viewModels<ScanViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var adapter: AllHistoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setDataView()
        binding.backLayout.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setDataView() {
        binding.detectAllRv.layoutManager = LinearLayoutManager(this)
        adapter = AllHistoryAdapter(
            onItemClick = { scanHistoryItem ->
                val intent = Intent(this, HistoryDetailActivity::class.java)
                intent.putExtra("SCAN_HISTORY_ID", scanHistoryItem.id)
                startActivity(intent)
            },
            onDeleteClick = { scanHistoryItem ->
                viewModel.deleteScanHistory(scanHistoryItem.id)
                Toast.makeText(this, resources.getString(R.string.deleteSuccess), Toast.LENGTH_SHORT).show()
            }
        )

        binding.detectAllRv.adapter = adapter

        viewModel.scanHistory.observe(this) { scanHistory ->
            adapter.submitList(scanHistory)
        }

        viewModel.getScanHistory()
    }
}