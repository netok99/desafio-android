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
            is GetUsersLoading -> showLoading()
            is GetUsersError -> showError()
            is GetUsersSuccess -> showSuccess(it.users)
        }
    }

    private fun showSuccess(users: List<User>) {
        userListAdapter.users = users
        binding.userListProgressBar.visibility = View.GONE
    }

    private fun showError() {
        binding.userListProgressBar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.userListProgressBar.visibility = View.VISIBLE
    }
}
