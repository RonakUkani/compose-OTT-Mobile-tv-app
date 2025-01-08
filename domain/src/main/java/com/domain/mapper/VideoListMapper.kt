package com.domain.mapper

import com.data.model.response.VideosListDTO
import com.domain.model.VideosList

class VideosListMapper {
    fun map(dto: VideosListDTO): VideosList {
        return VideosList(
            total = dto.total ?: 0,
            totalHits = dto.totalHits ?: 0,
            hits = dto.hits.map { it.toHit() }.toMutableList()
        )
    }

    private fun VideosListDTO.Hit.toHit(): VideosList.Hit {
        return VideosList.Hit(
            id = this.id ?: 0,
            pageURL = this.pageURL ?: "",
            type = this.type ?: "",
            tags = this.tags ?: "",
            duration = this.duration ?: 0,
            videos = this.videos?.toVideos(),
            views = this.views ?: 0,
            downloads = this.downloads ?: 0,
            likes = this.likes ?: 0,
            comments = this.comments ?: 0,
            userId = this.userId ?: 0,
            user = this.user ?: "",
            userImageURL = this.userImageURL ?: ""
        )
    }

    private fun VideosListDTO.Hit.Videos.toVideos(): VideosList.Hit.Videos {
        return VideosList.Hit.Videos(
            large = this.large?.toLarge(),
            medium = this.medium?.toMedium(),
            small = this.small?.toSmall(),
            tiny = this.tiny?.toTiny()
        )
    }

    private fun VideosListDTO.Hit.Videos.Large.toLarge(): VideosList.Hit.Videos.Large {
        return VideosList.Hit.Videos.Large(
            url = this.url ?: "",
            width = this.width ?: 0,
            height = this.height ?: 0,
            size = this.size ?: 0,
            thumbnail = this.thumbnail ?: ""
        )
    }

    private fun VideosListDTO.Hit.Videos.Medium.toMedium(): VideosList.Hit.Videos.Medium {
        return VideosList.Hit.Videos.Medium(
            url = this.url ?: "",
            width = this.width ?: 0,
            height = this.height ?: 0,
            size = this.size ?: 0,
            thumbnail = this.thumbnail ?: ""
        )
    }

    private fun VideosListDTO.Hit.Videos.Small.toSmall(): VideosList.Hit.Videos.Small {
        return VideosList.Hit.Videos.Small(
            url = this.url ?: "",
            width = this.width ?: 0,
            height = this.height ?: 0,
            size = this.size ?: 0,
            thumbnail = this.thumbnail ?: ""
        )
    }

    private fun VideosListDTO.Hit.Videos.Tiny.toTiny(): VideosList.Hit.Videos.Tiny {
        return VideosList.Hit.Videos.Tiny(
            url = this.url ?: "",
            width = this.width ?: 0,
            height = this.height ?: 0,
            size = this.size ?: 0,
            thumbnail = this.thumbnail ?: ""
        )
    }
}
