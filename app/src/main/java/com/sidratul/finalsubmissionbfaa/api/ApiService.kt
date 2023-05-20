package com.sidratul.finalsubmissionbfaa.api

import com.sidratul.finalsubmissionbfaa.BuildConfig
import com.sidratul.finalsubmissionbfaa.db.DetailuserModel
import com.sidratul.finalsubmissionbfaa.db.User
import com.sidratul.finalsubmissionbfaa.db.UserModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    @Headers("Authorization: token" + BuildConfig.KEY)
    fun getUser(
        @Query("q") query: String
    ): Call<UserModel>


    @GET("users/{username}")
    @Headers("Authorization: token" + BuildConfig.KEY)
    fun getUserDetail(
        @Path("username") username: String
    ): Call<DetailuserModel>


    @GET("users/{username}/followers")
    @Headers("Authorization: token" + BuildConfig.KEY)
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<ArrayList<User>>


    @GET("users/{username}/following")
    @Headers("Authorization: token" + BuildConfig.KEY)
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<ArrayList<User>>

}