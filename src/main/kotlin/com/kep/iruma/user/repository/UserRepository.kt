package com.kep.iruma.user.repository

import com.kep.iruma.user.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByUserId(id: Long): User?
    fun deleteByUserId(id: Long)
    fun findByNickname(nickname: String): User?
}
