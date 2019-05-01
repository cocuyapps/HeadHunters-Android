package pe.com.headhunters.fragments


import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.androidnetworking.widget.ANImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import pe.com.headhunters.R
import android.R.attr.button
import android.text.Editable
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.content_album_playlist.*
import kotlinx.android.synthetic.main.fragment_profile.*
import pe.com.headhunters.adapters.AlbumPlayListAdapter
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
class ProfileFragment : Fragment() {

    private lateinit var txtBandName: EditText
    private lateinit var txtBandMembers: EditText
    private lateinit var txtBandDescription: EditText
    private lateinit var txtBandImgUrl: EditText
    private lateinit var txtBandSample: EditText
    private lateinit var saveProfileBtn: Button
    private lateinit var dbReference: DatabaseReference
    private lateinit var dbUserData: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtBandName = view!!.findViewById(R.id.txtBandName)
        txtBandMembers = view!!.findViewById(R.id.txtBandMembers)
        txtBandDescription = view!!.findViewById(R.id.txtBandDescription)
        txtBandImgUrl = view!!.findViewById(R.id.txtBandImgUrl)
        txtBandSample = view!!.findViewById(R.id.txtBandSample)
        saveProfileBtn = view!!.findViewById(R.id.saveProfileBtn)
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        //Reference to read and write in a location
        dbReference = database.reference.child("User")

        dbUserData = database.reference
        var firebaseUser = auth.currentUser
        var userUID = firebaseUser!!.uid

        dbUserData.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds: DataSnapshot in snapshot.children) {
                    var user = ds.child(userUID).getValue(UserProfile::class.java)!!
                    txtBandName.setText(user.BandName)
                    txtBandMembers.setText(user.BandMembers)
                    txtBandDescription.setText(user.BandDescription)
                    txtBandImgUrl.setText(user.BandImgUrl)
                    txtBandSample.setText(user.BandSample)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.e("Firebase:", p0.toString())
            }
        })



        saveProfileBtn.setOnClickListener {
            val bandName: String = txtBandName.text.toString()
            val bandMembers: String  = txtBandMembers.text.toString()
            val bandDescription: String = txtBandDescription.text.toString()
            val bandImgUrl: String = txtBandImgUrl.text.toString()
            val bandSample: String = txtBandSample.text.toString()

            if(!TextUtils.isEmpty(bandName) && !TextUtils.isEmpty(bandMembers) && !TextUtils.isEmpty(bandDescription)){
                val user: FirebaseUser?=auth.currentUser
                val userBD = dbReference.child(user?.uid.toString())
                userBD.child("BandName").setValue(bandName)
                userBD.child("BandMembers").setValue(bandMembers)
                userBD.child("BandDescription").setValue(bandDescription)
                userBD.child("BandImgUrl").setValue(bandImgUrl)
                userBD.child("BandSample").setValue(bandSample)

                Toast.makeText(context,"You profile has been saved!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
