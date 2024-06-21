package com.bangkit.deteksitanaman.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.deteksitanaman.data.local.DetectEntity
import com.bangkit.deteksitanaman.data.models.response.LoginResult
import com.bangkit.deteksitanaman.data.repository.DataRepository
import kotlinx.coroutines.launch
import java.io.File

class ScanViewModel(private val repository: DataRepository) : ViewModel() {

    private val _scanHistory = MutableLiveData<List<DetectEntity>>()
    val scanHistory: LiveData<List<DetectEntity>> get() = _scanHistory

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getSession(): LiveData<LoginResult> {
        return repository.getUser().asLiveData()
    }

    fun addScanToHistory(scanHistory: DetectEntity) {
        viewModelScope.launch {
            repository.addScanToHistory(scanHistory)
        }
    }

    fun getScanHistory() {
        viewModelScope.launch {
            val history = repository.getScanHistory()
            _scanHistory.postValue(history)
        }
    }

    fun deleteScanHistory(id: Int) {
        viewModelScope.launch {
            repository.deleteScanHistory(id)
            getScanHistory()
        }
    }
}