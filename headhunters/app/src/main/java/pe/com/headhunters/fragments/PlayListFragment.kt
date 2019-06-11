package pe.com.headhunters.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

import kotlinx.android.synthetic.main.fragment_play_list.view.*

import pe.com.headhunters.R
import pe.com.headhunters.adapters.AlbumPlayListAdapter
import pe.com.headhunters.models.Album
import java.lang.Exception

class PlayListFragment : Fragment() {

    private lateinit var progressBar: ProgressBar
    lateinit var albumsRecyclerView: RecyclerView //promise don't initialize now
    private lateinit var nomusicTxt: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_play_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.setTitle("Playlist")
        progressBar = getView()!!.findViewById<ProgressBar>(R.id.playlistProgressBar)
        progressBar.visibility = View.VISIBLE
        nomusicTxt = getView()!!.findViewById<TextView>(R.id.noMusicText)
        super.onViewCreated(view, savedInstanceState)
        requestAlbums(view)
    }

    private fun requestAlbums(view: View) {
        val albums = ArrayList<Album>()
        albumsRecyclerView = view.albumsGridRecyclerView

        lateinit var dbReference: DatabaseReference
        lateinit var dbReference2: DatabaseReference
        var auth: FirebaseAuth = FirebaseAuth.getInstance()

        //send Query to FirebaseDatabase
        dbReference = FirebaseDatabase.getInstance().getReference("/User/${auth.uid}/albums")
        //get all albums from this particular user
        try {
            dbReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (child: DataSnapshot in snapshot.children) {
                        albums.add(child.getValue(Album::class.java)!!)
                    }
                    if (albums.size == 0) {
                        nomusicTxt.visibility = View.VISIBLE
                    }
                    albumsRecyclerView.apply {
                        layoutManager = GridLayoutManager(context, 2) as RecyclerView.LayoutManager?
                        adapter = AlbumPlayListAdapter(albums)
                        progressBar.visibility = View.GONE
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    Log.e("Firebase:", p0.toString())
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
