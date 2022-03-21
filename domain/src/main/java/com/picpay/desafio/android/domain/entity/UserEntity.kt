package com.picpay.desafio.android.domain.entity

import android.os.Parcelable
import androidx.core.util.PatternsCompat
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlinx.parcelize.Parcelize

@JvmInline
value class Image(val value: String) {
    companion object {
        fun create(value: String?): Either<String, Image> =
            value?.let {
                if (PatternsCompat.WEB_URL.matcher(value).matches()) Image(value).right()
                else "Invalid image url: $value".left()
            } ?: "Image url is null".left()
    }
}

@JvmInline
value class Name(val value: String) {
    companion object {
        fun create(value: String): Either<String, Name> =
            if (value.isNotEmpty()) Name(value).right()
            else "Name is empty".left()
    }
}

@JvmInline
value class Id(val value: Int)

@JvmInline
value class Username(val value: String)

@Parcelize
data class UserEntity(
    val image: Image,
    val name: Name,
    val id: Id,
    val username: Username
) : Parcelable
