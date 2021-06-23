package com.kep.iruma.user.service

import com.kep.iruma.user.model.entity.User
import com.kep.iruma.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    val userRepository: UserRepository
) {
    fun create(user: User): User? {
        val existUser = userRepository.findByUserId(user.userId) ?: return userRepository.save(user)
        existUser.access_token = user.access_token
        existUser.refresh_token = user.refresh_token
        userRepository.save(existUser)
        return null
    }

    // User id로 찾는 경우
    fun get(id: Long): User? {
        return userRepository.findByUserId(id)
    }

    // 닉네임으로 찾는 경우
    fun get(nickname: String): User? {
        return userRepository.findByNickname(nickname)
    }

    fun getAll(): List<User> {
        return userRepository.findAll()
    }

    @Transactional
    fun update(id: Long, nickname: String) {
        val user = userRepository.findByUserId(id) ?: return
        user.nickname = nickname
        userRepository.save(user)
    }

    @Transactional
    fun delete(id: Long) {
        userRepository.deleteByUserId(id)
    }
}