package com.bangkit23dwinovirhmwt.githubuser.ui.fragmentFollow

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit23dwinovirhmwt.githubuser.R
import com.bangkit23dwinovirhmwt.githubuser.databinding.FragmentFollowAdapterBinding
import com.bangkit23dwinovirhmwt.githubuser.ui.detailUser.DetailActivity
import com.bangkit23dwinovirhmwt.githubuser.ui.mainActivity.UserAdapter

class FollowersFragment : Fragment(R.layout.fragment_follow_adapter) {

    private lateinit var _binding: FragmentFollowAdapterBinding
    private val binding get() = _binding
    private lateinit var adapter: UserAdapter
    private lateinit var viewModel: FollowersViewModel
    private lateinit var username: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val argument = arguments
        username = argument?.getString(DetailActivity.EXTRA_USERNAME).toString()
        _binding = FragmentFollowAdapterBinding.bind(view)

        val viewModel = ViewModelProvider(this)[FollowersViewModel::class.java]
        viewModel.errorLiveData.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        initFollowers()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initFollowers() {
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        binding.apply {
            rvFollow.layoutManager = LinearLayoutManager(requireActivity())
            rvFollow.setHasFixedSize(true)
            rvFollow.adapter = adapter
        }

        showLoading(true)
        viewModel = ViewModelProvider(
            this@FollowersFragment,
            ViewModelProvider.NewInstanceFactory()
        )[FollowersViewModel::class.java]

        viewModel.setListFollowers(username)
        viewModel.getListFollowers().observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.setList(it)
                showLoading(false)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbFragment.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding
    }
}