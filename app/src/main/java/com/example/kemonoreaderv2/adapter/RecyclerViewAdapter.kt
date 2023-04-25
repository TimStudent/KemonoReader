package com.example.kemonoreaderv2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kemonoreaderv2.R
import com.example.kemonoreaderv2.databinding.ImageHolderBinding
import com.squareup.picasso.Picasso

class RecyclerViewAdapter(
    private var listOfLinks: MutableList<String> = mutableListOf(),
    private val listener: (String) -> Unit
): RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ImageHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listOfLinks[position], listener)
    }

    override fun getItemCount(): Int {
        return listOfLinks.size
    }
    fun add(Links: List<String>) {
        listOfLinks.addAll(Links)
    }
    fun update(links: List<String>) {
        listOfLinks.clear()
        listOfLinks.addAll(links)
    }
}
class ViewHolder(private val binding: ImageHolderBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(link: String, listener: (String) -> Unit) {
        if (link.contains(".jpg") || link.contains(".png")) {
            Picasso
                .get()
                .load(link)
                .error(R.drawable._19_jeanne_berserker_alter_4)
                .into(binding.imageView)
        }
        if (link.contains(".mp4")) {
            //exoplayer for streaming
        }
        listener.invoke(link)
    }
}