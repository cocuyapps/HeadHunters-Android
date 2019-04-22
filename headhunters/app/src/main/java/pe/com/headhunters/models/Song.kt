package pe.com.headhunters.models


import android.os.Parcel
import android.os.Parcelable

data class Song(
    var artist: String,
    var title: String,
    var albumArtUrl: String,
    var audioUrl: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    constructor() : this(
        "",
        "",
        "",
        ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(artist)
        parcel.writeString(title)
        parcel.writeString(albumArtUrl)
        parcel.writeString(audioUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Song> {
        override fun createFromParcel(parcel: Parcel): Song {
            return Song(parcel)
        }

        override fun newArray(size: Int): Array<Song?> {
            return arrayOfNulls(size)
        }
    }
}

