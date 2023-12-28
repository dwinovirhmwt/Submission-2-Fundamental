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

class FollowingViewModel : ViewModel() {

    private val _listFollowing = MutableLiveData<ArrayList<ItemsItem>>()
    private val listFollowing: LiveData<ArrayList<ItemsItem>> = _listFollowing

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData


    fun setListFollowing(username: String) {
        ApiConfig.getApiService()
            .getFollowing(username)
            .enqueue(object : Callback<List<ItemsItem>> {
                override fun onResponse(
                    call: Call<List<ItemsItem>>,
                    response: Response<List<ItemsItem>>
                ) {
                    if (response.isSuccessful) {
                        _listFollowing.postValue(response.body()?.let { ArrayList(it) })
                    }
                }

                override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                    val errorMessage = t.message.toString()
                    _errorLiveData.postValue(errorMessage)
                }

            })
    }

    fun getListFollowing(): LiveData<ArrayList<ItemsItem>> {
        return listFollowing
    }
}