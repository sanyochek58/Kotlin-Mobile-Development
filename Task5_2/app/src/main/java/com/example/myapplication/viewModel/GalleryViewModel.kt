package com.example.myapplication.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.PhotoItem
import com.example.myapplication.repository.PhotoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class GalleryViewModel(application: Application): AndroidViewModel(application) {

    private val repository = PhotoRepository(application.applicationContext)

    private val _photos = MutableStateFlow<List<PhotoItem>>(emptyList())
    val photos: StateFlow<List<PhotoItem>> = _photos

    private val _snackbarEvent = MutableSharedFlow<String>()
    val snackbarEvent: SharedFlow<String> = _snackbarEvent

    var pendingPhotoFile: File? = null

    init {
        loadPhotos()
    }

    fun loadPhotos() {
        viewModelScope.launch {
            val loaded = withContext(Dispatchers.IO) { repository.loadPhotos() }
            _photos.value = loaded
        }
    }

    fun createPhotoFile(): File {
        val file = repository.createPhotoFile()
        pendingPhotoFile = file
        return file
    }

    fun onPhotoCaptured() {
        loadPhotos()
        pendingPhotoFile = null
    }

    fun exportToGallery(photo: PhotoItem) {
        viewModelScope.launch {
            val success = withContext(Dispatchers.IO) { repository.exportToGallery(photo) }
            _snackbarEvent.emit(
                if (success) "Фото добавлено в галерею" else "Ошибка экспорта"
            )
        }
    }
}