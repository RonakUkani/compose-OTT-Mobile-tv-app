package com.domain.model

data class User(
    val name: String? = "",
    val height: String? = "",
    val mass: String? = "",
    val hairColor: String? = "",
    val skinColor: String? = "",
    val eyeColor: String? = "",
    val birthYear: String? = "",
    val gender: String? = "",
    val homeworld: String? = "",
    val films: List<String> = listOf(),
    val species: List<String> = listOf(),
    val vehicles: List<String> = listOf(),
    val starships: List<String> = listOf(),
    val created: String? = "",
    val edited: String? = "",
    val url: String? = ""
)
