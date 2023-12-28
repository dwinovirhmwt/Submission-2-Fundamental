package com.bangkit23dwinovirhmwt.githubuser.ui.fragmentFollow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit23dwinovirhmwt.githubuser.data.response.ItemsItem
import com.bangkit23dwinovirhmwt.githubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel() {
    private val _listFollowers = MutableLiveData<ArrayList<ItemsItem>>()
    private val listFollowers: LiveData<ArrayList<ItemsItem>> = _listFollowers

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    fun setListFollowers(username: String) {
        ApiConfig.getApiService()
            .getFollowers(username)
            .enqueue(object : Callback<List<ItemsItem>> {
                override fun onResponse(
                    call: Call<List<ItemsItem>>,
                    response: Response<List<ItemsItem>>
                ) {
                    if (response.isSuccessful) {
                        _listFollowers.postValue(response.body()?.let { ArrayList(it) })
                    }
                }

                override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                    val errorMessage = t.message.toString()
                    _errorLiveData.postValue(errorMessage)
                }

            })
    }

    fun getListFollowers(): LiveData<ArrayList<ItemsItem>> {
        return listFollowers
    }
}