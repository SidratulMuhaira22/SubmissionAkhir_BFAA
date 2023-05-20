package com.sidratul.finalsubmissionbfaa.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteUser::class], version = 1)
abstract class UserFavDatabase : RoomDatabase() {

    companion object {
        var INSTANCE: UserFavDatabase? = null

        fun getInstance(context: Context): UserFavDatabase? {
            if (INSTANCE == null) {
                synchronized(UserFavDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        UserFavDatabase::class.java,
                        "userFav_database").build()
                }
            }
            return INSTANCE
        }
    }

    abstract fun favoritDao() : FavoritDao

}