package com.example.whitelistverification.backend.model

import javax.persistence.*

@Entity
@Table(name = "users")
data class User(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int = 0,
    var email: String = "",
    var nickname: String = ""
)