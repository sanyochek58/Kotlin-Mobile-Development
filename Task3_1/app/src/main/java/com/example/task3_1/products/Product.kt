package com.example.task3_1.products

import androidx.annotation.DrawableRes
import com.example.task3_1.R


data class Product(
    @DrawableRes val image: Int,
    val title: String,
    val description: String
)

val productList = listOf(
    Product(R.drawable.apple, "Apple", "Red apple from sun Scotland farm"),
    Product(R.drawable.tomato, "Tomato", "Tomato from Texas farm"),
    Product(R.drawable.potato, "Potato", "Potato from Belarus farm"),
    Product(R.drawable.cucumber, "Cucumber", "Cucumber from sun Russia farm"),
    Product(R.drawable.banana, "Banana", "Banana from sun Africa"),
    Product(R.drawable.meat, "Meat", "Meat from Cherkizovo fabric"),
)
