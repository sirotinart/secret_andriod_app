package com.nullsoft.art.kuponchikru;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by art on 10.04.16.
 */
public class ServerApi {

    public class CityList {
        public List<String> cities;
    }

    public class ServerResponse {
        public boolean success;
        public String errorText;
        public String[] errors;
        public UserController.User user;
    }

    public interface CitiesList {
        @GET("api/cities")
        Call<CityList> getCities();
    }

    public interface LoginService
    {
        @FormUrlEncoded
        @POST("api/login/mobile")
        Call<ServerResponse> userLogin(@Field("login") String first, @Field("password") String last);

    }

    public interface RegistrationService
    {
        @FormUrlEncoded
        @POST("api/users")
        Call<ServerApi.ServerResponse> registerUser(@Field("login") String login, @Field("firstName") String firstName,
                                                    @Field("lastName") String lastName, @Field("city") String city,
                                                    @Field("password") String password);
    }

    public interface UserInfoUpdateService
    {
        @FormUrlEncoded
        @PUT("api/users")
        Call<ServerApi.ServerResponse> updateUser(@Field("id") int id, @Field("login") String login, @Field("firstName") String firstName,
                                                    @Field("lastName") String LastName, @Field("city") String city, @Field("password") String password,
                                                    @Field("newPassword") String newPassword);
    }

}
