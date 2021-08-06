package com.example.socialapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialapp.databinding.ItemPostBinding
import com.example.socialapp.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PostAdapter(options: FirestoreRecyclerOptions<Post>, private val listener: IPostAdapter): FirestoreRecyclerAdapter<Post, PostAdapter.PostViewHolder>(options) {

    class PostViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = PostViewHolder(view)

        viewHolder.binding.likeButton.setOnClickListener {
            listener.onLikedClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {

        holder.binding.postTitle.text = model.text
        holder.binding.userName.text = model.createdBy.displayName
        Glide.with(holder.binding.userImage).load(model.createdBy.photoUrl).circleCrop().into(holder.binding.userImage)
        holder.binding.likeCount.text = model.likedBy.size.toString()
        holder.binding.createdAt.text = Utils.getTimeAgo(model.createdAt)

        val auth = Firebase.auth
        val currentUserId = auth.currentUser!!.uid
        val isLiked = model.likedBy.contains(currentUserId)

        if(isLiked) {
            holder.binding.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.binding.likeButton.context, R.drawable.ic_liked))
        } else {
            holder.binding.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.binding.likeButton.context, R.drawable.ic_unliked))
        }

    }

}

interface IPostAdapter {
    fun onLikedClicked(postId: String)
}