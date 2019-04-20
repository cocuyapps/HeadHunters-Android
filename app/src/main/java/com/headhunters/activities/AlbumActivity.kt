package com.headhunters.activities

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.net.toUri
import com.headhunters.R

import kotlinx.android.synthetic.main.activity_album.*
import kotlinx.android.synthetic.main.content_albums.*

class AlbumActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        intent?.extras?.apply { // is it starts from an invoke, then do this
            titleTextView.text = getString("title")
            artistTextView.text = getString("artist")
            image.setImageUrl(getString("image"))
        }
    }

}
