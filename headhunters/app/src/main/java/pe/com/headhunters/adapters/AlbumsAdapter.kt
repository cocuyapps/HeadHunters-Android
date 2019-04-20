package pe.com.headhunters.adapters

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.widget.ANImageView
import kotlinx.android.synthetic.main.content_album.view.*
import pe.com.headhunters.R
import pe.com.headhunters.activities.AlbumActivity
import pe.com.headhunters.models.AlbumClass

class AlbumsAdapter(private var albums: List<AlbumClass>) :
    RecyclerView.Adapter<AlbumsAdapter.ViewHolder>() {
    class ViewHolder(albumView: View) : RecyclerView.ViewHolder(albumView) {
        var titleTextView: TextView
        var artistTextView: TextView
        var image: ANImageView
        var thumbnail_image: ANImageView
        var contentAlbum: ConstraintLayout
        init {
            titleTextView = albumView.title
            artistTextView = albumView.artist
            image = albumView.image
            thumbnail_image = albumView.thumbnail_image
            contentAlbum = albumView.contentAlbum
        }
        fun bindTo(album: AlbumClass) {
            titleTextView.text = album.title
            artistTextView.text = album.artist
            image.setImageUrl(album.image)
            thumbnail_image.setImageUrl(album.thumbnail_image)

            contentAlbum.setOnClickListener {
                val bundle = Bundle()
                bundle.apply {
                    putString("title", album.title)
                    putString("artist", album.artist)
                    putString("image", album.image)
                    putString("thumbnail_image", album.thumbnail_image)
                    putString("description", album.description)
                }
                var intent = Intent(it.context, AlbumActivity::class.java)
                intent.putExtras(bundle) //Intent initializes an activity. context is the current activity
                it.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.content_album, parent, false))
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(albums[position])
    }
}