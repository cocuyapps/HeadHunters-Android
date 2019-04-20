package pe.com.headhunters.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import pe.com.headhunters.R
import pe.com.headhunters.adapters.AlbumsAdapter
import pe.com.headhunters.models.AlbumClass
import pe.com.headhunters.network.AlbumsApi
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.json.JSONArray
import org.json.JSONObject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : Fragment() {
    val TAG = "Albums"
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
        val albums = ArrayList<AlbumClass>()


        AndroidNetworking.get(AlbumsApi.albumsUrl())
            .build()

            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    albumsRecyclerView = view.albumsRecyclerView  //Recycler view delegates

                    response?.apply {
                        //if this has a value
                        val list = JSONArray(response.getString("albums"))


                        for (i in 0 until list.length()) {

                            var resultsObject = list.getJSONObject(i)
                            var temp = AlbumClass()
                            temp.title = resultsObject.getString("title")
                            temp.artist = resultsObject.getString("artist")
                            temp.image = resultsObject.getString("image")
                            temp.thumbnail_image = resultsObject.getString("image")
                            albums.add(temp)
                        }

                        albumsRecyclerView.apply {
                            layoutManager = LinearLayoutManager(view.context)
                            adapter = AlbumsAdapter(albums)
                        }
                    }
                }

                override fun onError(error: ANError) {
                    error?.apply {
                        Log.d("error", message)
                    }
                }
            })
    }
}

