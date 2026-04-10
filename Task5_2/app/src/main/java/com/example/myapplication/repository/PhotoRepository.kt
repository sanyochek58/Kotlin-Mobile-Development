package com.example.myapplication.repository

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import com.example.myapplication.data.PhotoItem
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PhotoRepository (private val context: Context){

    private val photoDir: File
        get() = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            ?: context.filesDir

    fun loadPhotos(): List<PhotoItem> {
        return photoDir
            .listFiles{ file -> file.extension.lowercase() in listOf("jpg", "png", "jpeg") }
            ?.sortedByDescending { it.lastModified() }
            ?.map { PhotoItem(it) }
            ?: emptyList()
    }

    fun createPhotoFile(): File{
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "IMG_$timestamp.jpg"
        return File(photoDir.also { it.mkdirs() }, fileName)
    }

    fun exportToGallery(photo: PhotoItem): Boolean {
        return runCatching {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, photo.name)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES + "/MyGalleryApp"
                )
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            ) ?: return false

            resolver.openOutputStream(uri)?.use { output ->
                photo.file.inputStream().use { input ->
                    input.copyTo(output)
                }
            }

            contentValues.clear()
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
            resolver.update(uri, contentValues, null, null)
            true
        }.getOrDefault(false)
    }
}