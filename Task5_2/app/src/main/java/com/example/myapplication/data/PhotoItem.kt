package com.example.myapplication.data

import java.io.File

data class PhotoItem(
    val file: File,
    val name: String = file.name,
    val dateMillis : Long = file.lastModified()
)
