package com.picpay.desafio.android.domain

import arrow.core.left
import arrow.core.right
import com.picpay.desafio.android.domain.entity.Id
import com.picpay.desafio.android.domain.entity.Image
import com.picpay.desafio.android.domain.entity.Name
import com.picpay.desafio.android.domain.entity.UserEntity
import com.picpay.desafio.android.domain.entity.Username
import com.picpay.desafio.android.domain.repository.UserRepository
import com.picpay.desafio.android.domain.usecase.GetUsersError
import com.picpay.desafio.android.domain.usecase.GetUsersLoading
import com.picpay.desafio.android.domain.usecase.GetUsersSuccess
import com.picpay.desafio.android.domain.usecase.GetUsersUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
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
            UserEntity(
                id = Id(1),
                image = Image("https://randomuser.me/api/portraits/men/1.jpg"),
                name = Name("Sandrine Spinka"),
                username = Username("Tod86")
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
