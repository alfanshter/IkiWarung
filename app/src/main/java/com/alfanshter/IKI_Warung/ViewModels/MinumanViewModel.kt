package com.alfanshter.IKI_Warung.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alfanshter.IKI_Warung.Services.ApiClient
import com.alfanshter.IKI_Warung.Utils.MakananResponse
import com.alfanshter.IKI_Warung.Utils.MinumanResponse
import com.alfanshter.IKI_Warung.Utils.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MinumanViewModel : ViewModel(){
    private var minumans = MutableLiveData<List<MinumanResponse>>()
    private var state : SingleLiveEvent<MinumanState> = SingleLiveEvent()
    private var api = ApiClient.instance()

    fun fetchAllMinuman(token : String){
        state.value = MinumanState.IsLoading(true)
        api.getMinumanWarung(token).enqueue(object : Callback<List<MinumanResponse>>{
            override fun onResponse(call: Call<List<MinumanResponse>>, response: Response<List<MinumanResponse>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    minumans.postValue(body)
                }else{
                    state.value = MinumanState.Error("Terjadi kesalahan. Gagal mendapatkan response")
                }
                state.value = MinumanState.IsLoading(false)
            }

            override fun onFailure(call: Call<List<MinumanResponse>>, t: Throwable) {
                state.value = MinumanState.Error(t.message)
            }

        })
    }

    fun getMinuman() = minumans
}

sealed class MinumanState{
    data class ShowToast(var message : String) : MinumanState()
    data class IsLoading(var state : Boolean = false) : MinumanState()
    data class Error(var err : String?) : MinumanState()
    data class IsSuccess(var what : Int? = null) : MinumanState()
    object Reset : MinumanState()
}