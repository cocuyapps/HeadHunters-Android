package pe.com.headhunters.activities

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_album.*
import kotlinx.android.synthetic.main.content_albums.*
import pe.com.headhunters.R

class AlbumActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        intent?.extras?.apply { // is it starts from an invoke, then do this

            setTitle(getString("title"))
            titleTextView.text = "Title: " + getString("title")
            artistTextView.text = "Artist:" + getString("artist")
            single_image.setImageUrl(getString("image"))
            descriptionTextView.text = "Description: " + getString("description")

        }
    }

}
