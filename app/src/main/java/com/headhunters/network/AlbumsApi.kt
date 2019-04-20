package com.headhunters.network

class AlbumsApi {
    companion object {
        val BASE_URL = "https://headhuntersapp-api.herokuapp.com"
        fun albumsUrl(): String {
            return "${BASE_URL}/albums"
        }
    }
}