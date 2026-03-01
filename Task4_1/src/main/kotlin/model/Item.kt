package org.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Item(val product:String, val qty: Int, val revenue: Int)
