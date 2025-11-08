package com.example.task2_10.db

import com.example.task2_10.model.User

class Database : DatabaseService {
    private val _users = mutableListOf<User>()

    override val users: List<User>
        get() = _users.toList()

    override fun addUser(user: User): Boolean {
        return _users.add(user)
    }

    override fun login(email: String, password: String): User? {
        return _users.find { it.email == email && it.password == password }
    }
}