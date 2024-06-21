package com.bangkit.deteksitanaman.view.camera

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bangkit.deteksitanaman.R
import com.bangkit.deteksitanaman.data.local.DetectEntity
import com.bangkit.deteksitanaman.databinding.ActivityCameraBinding
import com.bangkit.deteksitanaman.utils.FILENAME_FORMAT
import com.bangkit.deteksitanaman.utils.REQUIRED_PERMISSIONS
import com.bangkit.deteksitanaman.utils.ViewModelFactory
import com.bangkit.deteksitanaman.utils.reduceFileImage
import com.bangkit.deteksitanaman.view.DetailActivity
import com.bangkit.deteksitanaman.view.viewmodel.ScanViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private val viewModel by viewModels<ScanViewModel> {
        ViewModelFactory.getInstance(this)
    }


    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraLifecycle: Camera

    private var isFlashEnabled = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupView()
    }

    private fun setupView() {
        permissionHandler()
        setupAction()
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun setupAction() {
        binding.btnCamera.setOnClickListener {
            takePhoto()
        }
        binding.backLayout.setOnClickListener {
            onBackPressed()
        }
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (allPermissionsGranted()) {
            startCamera()
        }
        else {
            Toast.makeText(
                this,
                getString(R.string.permission_not_granted),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }
    private fun permissionHandler() {
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            permissionLauncher.launch(REQUIRED_PERMISSIONS)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraLifecycle = cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )

            } catch (exc: Exception) {
                Toast.makeText(
                    this,
                    "Gagal memunculkan kamera.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("Test", "startCamera: ${exc.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun getOutputDirectory(): File {
        val mediaDir = this.externalMediaDirs?.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else this.filesDir!!
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(outputDirectory, SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg")

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        showLoading(true)
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val rotatedFile = photoFile.reduceFileImage()
                    val savedUri = rotatedFile.toUri()
                    showLoading(false)
                    moveToDetail(savedUri)
                    saveToDatabase(savedUri)
                }

                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(this@CameraActivity, "Gagal mengambil gambar.", Toast.LENGTH_SHORT).show()
                    Log.e("Test", "onError: ${exc.message}")
                }
            }
        )
    }

    private fun saveToDatabase(savedUri: Uri) {
        val scanHistory = DetectEntity(
            scanImage = savedUri.toString(),
            plantName = getString(R.string.plant_name),
            plantDesc = getString(R.string.plant_desc),
            plantType = getString(R.string.sayuran)
        )
        viewModel.addScanToHistory(scanHistory)
    }

    private fun moveToDetail(savedUri: Uri) {
        Toast.makeText(this, "Berhasil Memasukkan gambar ke riwayat", Toast.LENGTH_SHORT).show()
        val intent = Intent(this@CameraActivity, DetailActivity::class.java)
        intent.putExtra("imageUri", savedUri.toString())
        startActivity(intent)
        finish()
    }

    private fun showLoading(onLoading: Boolean) {
        binding.progressBar.visibility = if (onLoading) View.VISIBLE else View.GONE
    }


}