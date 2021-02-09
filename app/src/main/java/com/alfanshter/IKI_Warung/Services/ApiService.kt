package com.alfanshter.IKI_Warung.Services

import com.alfanshter.IKI_Warung.Model.LoginData
import com.alfanshter.IKI_Warung.Model.StatusWarung
import com.alfanshter.IKI_Warung.Utils.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("api/food/login_food")
    fun loginWarung(@Body params :  LoginData) : Call<LoginResponse>

    //data warung
    @GET("api/food/warung_id")
    fun getDetailWarung(@Header("auth-token") token: String) : Call<WarungResponse>

    @Headers("Content-Type: application/json")
    @Multipart
    @POST("api/makanan/insert_makanan")
    fun tambahMakanan(@Header("auth-token") token : String,
                      @Part file : MultipartBody.Part,
                      @Part("nama") nama : RequestBody,
                      @Part("kategori") kategori : RequestBody,
                      @Part("harga") harga : RequestBody,
                      @Part("keterangan") keterangan : RequestBody,
                      @Part("status") status : RequestBody) : Call<TambahMakananResponse>

//    fun tambahMakanan(@Header("auth-token") token : String,
//                      @Part file: MultipartBody.Part,
//                      @Field("nama") nama : String,
//                      @Field("kategori") kategori : String,
//                      @Field("harga") harga : Int,
//                      @Field("keterangan") keterangan : String,
//                      @Field("status") status : Boolean) : Call<TambahMakananResponse>

    @Multipart
    @PUT("food/update")
    fun updateFotoAwal(@Header("auth-token") token : String,
                       @Part("file") foto: MultipartBody.Part)

    @FormUrlEncoded
    @PUT("food/update_akun")
    fun updatePasswordDeskripsi(@Header("auth-token") token : String,
                                @Field("password") password: String,
                                @Field("description") deskrispi : String)

    @Headers("Content-Type: application/json")
    @PUT("api/food/aktif_warung")
    fun updateStatusWarung(@Header("auth-token") token: String,
                           @Body params: StatusWarung) : Call<BukaWarungResponse>

    @FormUrlEncoded
    @PUT("makanan/{id}")
    fun updateStatusMakanan(@Header("auth-token") token : String,
                            @Path("id") id : String)

    @Multipart
    @PUT("makanan/update/{id}")
    fun updateMenuMakanan(@Header("auth-token") token : String,
                          @Part("file") foto : MultipartBody.Part,
                          @Field("keterangan") keterangan: String,
                          @Field("nama") nama: String,
                          @Field("harga") harga: String)


    //data makanan
    @GET("api/makanan/get_makanan_private")
    fun getMakananWarung(@Header("auth-token") token: String) : Call<List<MakananResponse>>

    //data minuman
    @GET("api/makanan/get_minuman_private")
    fun getMinumanWarung(@Header("auth-token") token: String) : Call<List<MinumanResponse>>



    @FormUrlEncoded
    @DELETE("makanan/{id}")
    fun deleteMakanan(@Header("auth-token") token: String,
                      @Path("id") id: String)






}