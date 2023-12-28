package com.bangkit23dwinovirhmwt.githubuser.ui.mainActivity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit23dwinovirhmwt.githubuser.data.response.GithubResponse
import com.bangkit23dwinovirhmwt.githubuser.data.response.ItemsItem
import com.bangkit23dwinovirhmwt.githubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    val userGithub = MutableLiveData<ArrayList<ItemsItem>>()

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    fun findUsers(query: String) {
        ApiConfig.getApiService()
            .getGithub(query)
            .enqueue(object : Callback<GithubResponse> {
                override fun onResponse(
                    call: Call<GithubResponse>,
                    response: Response<GithubResponse>
                ) {
                    if (response.isSuccessful) {
                        userGithub.postValue(response.body()?.items as ArrayList<ItemsItem>?)
                        Log.d("debug", userGithub.value.toString())
                    }
                }

                override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                    val errorMessage = t.message.toString()
                    _errorLiveData.postValue(errorMessage)
                }
            })
    }

    fun getListUsers(): LiveData<ArrayList<ItemsItem>> {
        return userGithub
    }
}