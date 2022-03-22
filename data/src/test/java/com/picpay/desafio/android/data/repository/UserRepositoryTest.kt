package com.picpay.desafio.android.data.repository

import arrow.core.left
import arrow.core.right
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.remote.PicPayService
import com.picpay.desafio.android.domain.entity.Id
import com.picpay.desafio.android.domain.entity.Image
import com.picpay.desafio.android.domain.entity.Name
import com.picpay.desafio.android.domain.entity.UserEntity
import com.picpay.desafio.android.domain.entity.Username
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
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

        val expected = "Empty List".left()
        runBlocking {
            val response = repository.getUsers()
            assertEquals(expected, response)
        }
    }

    @Test
    fun shouldReturnEitherRightWithUsersRightWhenGetUsersReturnNonEmptyListFromService() {
        val usersModel = listOf(
            User(
                id = 1,
                image = "https://randomuser.me/api/portraits/men/1.jpg",
                name = "Sandrine Spinka",
                username = "Tod86"
            )
        )

        val expected = listOf(
            UserEntity(
                id = Id(1),
                image = Image("https://randomuser.me/api/portraits/men/1.jpg"),
                name = Name("Sandrine Spinka"),
                username = Username("Tod86")
            )
        ).right()

        coEvery { service.getUsers() } returns usersModel

        runBlocking {
            val response = repository.getUsers()
            assertEquals(expected, response)
        }
    }

    @Test
    fun shouldReturnEitherLeftWhenGetUsersReturnImageBadUrl() {
        val usersModel = listOf(
            User(
                id = 1,
                image = "bad url",
                name = "Sandrine Spinka",
                username = "Tod86"
            )
        )

        coEvery { service.getUsers() } returns usersModel

        val expected = "Invalid image url: bad url".left()

        runBlocking {
            assertEquals(expected, repository.getUsers())
        }
    }

    @Test
    fun shouldReturnEitherLeftWhenGetUsersReturnImageNull() {
        val usersModel = listOf(
            User(
                id = 1,
                image = null,
                name = "Sandrine Spinka",
                username = "Tod86"
            )
        )

        coEvery { service.getUsers() } returns usersModel

        val expected = "Image url is null".left()

        runBlocking {
            assertEquals(expected, repository.getUsers())
        }
    }

    @Test
    fun shouldReturnEitherLeftWhenGetUsersReturnEmptyName() {
        val usersModel = listOf(
            User(
                id = 1,
                image = "https://randomuser.me/api/portraits/men/1.jpg",
                name = "",
                username = "Tod86"
            )
        )

        coEvery { service.getUsers() } returns usersModel

        val expected = "Name is empty".left()

        runBlocking {
            assertEquals(expected, repository.getUsers())
        }
    }
}
