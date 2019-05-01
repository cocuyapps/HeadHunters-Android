package pe.com.headhunters.activities

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity;
import pe.com.headhunters.R

import kotlinx.android.synthetic.main.activity_album_play_list.*
import pe.com.headhunters.adapters.AlbumPlayListAdapter
import pe.com.headhunters.models.Album
import pe.com.headhunters.models.Song
import java.io.IOException
import java.lang.Error
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timerTask


class AlbumPlayList : AppCompatActivity() {

    private var player: MediaPlayer? = null
    private var musicPosition = 0
    private var currentPosition = 0
    private lateinit var btnPlayPause: ImageView
    private lateinit var toolbar_title: TextView
    private var audioManager: AudioManager? = null
    private lateinit var volumeSeekBar: SeekBar
    private lateinit var scrubberSeekBar: SeekBar
    private lateinit var txtStart: TextView
    private lateinit var txtEnd: TextView
    private var songs = ArrayList<Song>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_play_list)

        setSupportActionBar(toolbarPlayList)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(null)

        //get information of the volume of our device
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager?
        var maxVolume = audioManager?.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        var currentVolume = audioManager?.getStreamVolume(AudioManager.STREAM_MUSIC)

        volumeSeekBar = findViewById(R.id.volumeSeekBar)
        scrubberSeekBar = findViewById(R.id.scrubberSeekBar)
        txtStart = findViewById(R.id.txtStart)
        txtEnd = findViewById(R.id.txtEnd)
        var handler = Handler()

        volumeSeekBar.max = maxVolume!!
        volumeSeekBar.progress = currentVolume!!

        volumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                audioManager?.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
            }

        })

        scrubberSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                player?.seekTo(progress)
                var minutes = progress / 60000
                var seconds = (progress - minutes * 60) / 1000
                seconds %= 60

                var secondStr = Integer.toString(seconds)
                if (secondStr == "0")
                    secondStr = "00"
                if (secondStr.length == 1)
                    secondStr = "0"+secondStr

                txtStart.text = Integer.toString(minutes) + ":" + secondStr
            }

        })

        intent?.let {
            val album = intent.extras.getParcelable(AlbumPlayListAdapter.ViewHolder.ALBUM) as Album
            btnPlayPause = this.findViewById(R.id.btnPlayPause)
            toolbar_title = findViewById(R.id.toolbar_title)
            toolbar_title.isSelected = true
            toolbar_title.text = "Album: " + album.title
            songs = album.songs
            Log.i("SONGS",songs.toString())
            displaySong(songs[currentPosition].title, songs[currentPosition].artist, songs[currentPosition].albumArtUrl)

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //Look for ways to play in background mode and exit if necessary
        finish()
    }

    fun getNextSong(currentSongId: Int): Song? {
        var song: Song? = null
        for (i in songs.indices) {
            if (i == currentSongId && i < songs.size - 1) {
                song = songs[i + 1]
                currentPosition++
                break
            }
        }
        return song
    }

    fun getPreviousSong(currentSongId: Int): Song? {
        var song: Song? = null
        for (i in songs.indices) {
            if (i == currentSongId && i > 0) {
                song = songs[i - 1]
                currentPosition--
                break
            }
        }
        return song
    }

    fun playNext(view: View) {
        val nextSong = getNextSong(currentPosition);
        if (nextSong != null) {
            stopActivities()
            displaySong(nextSong.title, nextSong.artist, nextSong.albumArtUrl)
            playOrPauseMusic(view)
        }
    }

    fun playPrevious(view: View) {
        val previousSong = getPreviousSong(currentPosition)
        if (previousSong != null) {
            stopActivities()
            displaySong(previousSong.title, previousSong.artist, previousSong.albumArtUrl)
            playOrPauseMusic(view)
        }
    }

    fun playOrPauseMusic(view: View) {
        if (player == null) {
            preparePlayer()
        }
        if (player?.isPlaying() == false) {
            if (musicPosition > 0) {
                player?.seekTo(musicPosition)
            }
            player?.start()
            val imageId = getImageIdFromDrawable("pause")
            btnPlayPause.setImageResource(imageId)
            btnPlayPause.alpha = 0.5f
            toolbar_title.text = "Now playing: ${songs[currentPosition].title} - ${songs[currentPosition].artist}"
            gracefullyStopWhenMusicEnds()
        } else {
            pauseMusic()
            btnPlayPause.alpha = 1f
        }
    }

    private fun preparePlayer() {
        player = MediaPlayer()

        try {
            player?.setAudioStreamType(AudioManager.STREAM_MUSIC)
            player?.setDataSource(songs[currentPosition].audioUrl)
            player?.prepare()

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun gracefullyStopWhenMusicEnds() {
        player?.setOnCompletionListener(MediaPlayer.OnCompletionListener {
            if (player != null) {
                val imageId = getImageIdFromDrawable("playarrow")
                btnPlayPause.setImageResource(imageId)
                musicPosition = 0
                toolbar_title.text = ""
                player?.stop()
                player?.release()
            }
        })
    }

    private fun displaySong(title: String, artist: String, albumArtUrl: String) {
        txtTitle.setText(title)
        txtArttist.setText(artist)
        coverArt.setImageUrl(albumArtUrl)
    }

    private fun pauseMusic() {
        player?.pause()
        musicPosition = player!!.currentPosition
        val imageId = getImageIdFromDrawable("playarrow")
        btnPlayPause.setImageResource(imageId)
    }

    fun stopActivities() {
        if (player != null) {
            val imageId = getImageIdFromDrawable("playarrow")
            btnPlayPause.setImageResource(imageId)
            musicPosition = 0
            toolbar_title.text = ""
            player?.stop()
            player?.release()
            player = null
            preparePlayer()
        }
    }

    fun getImageIdFromDrawable(imageName: String): Int {
        return resources.getIdentifier(imageName, "drawable", packageName)
    }
}
