package com.picpay.desafio.android.contact.repository

import arrow.core.left
import arrow.core.right
import com.picpay.desafio.android.contact.model.User
import com.picpay.desafio.android.contact.remote.PicPayService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class UserRepositoryTest {

    private lateinit var repository: UserRepositoryImpl

    @MockK
    private lateinit var service: PicPayService

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = UserRepositoryImpl(service)
    }

    @Test
    fun shouldEitherLeftWhenGetUsersReturnEmptyListFromService() {
        val users = emptyList<User>()

        coEvery { service.getUsers() } returns users

        runBlocking {
            val response = repository.getUsers()
            assertEquals(response, EMPTY_LIST_MESSAGE.left())
        }
    }

    @Test
    fun shouldReturnEitherRightWithUsersRightWhenGetUsersReturnNonEmptyListFromService() {
        val users = listOf(
            User(
                id = 1,
                img = "https://randomuser.me/api/portraits/men/1.jpg",
                name = "Sandrine Spinka",
                username = "Tod86"
            )
        )

        coEvery { service.getUsers() } returns users

        runBlocking {
            val response = repository.getUsers()
            assertEquals(response, users.right())
        }
    }
}
