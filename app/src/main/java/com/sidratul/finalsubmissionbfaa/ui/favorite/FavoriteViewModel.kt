package com.sidratul.finalsubmissionbfaa.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.sidratul.finalsubmissionbfaa.db.FavoritDao
import com.sidratul.finalsubmissionbfaa.db.FavoriteUser
import com.sidratul.finalsubmissionbfaa.db.UserFavDatabase

class FavoriteViewModel(application: Application): AndroidViewModel(application) {

    private var userDao: FavoritDao?
    private var db : UserFavDatabase?

    init {
        db = UserFavDatabase.getInstance(application)
        userDao = db?.favoritDao()
    }

    fun getFavUser(): LiveData<List<FavoriteUser>>? {
        return userDao?.getAllFavUser()
    }

}