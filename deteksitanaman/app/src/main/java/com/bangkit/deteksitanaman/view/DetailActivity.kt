package com.bangkit.deteksitanaman.view

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bangkit.deteksitanaman.R
import com.bangkit.deteksitanaman.databinding.ActivityDetailBinding
import com.bangkit.deteksitanaman.utils.helper.ImageClassifierHelper
import org.tensorflow.lite.task.vision.classifier.Classifications

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var image: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupView()
    }

    private fun setupView() {
        val imageUri = intent.getStringExtra("imageUri")
        if (!imageUri.isNullOrEmpty()) {
            val uri = Uri.parse(imageUri)
            displayImage(uri)
//            analyzeImage(uri)
        }
        binding.backLayout.setOnClickListener {
            onBackPressed()
        }
    }

    private fun analyzeImage(uri: Uri) {
        image = uri.toString()
        val imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    Log.d("TAG", "ShowImage: $image")
                }

                @SuppressLint("SetTextI18n")
                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    results?.let {
                        val topResult = it[0]
                        val label = topResult.categories[0].label
                        val finalScore = topResult.categories[0].score

                        fun Float.formatToString(): String {
                            return String.format("%.2f%%", this * 100)
                        }
                        Log.d("TAG", "onResults: $label")
                        Log.d("TAG", "onResults: $finalScore")
                        binding.namaBunga.text = label
                        binding.descPlant.text = finalScore.formatToString()
                    }
                }
            }
        )
        imageClassifierHelper.classifyStaticImage(uri)
    }

    private fun displayImage(uri: Uri?) {
        binding.plantImage.setImageURI(uri)
    }

}