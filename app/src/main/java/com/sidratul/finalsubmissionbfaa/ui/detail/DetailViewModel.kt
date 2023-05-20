package com.sidratul.finalsubmissionbfaa.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sidratul.finalsubmissionbfaa.api.ApiConfig
import com.sidratul.finalsubmissionbfaa.db.DetailuserModel
import com.sidratul.finalsubmissionbfaa.db.FavoritDao
import com.sidratul.finalsubmissionbfaa.db.FavoriteUser
import com.sidratul.finalsubmissionbfaa.db.UserFavDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application): AndroidViewModel(application) {
    val userDetail = MutableLiveData<DetailuserModel>()
    private var userDao: FavoritDao?
    private var db : UserFavDatabase?

    init {
        db = UserFavDatabase.getInstance(application)
        userDao = db?.favoritDao()
    }

    fun setUserDetail(username: String) {
        ApiConfig.apiInstance
            .getUserDetail(username)
            .enqueue(object : Callback<DetailuserModel> {
                override fun onResponse(
                    call: Call<DetailuserModel>,
                    response: Response<DetailuserModel>,
                ) {
                    if (response.isSuccessful) {
                        userDetail.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<DetailuserModel>, t: Throwable) {
                    Log.d("onFailure", t.message.toString())
                }
            })
    }

    fun addFavorite(username: String, id: Int, avatar_url: String, url: String)  {
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavoriteUser(
                username,
                id,
                avatar_url,
                url
            )
            userDao?.addFavorite(user)
        }
    }

    suspend fun checkUser(id: Int) = userDao?.checkUser(id)

    fun deleteFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.deleteFavorite(id)
        }
    }

    fun getUserDetail(): LiveData<DetailuserModel> {
        return userDetail
    }

}