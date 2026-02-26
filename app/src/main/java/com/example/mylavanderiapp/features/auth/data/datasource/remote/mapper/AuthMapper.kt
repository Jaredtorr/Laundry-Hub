package com.example.mylavanderiapp.features.auth.data.datasource.remote.mapper

import com.example.mylavanderiapp.features.auth.data.datasource.remote.model.dto.UserDto
import com.example.mylavanderiapp.features.auth.domain.entities.User

fun UserDto.toDomain(): User {
    return User(
        id = this.id,
        name = this.name,
        secondName = this.secondName,
        paternalSurname = this.paternalSurname,
        maternalSurname = this.maternalSurname,
        email = this.email,
        imageProfile = this.imageProfile,
        oauthProvider = this.oauthProvider,
        role = this.role,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}