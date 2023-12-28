package com.bangkit23dwinovirhmwt.githubuser.ui.mainActivity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit23dwinovirhmwt.githubuser.data.response.ItemsItem
import com.bangkit23dwinovirhmwt.githubuser.databinding.UserListBinding
import com.bumptech.glide.Glide

class UserAdapter : RecyclerView.Adapter<UserAdapter.MyViewHolder>() {

    private var list = ArrayList<ItemsItem>()
    private var onItemClickCallback: OnItemClickCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = UserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder((binding))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class MyViewHolder(private val binding: UserListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(users: ItemsItem) {
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(users)
            }

            binding.apply {
                Glide.with(itemView)
                    .load(users.avatarUrl)
                    .centerCrop()
                    .into(photoUser)
                loginUser.text = users.login
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(users: List<ItemsItem>) {
        list.clear()
        list.addAll(users)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(users: ItemsItem)
    }
}