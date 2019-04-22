package pe.com.headhunters.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import pe.com.headhunters.R
import pe.com.headhunters.adapters.AlbumsAdapter
import pe.com.headhunters.models.Album
import pe.com.headhunters.network.AlbumsApi
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.json.JSONArray
import org.json.JSONObject
import pe.com.headhunters.models.Song


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : Fragment() {

    lateinit var albumsRecyclerView: RecyclerView //promise don't initialize now
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState) //refinement (invokes father super method)
        requestAlbums(view)
    }

    private fun requestAlbums(view: View) {
        val albums = ArrayList<Album>()

        AndroidNetworking.get(AlbumsApi.albumsUrl())
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    albumsRecyclerView = view.albumsRecyclerView  //Recycler view delegates

                    response?.apply {

                        val Albumlist = JSONArray(response.getString("albums"))

                        for (i in 0 until Albumlist.length()) {
                            var albumResultObject = Albumlist.getJSONObject(i)
                            var album = Album()
                            album.title = albumResultObject.getString("title")
                            album.artist = albumResultObject.getString("artist")
                            album.image = albumResultObject.getString("image")
                            album.thumbnail_image = albumResultObject.getString("thumbnail_image")
                            album.description = albumResultObject.getString("description")

                            var SongList = JSONArray(albumResultObject.getString("songs"))

                            for (j in 0 until SongList.length()) {
                                var songResultObject = SongList.getJSONObject(j)
                                var song = Song()
                                song.title = songResultObject.getString("title")
                                song.artist = songResultObject.getString("artist")
                                song.albumArtUrl = songResultObject.getString("albumArtUrl")
                                song.audioUrl = songResultObject.getString("audioUrl")
                                album.songs.add(song)
                            }

                            albums.add(album)
                        }
                        albumsRecyclerView.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = AlbumsAdapter(albums)
                        }
                    }
                }

                override fun onError(anError: ANError?) {
                    anError?.apply {
                        Log.d("error", message)
                    }
                }
            })
    }
}

