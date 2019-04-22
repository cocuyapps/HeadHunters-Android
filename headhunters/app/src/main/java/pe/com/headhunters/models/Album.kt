package pe.com.headhunters.models

import android.os.Parcel
import android.os.Parcelable

data class Album(
    var _id: String,
    var title: String,
    var artist: String,
    var url: String,
    var thumbnail_image: String,
    var image: String,
    var songs: ArrayList<Song>,
    var genre: String,
    var likes: Int,
    var description: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readArrayList(Song::class.java.classLoader) as ArrayList<Song>,
        parcel.readString(),
        parcel.readInt(),
        parcel.readString()
    )

    constructor() : this (
        "",
        "",
        "",
        "",
        "",
        "",
        ArrayList<Song>(),
        "",
        0,
        "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(title)
        parcel.writeString(artist)
        parcel.writeString(url)
        parcel.writeString(thumbnail_image)
        parcel.writeString(image)
        parcel.writeList(songs)
        parcel.writeString(genre)
        parcel.writeInt(likes)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Album> {
        override fun createFromParcel(parcel: Parcel): Album {
            return Album(parcel)
        }

        override fun newArray(size: Int): Array<Album?> {
            return arrayOfNulls(size)
        }
    }
}