package pe.com.headhunters.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_album.*
import kotlinx.android.synthetic.main.activity_artist.*
import kotlinx.android.synthetic.main.content_album.*
import kotlinx.android.synthetic.main.content_albums.*
import kotlinx.android.synthetic.main.content_artist.*
import pe.com.headhunters.R

class ArtistActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist)

        intent?.extras?.apply { // is it starts from an invoke, then do this

            setTitle("Artists")
            artistTitle.text = "Band name: " + getString("bandName")
            artistMembers.text = "Band members:" + getString("bandMembers")
            artistImage.setImageUrl(getString("bandImg"))
            artistDescription.text = "Description: " + getString("bandDescription")
            musicSource.text = "Music source: "
            artistSample.text = getString("bandSample")
        }
    }
}
