package com.sidratul.finalsubmissionbfaa.db

import com.google.gson.annotations.SerializedName

data class  UserModel(
    @field:SerializedName("items")
    val items : ArrayList<User>

)