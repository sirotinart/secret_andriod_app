package com.nullsoft.art.kuponchikru;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by art on 10.04.16.
 */
public interface RegistrationService
{
    @FormUrlEncoded
    @POST("userLogin")
    Call<ResponseBody> userRegistration(@Field("login") String login, @Field("firstName") String firstName,
                                 @Field("lastName") String LastName, @Field("city") String city,
                                 @Field("password") String last);
}
