package com.domain.model

data class VideosList(
    val total: Int? = 0,
    val totalHits: Int? = 0,
    val hits: MutableList<Hit> = mutableListOf()
) {
    data class Hit(
        val id: Int? = 0,
        val pageURL: String? = "",
        val type: String? = "",
        val tags: String? = "",
        val duration: Int? = 0,
        val videos: Videos? = Videos(),
        val views: Int? = 0,
        val downloads: Int? = 0,
        val likes: Int? = 0,
        val comments: Int? = 0,
        val userId: Int? = 0,
        val user: String? = "",
        val userImageURL: String? = "",
        var lastPlayedPosition: Long? = null
    ) {
        data class Videos(
            val large: Large? = Large(),
            val medium: Medium? = Medium(),
            val small: Small? = Small(),
            val tiny: Tiny? = Tiny()
        ) {

            data class Large(
                val url: String? = "",
                val width: Int? = 0,
                val height: Int? = 0,
                val size: Int? = 0,
                val thumbnail: String? = ""
            )

            data class Medium(
                val url: String? = "",
                val width: Int? = 0,
                val height: Int? = 0,
                val size: Int? = 0,
                val thumbnail: String? = ""
            )

            data class Small(
                val url: String? = "",
                val width: Int? = 0,
                val height: Int? = 0,
                val size: Int? = 0,
                val thumbnail: String? = ""
            )

            data class Tiny(
                val url: String? = "",
                val width: Int? = 0,
                val height: Int? = 0,
                val size: Int? = 0,
                val thumbnail: String? = ""
            )
        }
    }
}