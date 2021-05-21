package com.coolcats.foodsharecc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.feed_item_layout.view.*

class PostAdapter(): RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    var posts: List<FoodPost> = mutableListOf()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    inner class PostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView  = LayoutInflater.from(parent.context).inflate(
            R.layout.feed_item_layout, parent, false
        )
        return PostViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.itemView.feed_caption_textview.text = post.caption
        Glide.with(holder.itemView)
            .applyDefaultRequestOptions(RequestOptions().centerCrop())
            .load(post.imageUrl)
            .into(holder.itemView.feed_imageview)
    }


    override fun getItemCount(): Int = posts.size
}