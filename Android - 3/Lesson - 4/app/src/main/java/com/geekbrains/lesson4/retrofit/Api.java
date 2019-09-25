package com.geekbrains.lesson4.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {

    @GET("users")
    Call<List<UserModel>> loadUsers();

    @GET("users/{user}")
    Call<UserModel> loadUsers(@Path("user") String user);

    @GET("users/{user}/repos")
    Call<List<RepoModel>> loadRepos(@Path("user") String user);

}
