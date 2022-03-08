package com.example.githubuser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.databinding.ItemAvatarBinding

class UserAdapter(
    private val listUser: List<user>
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private lateinit var binding: ItemAvatarBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemAvatarBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = listUser[position]
        with(binding) {
            Glide.with(binding.root.context).load(user.avatar).circleCrop().into(imgPhoto)
            tvItemName.text = user.name
            tvItemCompany.text = user.company
            tvItemUsername.text = user.username
        }
    }

    override fun getItemCount(): Int = listUser.size


}