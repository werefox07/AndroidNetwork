package com.geekbrains.lesson4.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RepoModel {
    @SerializedName("name")
    @Expose
    private String name;

    public String getName() {
        return name;
    }
}