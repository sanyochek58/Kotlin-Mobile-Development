package com.example.task2_9.model

data class Cart(
    val products: List<Product>
){
    val total: Int get() = products.sumOf { it.price }
}
