package com.geekbrains.lesson4.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {

    @GET("users")
    Call<List<RetrofitModel>> loadUsers();

    @GET("users/{user}")
    Call<RetrofitModel> loadUsers(@Path("user") String user);

}
