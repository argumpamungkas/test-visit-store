package com.argump.visitstore.source

import com.argump.visitstore.model.ResponseStore
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiEndpoint {

    @FormUrlEncoded
    @POST("sariroti_md/index.php/login/loginTest")
    suspend fun postLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ) : ResponseStore
}