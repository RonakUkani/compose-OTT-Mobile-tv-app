package com.domain.model

data class Films(
    val count: Int? = 0,
    val next: Any? = Any(),
    val previous: Any? = Any(),
    val results: List<Result> = listOf()
) {
    data class Result(
        val title: String? = "",
        val episodeId: Int? = 0,
        val openingCrawl: String? = "",
        val director: String? = "",
        val producer: String? = "",
        val releaseDate: String? = "",
        val characters: List<String?>? = listOf(),
        val planets: List<String?>? = listOf(),
        val starships: List<String?>? = listOf(),
        val vehicles: List<String?>? = listOf(),
        val species: List<String?>? = listOf(),
        val created: String? = "",
        val edited: String? = "",
        val url: String? = "",
        var thumbUrl: String? = "",
        var videoUrl: String? = "",
        var isLiveUrl: Boolean = false,
    )
}
