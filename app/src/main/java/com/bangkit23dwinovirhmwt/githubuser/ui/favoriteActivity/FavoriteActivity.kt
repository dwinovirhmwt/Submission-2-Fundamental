package com.bangkit23dwinovirhmwt.githubuser.ui.favoriteActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.activity.viewModels
import com.bangkit23dwinovirhmwt.githubuser.R
import com.bangkit23dwinovirhmwt.githubuser.databinding.ActivityFavoriteBinding
import com.bangkit23dwinovirhmwt.githubuser.ui.ViewModelFactory
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteAdapter: FavoriteAdapter
    private val viewModel: FavoriteViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val actionbar = supportActionBar
        actionbar?.title = getString(R.string.title_user_favorite)

        favoriteAdapter = FavoriteAdapter()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    binding.progressBar.visibility = View.VISIBLE
                    viewModel.getAllFavoriteUser().observe(this@FavoriteActivity) {
                        if (it != null) {
                            Log.d("Data Favorite", it.toString())
                            binding.progressBar.visibility = View.GONE
                            favoriteAdapter.submitList(it)
                            binding.listUser.apply {
                                layoutManager = LinearLayoutManager(this@FavoriteActivity)
                                setHasFixedSize(true)
                                adapter = favoriteAdapter
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}

