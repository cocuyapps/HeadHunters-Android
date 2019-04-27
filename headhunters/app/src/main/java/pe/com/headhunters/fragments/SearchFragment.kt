package pe.com.headhunters.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_play_list.view.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import org.json.JSONArray
import org.json.JSONObject

import pe.com.headhunters.R
import pe.com.headhunters.adapters.AlbumPlayListAdapter
import pe.com.headhunters.adapters.GenresAdapter
import pe.com.headhunters.models.Album
import pe.com.headhunters.network.AlbumsApi

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class SearchFragment : Fragment() {

    private lateinit var progressBar: ProgressBar
    lateinit var genresRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.setTitle("Genres")
        progressBar = getView()!!.findViewById<ProgressBar>(R.id.genresProgressBar)
        progressBar.visibility = View.VISIBLE
        super.onViewCreated(view, savedInstanceState)
        requestGenres(view)
    }

    private fun requestGenres(view: View) {
        val titleGenres = ArrayList<String>()
        val imgGenres = ArrayList<Int>()

        AndroidNetworking.get(AlbumsApi.albumsUrl()+"/genres")
            .build()
            .getAsJSONArray(object: JSONArrayRequestListener {

                override fun onResponse(response: JSONArray?) {
                    genresRecyclerView = view.genresRecyclerView
                    response?.apply {

                        for(i in 0 until response.length()) {
                            titleGenres.add(response.get(i).toString())
                        }

                        genresRecyclerView.apply {
                            layoutManager = GridLayoutManager(context, 2)
                            adapter = GenresAdapter(titleGenres, context)
                            progressBar.visibility = View.GONE
                        }
                    }
                }

                override fun onError(anError: ANError?) {
                    anError?.apply {
                        Log.d("Error", message)
                    }
                }

            })
    }
}