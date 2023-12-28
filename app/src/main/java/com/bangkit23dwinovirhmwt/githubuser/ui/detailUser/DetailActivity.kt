package com.bangkit23dwinovirhmwt.githubuser.ui.detailUser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.bangkit23dwinovirhmwt.githubuser.R
import com.bangkit23dwinovirhmwt.githubuser.data.database.FavoriteEntity
import com.bangkit23dwinovirhmwt.githubuser.databinding.ActivityDetailBinding
import com.bangkit23dwinovirhmwt.githubuser.ui.ViewModelFactory
import com.bangkit23dwinovirhmwt.githubuser.ui.fragmentFollow.FollowAdapter
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {
    private lateinit var _bindingDetail: ActivityDetailBinding
    private val binding get() = _bindingDetail
    private var favoriteEntity: FavoriteEntity? = null

    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _bindingDetail = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val actionbar = supportActionBar
        actionbar?.title = getString(R.string.title_user_detail)

        viewModel.errorLiveData.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        initList()
    }

    private fun initList() {
        showLoading(true)

        val username = intent.getStringExtra(EXTRA_USERNAME) ?: "null"
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.deleteAllUsersFavorite()
                }
                launch {
                    viewModel.setUserGithubDetail(username)
                    Log.e("Detail User", "username: $username")
                    viewModel.getUserGithubDetail().observe(this@DetailActivity) {
                        favoriteEntity = FavoriteEntity(
                            it.login,
                            it.avatarUrl,
                            false
                        )
                        binding.apply {
                            tvUsername.text = it.login
                            totalFollowers.text = it.followers.toString()
                            totalFollowing.text = it.following.toString()

                            // Periksa dan atur Nama
                            if (!it.name.isNullOrEmpty()) {
                                tvName.text = it.name
                                tvName.visibility = View.VISIBLE
                            } else {
                                tvName.visibility = View.GONE
                            }

                            // Periksa dan atur Lokasi
                            if (!it.location.isNullOrEmpty()) {
                                ivLoc.visibility = View.VISIBLE
                                tvLocation.text = it.location
                                tvLocation.visibility = View.VISIBLE
                            } else {
                                ivLoc.visibility = View.GONE
                                tvLocation.visibility = View.GONE
                            }

                            // Periksa dan atur Bio
                            if (it.bio != null && it.bio is String && it.bio.isNotBlank()) {
                                tvBio.text = it.bio
                                tvBio.visibility = View.VISIBLE
                            } else {
                                tvBio.visibility = View.GONE
                            }

                            Glide.with(this@DetailActivity)
                                .load(it.avatarUrl)
                                .centerCrop()
                                .into(ivProfile)

                            if (it != null) {
                                showLoading(false)
                            }
                        }
                    }
                }
                launch {
                    var isFavorite = viewModel.isFavorite(username)
                    setFavorite(isFavorite)
                    Log.d("Favorite", isFavorite.toString())

                    binding.fabFav.setOnClickListener {
                        if (isFavorite) {
                            favoriteEntity?.let { viewModel.deleteUserFromFavorite(it) }

                            Toast.makeText(
                                this@DetailActivity,
                                "Delete from favorite",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            isFavorite = false
                            setFavorite(isFavorite)

                        } else {
                            favoriteEntity?.let { viewModel.insertUserToFavorite(it) }

                            favoriteEntity?.let { viewModel.saveUserToFavorite(it) }
                            Toast.makeText(
                                this@DetailActivity,
                                "Add to favorite",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            isFavorite = true
                            setFavorite(isFavorite)
                        }
                    }
                }
            }
        }

        val followAdapter = FollowAdapter(this, bundle)
        val viewPager: ViewPager2 = findViewById(R.id.vpFollow)
        viewPager.adapter = followAdapter
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    private fun setFavorite(isFavorite: Boolean) {
        val drawableResId =
            if (isFavorite) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off
        binding.fabFav.setImageResource(drawableResId)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)

        val shareFitur = menu?.findItem(R.id.action_share)
        val shareIcon = shareFitur?.icon

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            shareIcon?.setTint(ContextCompat.getColor(this, R.color.white))
        } else {
            shareIcon?.setTint(ContextCompat.getColor(this, R.color.black))
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }

            R.id.action_share -> {
                val username = intent.getStringExtra(EXTRA_USERNAME) ?: "null"
                val share = Intent(Intent.ACTION_SEND)
                share.type = "text/plain"
                val url = "https://github.com/$username"
                share.apply {
                    putExtra(Intent.EXTRA_SUBJECT, "More Information")
                    putExtra(Intent.EXTRA_TEXT, url)
                    startActivity(Intent.createChooser(share, "Share to"))
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"

        private val TAB_TITLES: IntArray
            get() = intArrayOf(
                R.string.followers,
                R.string.following
            )
    }
}

