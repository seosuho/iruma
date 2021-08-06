package com.kep.iruma.user.controller

import com.kep.iruma.user.service.UserService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var userService: UserService

    @DisplayName("유저 ID로 검색")
    @Test
    fun getUser() {
        //given
        val user = userService.get("sean") ?: return

        //when
        val action = mockMvc.perform(
            get("/api/user/${user.userId}")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(print())

        //then
        action
            .andExpect(status().isOk)
            .andExpect(jsonPath("id").value(user.id!!))
            .andExpect(jsonPath("nickname").value(user.nickname))
            .andExpect(jsonPath("app_user_id").value(user.userId))
    }

    @DisplayName("유저 닉네임 업데이트")
    @Test
    @Transactional
    fun updateUser() {
        //given
        val user = userService.get("sean") ?: return

        //when
        val action = mockMvc.perform(
            put("/api/user/${user.userId}?nickname=babo")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(print())

        //then
        action
            .andExpect(status().isOk)
    }

    @DisplayName("유저 삭제")
    @Test
    @Transactional
    fun deleteUser() {
        //given
        val user = userService.get("sean") ?: return

        //when
        val action = mockMvc.perform(
            delete("/api/user/${user.userId}")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(print())

        //then
        action
            .andExpect(status().isOk)
    }

    @DisplayName("모든 유저 검색")
    @Test
    fun getAllUsers() {
        //given
        val users = userService.getAll()

        //when
        val action = mockMvc.perform(
            get("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(print())

        //then
        action
            .andExpect(status().isOk)
            .andExpect(jsonPath("length()").value(users.size))
            .andExpect(jsonPath("$[0].nickname").value(users[0].nickname))
            .andExpect(jsonPath("$[1].nickname").value(users[1].nickname))
    }

    @DisplayName("닉네임으로 유저 찾기")
    @Test
    fun getUserByNickName() {
        //given
        val user = userService.get("Harry") ?: return

        //when
        val action = mockMvc.perform(
            get("/api/users?nickname=${user.nickname}")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(print())

        //then
        action
            .andExpect(status().isOk)
            .andExpect(jsonPath("id").value(user.id!!))
            .andExpect(jsonPath("nickname").value(user.nickname))
            .andExpect(jsonPath("app_user_id").value(user.userId))
    }
}