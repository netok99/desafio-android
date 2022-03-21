package com.picpay.desafio.android.contact

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import arrow.core.left
import arrow.core.right
import com.picpay.desafio.android.contact.viewmodel.ContactViewModel
import com.picpay.desafio.android.domain.entity.*
import com.picpay.desafio.android.domain.repository.UserRepository
import com.picpay.desafio.android.domain.usecase.*
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ContactViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ContactViewModel

    lateinit var useCase: GetUsersUseCase

    @MockK
    private lateinit var repository: UserRepository

    @RelaxedMockK
    private lateinit var observerAction: Observer<GetUsersState>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = GetUsersUseCase(repository)
        viewModel = ContactViewModel(useCase)
        viewModel.action.observeForever(observerAction)
    }

    @Test
    fun shouldOrderEventSuccessWhenFetchUsesViewModelSuccess() {
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
            viewModel.fetchUsers()

            verifyOrder {
                observerAction.onChanged(GetUsersLoading)
                observerAction.onChanged(GetUsersSuccess(users))
            }
        }
    }

    @Test
    fun shouldOrderEventErrorWhenFetchUsesViewModelError() {
        coEvery { repository.getUsers() } returns "Message Error".left()

        runBlocking {
            viewModel.fetchUsers()

            verifyOrder {
                observerAction.onChanged(GetUsersLoading)
                observerAction.onChanged(GetUsersError)
            }
        }
    }
}
