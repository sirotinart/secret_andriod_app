package com.nullsoft.art.kuponchikru;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
        public User user;
        public ArrayList<Coupon> couponsList;
        public ArrayList<CouponWithCode> userCouponsList;
        public Coupon coupon;
    }

    public interface CitiesList {
        @GET("api/cities")
        Call<CityList> getCities();
    }

    public interface UserService
    {
        @FormUrlEncoded
        @POST("api/mobile/login")
        Call<ServerResponse> login(@Field("login") String first, @Field("password") String last);

        @POST("api/mobile/logout")
        Call<ServerResponse> logout();

        @FormUrlEncoded
        @POST("api/users")
        Call<ServerApi.ServerResponse> register(@Field("login") String login, @Field("firstName") String firstName,
                                                    @Field("lastName") String lastName, @Field("city") String city,
                                                    @Field("password") String password);

        @FormUrlEncoded
        @PUT("api/users")
        Call<ServerApi.ServerResponse> update(@Field("userId") int id, @Field("login") String login, @Field("firstName") String firstName,
                                                  @Field("lastName") String lastName, @Field("city") String city, @Field("password") String password,
                                                  @Field("newPassword") String newPassword);

    }

    public interface CouponsService
    {
        @GET("api/coupons")
        Call<ServerResponse> getCouponsList(@Query("startFrom") int startFrom);

        @GET("api/coupons/{id}")
        Call<ServerResponse> getCoupon(@Path("id") int couponId);

        @GET("api/purchases/{id}")
        Call<ServerResponse> getUserCouponsList(@Path("id") int userId);

        @FormUrlEncoded
        @POST("api/purchases")
        Call<ServerResponse> buyCoupon(@Field("userId") int userId, @Field("couponId") int couponId);
    }

    public static CouponsService getCouponsService()
    {
        return RetrofitManager.getRetrofit().create(CouponsService.class);
    }

    public static UserService getUserService()
    {
        return RetrofitManager.getRetrofit().create(UserService.class);
    }
}
