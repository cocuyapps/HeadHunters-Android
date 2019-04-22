package pe.com.headhunters.adapters

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.widget.ANImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.content_album.view.*
import pe.com.headhunters.R
import pe.com.headhunters.activities.AlbumActivity
import pe.com.headhunters.models.Album

class AlbumsAdapter(private var albums: List<Album>) :
    RecyclerView.Adapter<AlbumsAdapter.ViewHolder>() {

    class ViewHolder(albumView: View) : RecyclerView.ViewHolder(albumView) {

        private lateinit var dbReference: DatabaseReference
        private var albumUploadId: String? = null
        private var auth: FirebaseAuth

        var titleTextView: TextView
        var artistTextView: TextView
        var image: ANImageView
        var thumbnail_image: ANImageView
        var addToPlayList: AppCompatButton
        var contentAlbum: ConstraintLayout
        init {
            titleTextView = albumView.title
            artistTextView = albumView.artist
            image = albumView.image
            thumbnail_image = albumView.thumbnail_image
            addToPlayList = albumView.addToPlayList
            contentAlbum = albumView.contentAlbum
            auth = FirebaseAuth.getInstance()
        }


        fun bindTo(album: Album) {
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

            addToPlayList.setOnClickListener {
                //assign FirebaseDatabase instance with root database name
                dbReference = FirebaseDatabase.getInstance().getReference("/User/${auth.uid}/albums")
                //getting AlbumId
                albumUploadId = dbReference.push().key
                //adding album upload id's child element into databaseReference
                albumUploadId?.let { dbReference.child(it).setValue(album) }

                Toast.makeText(it.context,"Album added!", Toast.LENGTH_LONG).show()
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