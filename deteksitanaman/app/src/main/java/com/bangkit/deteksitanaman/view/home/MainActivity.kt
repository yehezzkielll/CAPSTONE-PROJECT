package com.bangkit.deteksitanaman.view.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.deteksitanaman.R
import com.bangkit.deteksitanaman.databinding.ActivityMainBinding
import com.bangkit.deteksitanaman.utils.ViewModelFactory
import com.bangkit.deteksitanaman.view.adapters.Article
import com.bangkit.deteksitanaman.view.adapters.ArticleAdapter
import com.bangkit.deteksitanaman.view.adapters.HistoryAdapter
import com.bangkit.deteksitanaman.view.auth.LoginActivity
import com.bangkit.deteksitanaman.view.camera.CameraActivity
import com.bangkit.deteksitanaman.view.history.HistoryActivity
import com.bangkit.deteksitanaman.view.history.HistoryDetailActivity
import com.bangkit.deteksitanaman.view.viewmodel.ScanViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ArticleAdapter
    private lateinit var historyAdapter: HistoryAdapter
    private val viewModel by viewModels<ScanViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupView()
    }

    override fun onResume() {
        super.onResume()
        binding.swipeRefresh.setOnRefreshListener {
            setupView()
        }
        historyDataSetup()
    }

    private fun setupView() {
        articleSetup()
        historyDataSetup()
        setupAction()
        setupName()
        binding.swipeRefresh.isRefreshing = false
    }

    private fun setupName() {
        viewModel.getSession().observe(this) { user ->
            binding.userText.text = getString(R.string.nameUser, user.nameUser)
        }
    }

    private fun setupAction() {
        binding.cameraFab.setOnClickListener { moveToCamera() }
        binding.showAllHistoryBtn.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
        binding.menuBtn.setOnClickListener {
            viewModel.logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun historyDataSetup() {
        binding.historyRv.layoutManager = LinearLayoutManager(this)
        historyAdapter = HistoryAdapter(
            onItemClick = { scanHistoryItem ->
                val intent = Intent(this, HistoryDetailActivity::class.java)
                intent.putExtra("SCAN_HISTORY_ID", scanHistoryItem.id)
                startActivity(intent)
            }
        )

        binding.historyRv.adapter = historyAdapter
        binding.historyRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        viewModel.scanHistory.observe(this) { scanHistory ->
            historyAdapter.submitList(scanHistory)
        }

        viewModel.getScanHistory()
    }

    private fun moveToCamera() {
        startActivity(Intent(this, CameraActivity::class.java))
    }

    private fun articleSetup() {
        val articles = createArticles()
        setupViewPager(articles)
    }

    @SuppressLint("Recycle")
    private fun createArticles(): List<Article> {
        val images = resources.obtainTypedArray(R.array.articleImage)
        val text = resources.getStringArray(R.array.articleText)

        val articles = mutableListOf<Article>()
        for (i in text.indices) {
            articles.add(
                Article(
                    images.getResourceId(i, -1),
                    text[i]
                )
            )
        }
        return articles
    }

    private fun setupViewPager(articles: List<Article>) {
        adapter = ArticleAdapter(articles)
        binding.viewPager.adapter = adapter
    }
}