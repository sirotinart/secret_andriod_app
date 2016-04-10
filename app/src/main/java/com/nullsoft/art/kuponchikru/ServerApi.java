package com.nullsoft.art.kuponchikru;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by art on 10.04.16.
 */
public class ServerApi {

    public class CityList {
        public List<String> cities;
    }

    public interface CitiesList {
        @GET("getCities")
        Call<CityList> getCities();
    }
}
