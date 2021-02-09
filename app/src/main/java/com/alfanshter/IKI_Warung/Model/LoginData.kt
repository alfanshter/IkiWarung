package com.alfanshter.IKI_Warung.Model

import com.google.gson.annotations.SerializedName

class LoginData {
    @SerializedName("username") private var username : String? = null
    @SerializedName("password") private var password : String? = null

    constructor(username: String?, password: String?) {
        this.username = username
        this.password = password
    }
}