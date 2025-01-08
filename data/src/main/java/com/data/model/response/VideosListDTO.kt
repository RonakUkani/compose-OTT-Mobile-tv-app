package com.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideosListDTO(
    @SerialName("total")
    val total: Int? = 0,
    @SerialName("totalHits")
    val totalHits: Int? = 0,
    @SerialName("hits")
    val hits: MutableList<Hit> = mutableListOf()
) {
    @Serializable
    data class Hit(
        @SerialName("id")
        val id: Int? = 0,
        @SerialName("pageURL")
        val pageURL: String? = "",
        @SerialName("type")
        val type: String? = "",
        @SerialName("tags")
        val tags: String? = "",
        @SerialName("duration")
        val duration: Int? = 0,
        @SerialName("videos")
        val videos: Videos? = Videos(),
        @SerialName("views")
        val views: Int? = 0,
        @SerialName("downloads")
        val downloads: Int? = 0,
        @SerialName("likes")
        val likes: Int? = 0,
        @SerialName("comments")
        val comments: Int? = 0,
        @SerialName("user_id")
        val userId: Int? = 0,
        @SerialName("user")
        val user: String? = "",
        @SerialName("userImageURL")
        val userImageURL: String? = ""
    ) {
        @Serializable
        data class Videos(
            @SerialName("large")
            val large: Large? = Large(),
            @SerialName("medium")
            val medium: Medium? = Medium(),
            @SerialName("small")
            val small: Small? = Small(),
            @SerialName("tiny")
            val tiny: Tiny? = Tiny()
        ) {
            @Serializable
            data class Large(
                @SerialName("url")
                val url: String? = "",
                @SerialName("width")
                val width: Int? = 0,
                @SerialName("height")
                val height: Int? = 0,
                @SerialName("size")
                val size: Int? = 0,
                @SerialName("thumbnail")
                val thumbnail: String? = ""
            )

            @Serializable
            data class Medium(
                @SerialName("url")
                val url: String? = "",
                @SerialName("width")
                val width: Int? = 0,
                @SerialName("height")
                val height: Int? = 0,
                @SerialName("size")
                val size: Int? = 0,
                @SerialName("thumbnail")
                val thumbnail: String? = ""
            )

            @Serializable
            data class Small(
                @SerialName("url")
                val url: String? = "",
                @SerialName("width")
                val width: Int? = 0,
                @SerialName("height")
                val height: Int? = 0,
                @SerialName("size")
                val size: Int? = 0,
                @SerialName("thumbnail")
                val thumbnail: String? = ""
            )

            @Serializable
            data class Tiny(
                @SerialName("url")
                val url: String? = "",
                @SerialName("width")
                val width: Int? = 0,
                @SerialName("height")
                val height: Int? = 0,
                @SerialName("size")
                val size: Int? = 0,
                @SerialName("thumbnail")
                val thumbnail: String? = ""
            )
        }
    }
}