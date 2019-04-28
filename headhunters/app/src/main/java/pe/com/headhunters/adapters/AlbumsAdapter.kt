package pe.com.headhunters.adapters

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.interfaces.OkHttpResponseListener
import com.androidnetworking.widget.ANImageView
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.content_album.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import okhttp3.Response
import org.json.JSONArray
import pe.com.headhunters.R
import pe.com.headhunters.activities.AlbumActivity
import pe.com.headhunters.models.Album
import org.json.JSONException
import org.json.JSONObject
import pe.com.headhunters.models.Song
import pe.com.headhunters.network.AlbumsApi


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
        var addToPlayList: TextView
        var heartColor: ImageView
        var contentAlbum: MaterialCardView
        init {
            titleTextView = albumView.title
            artistTextView = albumView.artist
            image = albumView.image
            thumbnail_image = albumView.thumbnail_image
            addToPlayList = albumView.addToPlayList
            heartColor = albumView.imgHeart
            contentAlbum = albumView.contentAlbum
            auth = FirebaseAuth.getInstance()
        }

        fun bindTo(album: Album) {
            titleTextView.text = album.title
            artistTextView.text = album.artist
            image.setImageUrl(album.image)
            thumbnail_image.setImageUrl(album.thumbnail_image)

            //for firebase
            lateinit var albumKey: String
            var serialized: Album

            //send Query to FirebaseDatabase
            dbReference = FirebaseDatabase.getInstance().getReference("/User/${auth.uid}/albums")

            //get all albums from this particular user
            dbReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (child: DataSnapshot in snapshot.children) {
                        serialized = child.getValue(Album::class.java)!!
                        if(serialized.title == album.title) {
                            albumKey = child.key!!
                            addToPlayList.text = "Album added"

                            if(serialized.liked == 1) {
                                heartColor.setTag("heartred")
                                heartColor.setImageResource(R.drawable.heartred)
                            } else {
                                heartColor.setTag("heartblank")
                                heartColor.setImageResource(R.drawable.heartblank)
                            }
                            album.liked = serialized.liked
                        }
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    Log.e("Firebase:", p0.toString())
                }
            })

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
                if (addToPlayList.text != "Album added") {
                    //assign FirebaseDatabase instance with root database name
                    dbReference = FirebaseDatabase.getInstance().getReference("/User/${auth.uid}/albums")
                    //getting AlbumId
                    albumUploadId = dbReference.push().key
                    //adding album upload id's child element into databaseReference
                    albumUploadId?.let { dbReference.child(it).setValue(album) }

                    addToPlayList.text = "Album added"

                    Toast.makeText(it.context,"Album added!", Toast.LENGTH_LONG).show()
                }
            }

            heartColor.setOnClickListener {
                if (addToPlayList.text == "Album added") {
                    if (heartColor.getTag() == "heartblank") {
                        album.likes += 1
                        val jsonObject = JSONObject()
                        try {
                            jsonObject.put("likes", album.likes)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                        AndroidNetworking.put(AlbumsApi.albumsUrl()+"/"+album._id)
                            .addJSONObjectBody(jsonObject)
                            .build()
                            .getAsOkHttpResponse(object: OkHttpResponseListener {
                                override fun onResponse(response: Response?) {
                                    response?.apply {
                                        Log.i("RESPONSE", response.toString())
                                    }
                                }
                                override fun onError(anError: ANError?) {
                                    anError?.apply {
                                        Log.d("error", message)
                                    }
                                }
                            })

                        //assign FirebaseDatabase instance with root database name
                        dbReference = FirebaseDatabase.getInstance().getReference("/User/${auth.uid}/albums")
                        //assign liked to this album
                        album.liked = 1
                        dbReference.child(albumKey).setValue(album)

                        heartColor.setTag("heartred")
                        heartColor.setImageResource(R.drawable.heartred)

                        Toast.makeText(it.context,"Nice!", Toast.LENGTH_SHORT).show()

                    } else {
                        album.likes -= 1
                        val jsonObject = JSONObject()
                        try {
                            jsonObject.put("likes", album.likes)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                        AndroidNetworking.put(AlbumsApi.albumsUrl()+"/"+album._id)
                            .addJSONObjectBody(jsonObject)
                            .build()
                            .getAsOkHttpResponse(object: OkHttpResponseListener {
                                override fun onResponse(response: Response?) {
                                    response?.apply {
                                        Log.i("RESPONSE", response.toString())
                                    }
                                }
                                override fun onError(anError: ANError?) {
                                    anError?.apply {
                                        Log.d("error", message)
                                    }
                                }
                            });

                        //assign FirebaseDatabase instance with root database name
                        dbReference = FirebaseDatabase.getInstance().getReference("/User/${auth.uid}/albums")
                        //getting AlbumId
                        album.liked = 0
                        dbReference.child(albumKey).setValue(album)

                        heartColor.setTag("heartblank")
                        heartColor.setImageResource(R.drawable.heartblank)

                        Toast.makeText(it.context,"Pitty", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(it.context,"You need to add the album first!", Toast.LENGTH_SHORT).show()
                }
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