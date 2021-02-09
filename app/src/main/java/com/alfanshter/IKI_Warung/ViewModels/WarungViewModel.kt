package com.alfanshter.IKI_Warung.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alfanshter.IKI_Warung.Model.StatusWarung
import com.alfanshter.IKI_Warung.Model.Warung
import com.alfanshter.IKI_Warung.Services.ApiClient
import com.alfanshter.IKI_Warung.Utils.BukaWarungResponse
import com.alfanshter.IKI_Warung.Utils.SingleLiveEvent
import com.alfanshter.IKI_Warung.Utils.WarungResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class WarungViewModel : ViewModel(){
    private var dataWarung = MutableLiveData<WarungResponse>()
    private var state : SingleLiveEvent<WarungState> = SingleLiveEvent()
    private var api = ApiClient.instance()

    fun getDataWarungById(token : String){
        state.value = WarungState.IsLoading(true)
        api.getDetailWarung(token).enqueue(object : Callback<WarungResponse>{
            override fun onResponse(call: Call<WarungResponse>, response: Response<WarungResponse>) {
                if (response.isSuccessful){
                    val body = response.body() as WarungResponse
                    dataWarung.postValue(body)
                }else{
                    state.value = WarungState.Error("Terjadi kesalahan.")
                }
                state.value = WarungState.IsLoading(false)
            }

            override fun onFailure(call: Call<WarungResponse>, t: Throwable) {
                state.value = WarungState.Error(t.message)
            }

        })
    }

    fun updateStateWarung(token: String, status: Boolean){
        state.value = WarungState.IsLoading(true)
        var statusWarung = StatusWarung(status)
        api.updateStatusWarung(token,statusWarung).enqueue(object : Callback<BukaWarungResponse>{
            override fun onResponse(call: Call<BukaWarungResponse>, response: Response<BukaWarungResponse>) {
                val body = response.body()
                state.value = WarungState.IsSucees(body!!.message.toString())
            }

            override fun onFailure(call: Call<BukaWarungResponse>, t: Throwable) {
                state.value = WarungState.Error(t.message)
            }

        })
    }

    fun getDataWarung() = dataWarung
    fun getState() = state

}

sealed class WarungState{
    data class ShowToast(var message : String) : WarungState()
    data class IsLoading(var state : Boolean) : WarungState()
    data class Error(var err : String?) : WarungState()
    data class IsSucees(var success : String) : WarungState()
    object Reset : WarungState()
}