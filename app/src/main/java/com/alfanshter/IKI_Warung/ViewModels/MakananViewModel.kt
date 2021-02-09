package com.alfanshter.IKI_Warung.ViewModels

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alfanshter.IKI_Warung.Services.ApiClient
import com.alfanshter.IKI_Warung.Utils.MakananResponse
import com.alfanshter.IKI_Warung.Utils.SingleLiveEvent
import com.alfanshter.IKI_Warung.Utils.TambahMakananResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import java.net.URI

class MakananViewModel : ViewModel(){
    private var makanans = MutableLiveData<List<MakananResponse>>()
    private var state : SingleLiveEvent<MakananState> = SingleLiveEvent()
    private var api = ApiClient.instance()

    fun fetchAllMakanan(token : String){
        state.value = MakananState.IsLoading(true)
        api.getMakananWarung(token).enqueue(object : Callback<List<MakananResponse>>{
            override fun onResponse(call: Call<List<MakananResponse>>, response: Response<List<MakananResponse>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    makanans.postValue(body)
                }else{
                    state.value = MakananState.Error("Terjadi kesalahan. Gagal mendapatkan response")
                }
                state.value = MakananState.IsLoading(false)
            }

            override fun onFailure(call: Call<List<MakananResponse>>, t: Throwable) {
                state.value = MakananState.Error(t.message)
            }

        })
    }


    fun addMakanan(token: String, nama: RequestBody, kategori: RequestBody, harga: RequestBody, keterangan: RequestBody, status: RequestBody, file : MultipartBody.Part){
        state.value = MakananState.IsLoading(true)
        api.tambahMakanan(token,file,nama,kategori,harga,keterangan,status).enqueue(object : Callback<TambahMakananResponse>{
            override fun onResponse(call: Call<TambahMakananResponse>, response: Response<TambahMakananResponse>) {
                if (response.isSuccessful){
                    val body = response.body()
                    state.value = MakananState.Success(body!!.message.toString())
                }else{
                    state.value = MakananState.Error("Terjadi kesalahan. Gagal mendapatkan response")
                }

                state.value = MakananState.IsLoading(false)
            }

            override fun onFailure(call: Call<TambahMakananResponse>, t: Throwable) {
                state.value = MakananState.Error(t.message)
            }

        })
    }


//    fun addMakanan(token: String, nama: String, kategori: String, harga: Int, keterangan: String, status: Boolean, file : MultipartBody.Part){
//        state.value = MakananState.IsLoading(true)
//        api.tambahMakanan(token,file,nama,kategori,harga,keterangan,false).enqueue(object : Callback<TambahMakananResponse>{
//            override fun onResponse(call: Call<TambahMakananResponse>, response: Response<TambahMakananResponse>) {
//                if (response.isSuccessful){
//                    val body = response.body()
//                    state.value = MakananState.Success(body!!.message.toString())
//                }else{
//                    state.value = MakananState.Error("Terjadi kesalahan. Gagal mendapatkan response")
//                }
//
//                state.value = MakananState.IsLoading(false)
//            }
//
//            override fun onFailure(call: Call<TambahMakananResponse>, t: Throwable) {
//                state.value = MakananState.Error(t.message)
//            }
//
//        })
//    }

    fun validate(nama: String, harga: Int, keterangan: String, uri : Uri?) : Boolean{
        state.value = MakananState.Reset
//        if(email.isEmpty() || password.isEmpty()){
//            state.value = MakananState.ShowToast("Username dan Password tidak boleh kosong")
//            return false
//        }
        if(nama.isEmpty()){
            state.value = MakananState.ShowToast("Masukkan Nama Makanan/Minuman")
            return  false
        }else if (harga <= 0){
            state.value = MakananState.ShowToast("Masukkan Harga")
            return false
        }else if(keterangan.isEmpty()){
            state.value = MakananState.ShowToast("Masukkan Keterangan")
            return false
        }else if(uri == null){
            state.value = MakananState.ShowToast("Silahkan pilih foto terlebih dahulu")
            return false
        }
        return true
    }

    fun getMakanan() = makanans
    fun getState() = state
}

sealed class MakananState{
    data class ShowToast(var message : String) : MakananState()
    data class IsLoading(var state : Boolean = false) : MakananState()
    data class Error(var err : String?) : MakananState()
//    data class IsSuccess(var what : Int? = null) : MakananState()
    data class Success(var message : String) : MakananState()
    data class Failed(var message: String) : MakananState()
    object Reset : MakananState()
}