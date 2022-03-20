package com.picpay.desafio.android.contact.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.picpay.desafio.android.contact.databinding.ContactFragmentBinding
import com.picpay.desafio.android.contact.domain.GetUsersError
import com.picpay.desafio.android.contact.domain.GetUsersLoading
import com.picpay.desafio.android.contact.domain.GetUsersSuccess
import com.picpay.desafio.android.contact.model.User
import org.koin.androidx.viewmodel.ext.android.viewModel

class ContactFragment : Fragment() {

    private lateinit var binding: ContactFragmentBinding
    private val viewModel: ContactViewModel by viewModel()
    private val userListAdapter by lazy { UserListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ContactFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setupObservers()
        viewModel.fetchUsers()
    }

    private fun initViews() = with(binding) {
        recyclerView.adapter = userListAdapter
        userListProgressBar.visibility = View.VISIBLE
    }

    private fun setupObservers() = viewModel.action.observe(viewLifecycleOwner) {
        when (it) {
            is GetUsersLoading -> showLoadingState()
            is GetUsersError -> showErrorState()
            is GetUsersSuccess -> showSuccessState(it.users)
        }
    }

    private fun showSuccessState(users: List<User>) = with(binding) {
        userListAdapter.users = users
        userListProgressBar.visibility = View.GONE
        errorMessage.visibility = View.GONE
        retry.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    private fun showErrorState() = with(binding) {
        userListProgressBar.visibility = View.GONE
        recyclerView.visibility = View.GONE
        errorMessage.visibility = View.VISIBLE
        retry.visibility = View.VISIBLE
    }

    private fun showLoadingState() = with(binding) {
        recyclerView.visibility = View.GONE
        errorMessage.visibility = View.GONE
        retry.visibility = View.GONE
        userListProgressBar.visibility = View.VISIBLE
    }
}
