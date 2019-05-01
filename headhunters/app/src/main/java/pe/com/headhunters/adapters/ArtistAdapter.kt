package pe.com.headhunters.adapters

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.widget.ANImageView
import kotlinx.android.synthetic.main.content_album_playlist.view.*
import kotlinx.android.synthetic.main.content_artists.view.*
import pe.com.headhunters.R
import pe.com.headhunters.activities.AlbumActivity
import pe.com.headhunters.activities.AlbumPlayList
import pe.com.headhunters.activities.ArtistActivity
import pe.com.headhunters.models.Album
import pe.com.headhunters.models.UserProfile

class ArtistAdapter(private var artists : List<UserProfile>) :
    RecyclerView.Adapter<ArtistAdapter.ViewHolder>() {


    class ViewHolder(artistView: View) : RecyclerView.ViewHolder(artistView) {

        var bandTitle: TextView
        var artistImage: ANImageView
        var contentArtist: ConstraintLayout
        init {
            bandTitle = artistView.bandTitle
            artistImage = artistView.artistImage
            contentArtist = artistView.contentArtist
        }
        fun bindTo(artist: UserProfile) {
            bandTitle.text = artist.BandName
            artistImage.setImageUrl(artist.BandImgUrl)

            contentArtist.setOnClickListener {
                val bundle = Bundle()
                bundle.apply {
                    putString("bandName", artist.BandName)
                    putString("bandMembers", artist.BandMembers)
                    putString("bandDescription", artist.BandDescription)
                    putString("bandImg", artist.BandImgUrl)
                    putString("bandSample", artist.BandSample)
                }
                var intent = Intent(it.context, ArtistActivity::class.java)
                intent.putExtras(bundle) //Intent initializes an activity. context is the current activity
                it.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.content_artists, parent, false))
    }

    override fun getItemCount(): Int {
        return artists.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(artists[position])
    }
}