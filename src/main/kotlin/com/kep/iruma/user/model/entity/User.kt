package com.kep.iruma.user.model.entity

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.ZonedDateTime
import javax.persistence.*

@Entity
class User private constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    @Column(name = "app_user_id")
    @JsonProperty("app_user_id")
    val userId: Long,
    var nickname: String,
    var access_token: String,
    var refresh_token: String,
    val created_at: ZonedDateTime
) {
    companion object {
        fun of(
            id: Long?,
            userId: Long,
            nickname: String,
            access_token: String,
            refresh_token: String,
            created_at: ZonedDateTime
        ) = User(
            id = id,
            userId = userId,
            nickname = nickname,
            access_token = access_token,
            refresh_token = refresh_token,
            created_at = created_at
        )
    }
}