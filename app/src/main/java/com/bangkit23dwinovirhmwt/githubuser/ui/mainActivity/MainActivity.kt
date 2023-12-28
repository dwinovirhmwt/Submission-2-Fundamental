package com.bangkit23dwinovirhmwt.githubuser.ui.mainActivity

import android.annotation.SuppressLint
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
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit23dwinovirhmwt.githubuser.R
import com.bangkit23dwinovirhmwt.githubuser.data.response.GithubResponse
import com.bangkit23dwinovirhmwt.githubuser.data.response.ItemsItem
import com.bangkit23dwinovirhmwt.githubuser.data.retrofit.ApiConfig
import com.bangkit23dwinovirhmwt.githubuser.databinding.ActivityMainBinding
import com.bangkit23dwinovirhmwt.githubuser.ui.detailUser.DetailActivity
import com.bangkit23dwinovirhmwt.githubuser.ui.favoriteActivity.FavoriteActivity
import com.bangkit23dwinovirhmwt.githubuser.ui.themeActivity.ThemeActivity
import com.bangkit23dwinovirhmwt.githubuser.ui.themeActivity.ViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar?.title = getString(R.string.title_user_search)

        val viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.errorLiveData.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        initList()
        showData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val themeItem = menu?.findItem(R.id.action_theme)
        val themeIcon = themeItem!!.icon

        val favItem = menu.findItem(R.id.action_fav)
        val favIcon = favItem?.icon

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            themeIcon?.setTint(ContextCompat.getColor(this, R.color.white))
            favIcon?.setTint(ContextCompat.getColor(this, R.color.white))
        } else {
            themeIcon?.setTint(ContextCompat.getColor(this, R.color.black))
            favIcon?.setTint(ContextCompat.getColor(this, R.color.black))
        }
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_fav -> {
                Intent(this@MainActivity, FavoriteActivity::class.java).also {
                    startActivity(it)
                }
            }

            R.id.action_theme -> {
                Intent(this@MainActivity, ThemeActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showData() {
        showLoading(true)
        val client = ApiConfig.getApiService().getGithub(USER_ID)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setGithubUser(responseBody.items)
                    }
                } else {
                    Toast.makeText(this@MainActivity, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setGithubUser(items: List<ItemsItem?>?) {
        adapter.setList(items as ArrayList<ItemsItem>)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initList() {
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(users: ItemsItem) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USERNAME, users.login)
                startActivity(intent)
            }
        })

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[MainViewModel::class.java]

        binding.apply {
            listUser.layoutManager = LinearLayoutManager(this@MainActivity)
            listUser.setHasFixedSize(true)
            listUser.adapter = adapter
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.text = searchView.text
                    searchView.hide()
                    showLoading(true)
                    viewModel.findUsers(searchView.text.toString())
                    false
                }
        }

        viewModel.getListUsers().observe(this@MainActivity) {
            if (it != null) {
                adapter.setList(it)
                showLoading(false)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val USER_ID = "a"
    }
}