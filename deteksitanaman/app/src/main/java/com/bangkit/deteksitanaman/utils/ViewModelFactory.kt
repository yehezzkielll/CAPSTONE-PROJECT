package com.bangkit.deteksitanaman.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.deteksitanaman.data.repository.DataRepository
import com.bangkit.deteksitanaman.view.viewmodel.ScanViewModel
import com.bangkit.deteksitanaman.view.viewmodel.UserViewModel

class ViewModelFactory(private val repository: DataRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(UserViewModel::class.java) -> {
                UserViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ScanViewModel::class.java) -> {
                ScanViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Utility.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}