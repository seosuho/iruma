package com.kep.iruma.user.controller

import com.kep.iruma.user.model.entity.User
import com.kep.iruma.user.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
    val userService: UserService
) {

    // 개인 회원 API
    @GetMapping("/api/user/{appUserId}")
    fun getUser(@PathVariable appUserId: Long): User? {
        return userService.get(appUserId)
    }

    @PutMapping("/api/user/{appUserId}")
    fun updateUser(@PathVariable appUserId: Long, @RequestParam nickname: String) {
        userService.update(appUserId, nickname)
    }

    @DeleteMapping("/api/user/{appUserId}")
    fun deleteUser(@PathVariable appUserId: Long) {
        userService.delete(appUserId)
    }

    // 전체 회원 API
    @GetMapping("/api/users")
    fun getUsers(): List<User> {
        return userService.getAll()
    }

    @GetMapping("/api/users", params = ["nickname"])
    fun getUser(@RequestParam nickname: String): User? {
        return userService.get(nickname)
    }
}

