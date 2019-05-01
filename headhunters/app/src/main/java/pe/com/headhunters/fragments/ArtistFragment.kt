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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.content_album_playlist.*
import kotlinx.android.synthetic.main.content_artists.*
import kotlinx.android.synthetic.main.fragment_artist.view.*
import kotlinx.android.synthetic.main.fragment_play_list.view.*

import pe.com.headhunters.R
import pe.com.headhunters.adapters.AlbumPlayListAdapter
import pe.com.headhunters.adapters.ArtistAdapter
import pe.com.headhunters.models.Album
import pe.com.headhunters.models.UserProfile

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ArtistFragment : Fragment() {

    private lateinit var progressBar: ProgressBar
    lateinit var artistRecyclerView: RecyclerView //promise don't initialize now

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.setTitle("Artists")
        progressBar = getView()!!.findViewById<ProgressBar>(R.id.artistProgressBar)
        progressBar.visibility = View.VISIBLE
        super.onViewCreated(view, savedInstanceState)
        requestAlbums(view)
    }

    private fun requestAlbums(view: View) {
        val users = ArrayList<UserProfile>()
        artistRecyclerView = view.artistRecyclerView

        lateinit var dbReference: DatabaseReference

        //send Query to FirebaseDatabase
        dbReference = FirebaseDatabase.getInstance().getReference("/User")

        //get all albums from this particular user
        dbReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (child: DataSnapshot in snapshot.children) {
                    if (child.getValue(UserProfile::class.java)!!.BandName != "")
                        users.add(child.getValue(UserProfile::class.java)!!)
                }

                artistRecyclerView.apply {
                    layoutManager = GridLayoutManager(context, 2)
                    adapter = ArtistAdapter(users)
                    progressBar.visibility = View.GONE
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.e("Firebase:", p0.toString())
            }
        })
    }
}
