package pe.com.headhunters.models

data class AlbumClass(
    var _id: String,
    var title: String,
    var artist: String,
    var url: String,
    var thumbnail_image: String,
    var image: String,
    var songs: List<String>,
    var genre: String,
    var likes: Number,
    var description: String
) {
    constructor() : this (
        "",
        "",
        "",
        "",
        "",
        "",
        ArrayList<String>(),
        "",
        0,
        "")
}