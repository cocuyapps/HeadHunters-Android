package pe.com.headhunters.adapters

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.content_genres.view.*
import pe.com.headhunters.R
import pe.com.headhunters.fragments.HomeFragment
import androidx.fragment.app.FragmentActivity
import com.androidnetworking.widget.ANImageView
import pe.com.headhunters.activities.MainActivity

class GenresAdapter(private var genres: List<String>, private var genresimg: List<String>) :
        RecyclerView.Adapter<GenresAdapter.ViewHolder>() {


    class ViewHolder(genreView: View) : RecyclerView.ViewHolder(genreView) {

        var genreTitle: TextView
        var genreImage: ANImageView
        init {
            genreTitle = genreView.genreTitle
            genreImage = genreView.genreImage
        }

        fun bindTo(genre: String, genreimg: String) {
            genreTitle.text = genre
            genreImage.setImageUrl(genreimg)

            genreImage.setOnClickListener {
                val fragment = HomeFragment()
                val bundle = Bundle()
                bundle.putString("Genre", genre) //key and value
                //set Fragmentclass Arguments
                fragment.arguments = bundle
                val feeds = it.context as MainActivity
                feeds.switchContent(fragment)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.content_genres, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return genres.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(genres[position], genresimg[position])
    }
}