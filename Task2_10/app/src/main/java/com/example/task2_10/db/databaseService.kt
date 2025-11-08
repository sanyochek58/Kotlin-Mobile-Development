package com.example.task2_10.db

import com.example.task2_10.model.User

interface DatabaseService {
    val users: List<User>
    fun addUser(user: User): Boolean
    fun login(email: String, password: String): User?
}