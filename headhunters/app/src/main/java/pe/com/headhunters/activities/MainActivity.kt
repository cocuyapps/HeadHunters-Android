package pe.com.headhunters.activities

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.json.JSONObject
import pe.com.headhunters.R
import pe.com.headhunters.fragments.*
import pe.com.headhunters.models.Album


class MainActivity : AppCompatActivity() {

    var message: Boolean = false
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        navigateTo(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigateTo(navigation.menu.findItem(R.id.navigation_home))
    }

    private fun getFragmentFor(item: MenuItem) : Fragment {
        when (item.itemId) {
            R.id.navigation_home -> {
                message = false
                return HomeFragment()
            }
            R.id.navigation_search -> {
                message = false
                return SearchFragment()
            }
            R.id.navigation_playlist -> {
                message = false
                return PlayListFragment()
            }
            R.id.navigation_profile -> {
                message = false
                return ProfileFragment()
            }
            R.id.navigation_artist -> {
                message = false
                return ArtistFragment()
            }
        }
        return HomeFragment()
    }

    private fun navigateTo(item: MenuItem) : Boolean {
        item.isChecked = true
        return supportFragmentManager
            .beginTransaction()
            .replace(R.id.content, getFragmentFor(item))
            .commit() > 0
    }

    fun switchContent(fragment: Fragment) : Boolean {
        return supportFragmentManager
            .beginTransaction()
            .replace(R.id.content, fragment)
            .commit() > 0
    }

    override fun onBackPressed() {
        if(message) {
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(1)
        } else {
            Toast.makeText(this,"If you press the back button again you will exit the app", Toast.LENGTH_SHORT).show()
            message = true
        }

    }
}
