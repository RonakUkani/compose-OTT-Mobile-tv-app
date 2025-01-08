package com.domain.mapper

import com.data.model.response.UserDTO
import com.domain.model.User

class UserMapper {
    fun map(userDTO: UserDTO): User {
        return User(
            name = userDTO.name ?: "",
            height = userDTO.height ?: "",
            mass = userDTO.mass ?: "",
            hairColor = userDTO.hairColor ?: "",
            skinColor = userDTO.skinColor ?: "",
            eyeColor = userDTO.eyeColor ?: "",
            birthYear = userDTO.birthYear ?: "",
            gender = userDTO.gender ?: "",
            homeworld = userDTO.homeworld ?: "",
            films = userDTO.films,
            species = userDTO.species,
            vehicles = userDTO.vehicles,
            starships = userDTO.starships,
            created = userDTO.created ?: "",
            edited = userDTO.edited ?: "",
            url = userDTO.url ?: ""
        )
    }
}
