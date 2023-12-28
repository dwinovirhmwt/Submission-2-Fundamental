package com.bangkit23dwinovirhmwt.githubuser.data.retrofit

import com.bangkit23dwinovirhmwt.githubuser.data.response.DetailUserResponse
import com.bangkit23dwinovirhmwt.githubuser.data.response.GithubResponse
import com.bangkit23dwinovirhmwt.githubuser.data.response.ItemsItem
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getGithub(
        @Query("q") query: String
    ): Call<GithubResponse>

    @GET("users/{username}")
    fun getUserGithubDetail(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<ItemsItem>>
}