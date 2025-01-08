package com.data.remote.api

object ApiEndpoints {
//    private const val BASE_URL = "https://swapi.dev/"
    private const val BASE_URL = "https://swapi.py4e.com/"
    private const val BASE_URL_VIDEOS = "https://pixabay.com/"
    private const val API = "api/"
    const val FILMS = BASE_URL + API + "films"
    const val PEOPLE = BASE_URL + API + "people/1"
    const val VIDEOS = BASE_URL_VIDEOS + API + "videos"
}

object ApiParameters {
    const val FORMAT = "format"
    const val KEY = "key"
    const val PAGE = "page"
    const val PER_PAGE = "per_page"
}

object ApiParametersValue {
    const val JSON = "json"
    const val PER_PAGE = "10"
}
