package com.nullsoft.art.kuponchikru;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


/**
 * Created by art on 09.04.16.
 */
public interface LoginService
{
    @FormUrlEncoded
    @POST("userLogin")
    Call<ServerApi.ServerResponse> userLogin(@Field("login") String first, @Field("password") String last);

}
