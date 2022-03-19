package com.picpay.desafio.android.contact.domain

import arrow.core.left
import arrow.core.right
import com.picpay.desafio.android.contact.model.User
import com.picpay.desafio.android.contact.repository.UserRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.io.IOException

class GetUsersUseCaseTest {

    lateinit var useCase: GetUsersUseCase

    @MockK
    private lateinit var repository: UserRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        useCase = GetUsersUseCase(repository)
    }

    @Test
    fun `should emit Loading and Error events when fetch users throw exception`() {
        coEvery { repository.getUsers() } throws IOException()

        runBlocking {
            useCase().collectIndexed { index, value ->
                if (index == 0) assertEquals(GetUsersLoading, value)
                if (index == 1) assertEquals(GetUsersError, value)
            }
        }
    }

    @Test
    fun `should emit Loading and Error events when fetch users error`() {
        coEvery { repository.getUsers() } returns "error".left()

        runBlocking {
            useCase().collectIndexed { index, value ->
                if (index == 0) assertEquals(GetUsersLoading, value)
                if (index == 1) assertEquals(GetUsersError, value)
            }
        }
    }

    @Test
    fun `should emit Loading and Success events  when fetch users with success`() {
        val users = listOf(
            User(
                id = 1,
                img = "https://randomuser.me/api/portraits/men/1.jpg",
                name = "Sandrine Spinka",
                username = "Tod86"
            )
        )

        coEvery { repository.getUsers() } returns users.right()

        runBlocking {
            useCase().collectIndexed { index, value ->
                if (index == 0) assertEquals(GetUsersLoading, value)
                if (index == 1) assertEquals(GetUsersSuccess(users), value)
            }
        }
    }
}
