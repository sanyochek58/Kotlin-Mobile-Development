package org.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Sales(val today: String, val items: List<Item>)
