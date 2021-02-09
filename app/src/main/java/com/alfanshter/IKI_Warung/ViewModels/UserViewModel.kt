package com.alfanshter.IKI_Warung.ViewModels

import androidx.lifecycle.ViewModel
import com.alfanshter.IKI_Warung.Model.LoginData
import com.alfanshter.IKI_Warung.Services.ApiClient
import com.alfanshter.IKI_Warung.Utils.LoginResponse
import com.alfanshter.IKI_Warung.Utils.SingleLiveEvent
import com.alfanshter.udinlelangfix.Session.SessionManager
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserViewModel : ViewModel() {
    private var state : SingleLiveEvent<UserState> = SingleLiveEvent()
    private var api = ApiClient.instance()

    fun login(username: String, password: String){
        state.value = UserState.IsLoading(true)
        var login = LoginData(username,password)
        api.loginWarung(login).enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                println(t.message)
                state.value = UserState.Error(t.message)
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    val body = response.body() as LoginResponse
                    if(body.token != null){
                        state.value = UserState.Success("${body.token}")
                    }else{
                        state.value = UserState.Failed("Login gagal")
                    }

                }else{
                    state.value = UserState.Error("Kesalahan terjadi saat login")
                }

                state.value = UserState.IsLoading(false)
            }
        })
    }

    fun validate(email: String, password: String) : Boolean{
        state.value = UserState.Reset
        if(email.isEmpty() || password.isEmpty()){
            state.value = UserState.ShowToast("Username dan Password tidak boleh kosong")
            return false
        }
        return true
    }

    fun getState() =  state
}

sealed class UserState{
    data class Error(var err : String?) : UserState()
    data class ShowToast(var message : String) : UserState()
    data class Validate(var email : String? = null, var password : String? = null) : UserState()
    data class IsLoading(var state : Boolean = false) : UserState()
    data class Success(var token : String) : UserState()
    data class Failed(var message: String) : UserState()
    object Reset : UserState()
}