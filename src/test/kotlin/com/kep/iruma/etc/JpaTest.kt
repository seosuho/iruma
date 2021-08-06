package com.kep.iruma.etc

import com.kep.iruma.user.model.entity.User
import com.kep.iruma.user.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.TestPropertySource
import java.time.ZonedDateTime

//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
//@TestPropertySource(locations = ["classpath:/jpatest.properties"])
//@AutoConfigureMockMvc
@SpringBootTest
class JpaTest {

    @Autowired
    lateinit var userRepository: UserRepository

    @DisplayName("DB 유저 생성")
    @Order(1)
    @Rollback(false)
    @Test
    fun createTest() {
        // given
        val user = User.of(1, 1, "sean", "1", "1", ZonedDateTime.now())

        // when
        val saveUser = userRepository.save(user)

        //then
        assertNotNull(saveUser)
    }

    @DisplayName("DB 유저 읽어오기")
    @Order(2)
    @Test
    fun readTest() {
        // given
        val user = userRepository.findByNickname("harry") ?: throw NullPointerException("Empty User!")

        // when
        val saveUser = userRepository.findByUserId(user.userId) ?: throw NullPointerException("Empty User!")

        //then
        assertThat(saveUser.nickname).isEqualTo(user.nickname)
        assertThat(saveUser.access_token).isEqualTo(user.access_token)
        assertThat(saveUser.refresh_token).isEqualTo(user.refresh_token)
        assertThat(saveUser.created_at).isEqualTo(user.created_at)
    }

    @DisplayName("DB 유저 닉네임 업데이트")
    @Order(3)
    @Test
    fun updateTest() {
        // given
        val user = userRepository.findByNickname("harry") ?: throw NullPointerException("Empty User!")
        user.nickname = "babo"

        // when
        val saveUser = userRepository.findByUserId(user.userId) ?: throw NullPointerException("Empty User!")

        //then
        assertThat(saveUser.nickname).isEqualTo(user.nickname)
        assertThat(saveUser.access_token).isEqualTo(user.access_token)
        assertThat(saveUser.refresh_token).isEqualTo(user.refresh_token)
        assertThat(saveUser.created_at).isEqualTo(user.created_at)
    }

    @DisplayName("DB 유저 삭제")
    @Order(4)
    @Test
    fun deleteTest() {
        // given
        val user = userRepository.findByNickname("babo") ?: throw NullPointerException("Empty User!")
        userRepository.deleteByUserId(user.userId)

        // when
        val saveUser = userRepository.findByUserId(user.userId)

        //then
        assertThat(saveUser).isNull()
    }
}