package com.picpay.desafio.android.contact.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.picpay.desafio.android.contact.R
import com.picpay.desafio.android.contact.databinding.ListItemUserBinding
import com.picpay.desafio.android.domain.entity.UserEntity

class UserListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var users = emptyList<UserEntity>()
        set(value) {
            DiffUtil.calculateDiff(UserListDiffCallback(field, value))
                .dispatchUpdatesTo(this)
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        UserListItemViewHolder(
            ListItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as UserListItemViewHolder).bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    inner class UserListItemViewHolder(private val binding: ListItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: UserEntity) = with(binding) {
            picture.load(user.image.value) {
                crossfade(true)
                placeholder(R.drawable.ic_round_account_circle)
                transformations(CircleCropTransformation())
            }
            username.text = user.username.value
            name.text = user.name.value
        }
    }
}
