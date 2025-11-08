package com.example.task2_9.model

data class Cart(
    val product: List<Product>
){
    val total : Int get() = product.sumOf { it.price }
}