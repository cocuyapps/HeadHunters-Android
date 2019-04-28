package pe.com.headhunters.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.widget.ANImageView
import kotlinx.android.synthetic.main.content_album_playlist.view.*
import pe.com.headhunters.R
import pe.com.headhunters.activities.AlbumPlayList
import pe.com.headhunters.models.Album

class AlbumPlayListAdapter(private var albums : List<Album>) :
    RecyclerView.Adapter<AlbumPlayListAdapter.ViewHolder>() {


    class ViewHolder(albumView: View) : RecyclerView.ViewHolder(albumView) {

        companion object {
            const val ALBUM = "album"
        }

        var albumTitle: TextView
        var albumImage: ANImageView
        var contentPlayList: ConstraintLayout
        init {
            albumTitle = albumView.PlayListtTitle
            albumImage = albumView.PlayListimage
            contentPlayList = albumView.contentPlayList
        }
        fun bindTo(album: Album) {
            albumTitle.text = album.title
            albumImage.setImageUrl(album.image)

            contentPlayList.setOnClickListener {
                var intent = Intent(it.context, AlbumPlayList::class.java)
                intent.putExtra(ALBUM, album) //Intent initializes an activity. context is the current activity
                it.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.content_album_playlist, parent, false))
    }

    override fun getItemCount(): Int {
        return albums.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(albums[position])
    }
}