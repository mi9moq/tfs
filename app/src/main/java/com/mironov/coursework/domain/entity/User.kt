package com.mironov.coursework.domain.entity

data class User(
    val id: Int,
    val userName: String,
    val email: String,
    val avatarUrl: String,
    val presence: Presence,
){
    enum class Presence {
        ACTIVE,
        IDLE,
        OFFLINE;
    }
}
