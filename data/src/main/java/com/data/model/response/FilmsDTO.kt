package com.data.model.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class FilmsDTO(
    @SerialName("count")
    val count: Int? = 0,
    @SerialName("next")
    val next: Int? = 0,
    @SerialName("previous")
    val previous: Int? = 0,
    @SerialName("results")
    val results: List<Result?>? = listOf()
) {
    @Serializable
    data class Result(
        @SerialName("title")
        val title: String? = "",
        @SerialName("episode_id")
        val episodeId: Int? = 0,
        @SerialName("opening_crawl")
        val openingCrawl: String? = "",
        @SerialName("director")
        val director: String? = "",
        @SerialName("producer")
        val producer: String? = "",
        @SerialName("release_date")
        val releaseDate: String? = "",
        @SerialName("characters")
        val characters: List<String?>? = listOf(),
        @SerialName("planets")
        val planets: List<String?>? = listOf(),
        @SerialName("starships")
        val starships: List<String?>? = listOf(),
        @SerialName("vehicles")
        val vehicles: List<String?>? = listOf(),
        @SerialName("species")
        val species: List<String?>? = listOf(),
        @SerialName("created")
        val created: String? = "",
        @SerialName("edited")
        val edited: String? = "",
        @SerialName("url")
        val url: String? = ""
    )
}