package com.domain.mapper

import com.data.model.response.FilmsDTO
import com.domain.model.Films

class FilmsMapper {

    fun map(dto: FilmsDTO): Films {
        return Films(
            count = dto.count ?: 0,
            next = dto.next,
            previous = dto.previous,
            results = dto.results?.map { map(it) } ?: emptyList()
        )
    }

    private fun map(resultDTO: FilmsDTO.Result?): Films.Result {
        return Films.Result(
            title = resultDTO?.title.orEmpty(),
            episodeId = resultDTO?.episodeId ?: 0,
            openingCrawl = resultDTO?.openingCrawl.orEmpty(),
            director = resultDTO?.director.orEmpty(),
            producer = resultDTO?.producer.orEmpty(),
            releaseDate = resultDTO?.releaseDate.orEmpty(),
            characters = resultDTO?.characters?.filterNotNull() ?: emptyList(),
            planets = resultDTO?.planets?.filterNotNull() ?: emptyList(),
            starships = resultDTO?.starships?.filterNotNull() ?: emptyList(),
            vehicles = resultDTO?.vehicles?.filterNotNull() ?: emptyList(),
            species = resultDTO?.species?.filterNotNull() ?: emptyList(),
            created = resultDTO?.created.orEmpty(),
            edited = resultDTO?.edited.orEmpty(),
            url = resultDTO?.url.orEmpty()
        )
    }
}