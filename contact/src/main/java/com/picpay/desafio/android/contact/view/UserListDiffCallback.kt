package com.picpay.desafio.android.contact.view

import androidx.recyclerview.widget.DiffUtil
import com.picpay.desafio.android.domain.entity.UserEntity

class UserListDiffCallback(
    private val oldList: List<UserEntity>,
    private val newList: List<UserEntity>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].username == newList[newItemPosition].username

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = true
}
