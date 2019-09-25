package com.geekbrains.lesson4.retrofit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.geekbrains.lesson4.R;

import java.io.IOException;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitMainActivity extends AppCompatActivity {

    private TextView mInfoTextView;
    private ProgressBar progressBar;
    private EditText editText;
    private Api restAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        mInfoTextView = (TextView) findViewById(R.id.tvLoad);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Button btnLoad = (Button) findViewById(R.id.btnLoad);
        btnLoad.setOnClickListener((v) -> onClick());
    }

    public void onClick() {
        mInfoTextView.setText("");
        Retrofit retrofit = null;
        try {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            restAPI = retrofit.create(Api.class);
        } catch (Exception io) {
            mInfoTextView.setText("no retrofit: " + io.getMessage());
            return;
        }
        // Подготовили вызов на сервер
        Call<RetrofitModel> call = restAPI.loadUsers(editText.getText().toString());
        if (isNetworkSuccess()) {
            // Запускаем
            try {
                progressBar.setVisibility(View.VISIBLE);
                downloadOneUser(call);
            } catch (IOException e) {
                e.printStackTrace();
                mInfoTextView.setText(e.getMessage());
            }
        } else {
            Toast.makeText(this, "Подключите интернет", Toast.LENGTH_SHORT).show();
        }
    }

    private Boolean isNetworkSuccess() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();
        return networkinfo != null && networkinfo.isConnected();
    }

    private void downloadUsers(Call<List<RetrofitModel>> call) throws IOException {
        call.enqueue(new Callback<List<RetrofitModel>>() {
            @Override
            public void onResponse(Call<List<RetrofitModel>> call, Response<List<RetrofitModel>> response) {
                if (response.isSuccessful()) {
                    //String data = response.body().toString();
                    RetrofitModel curRetrofitModel = null;
                    for (int i = 0; i < response.body().size(); i++) {
                        curRetrofitModel = response.body().get(i);
                        mInfoTextView.append("\nLogin = " + curRetrofitModel.getLogin() +
                                "\nId = " + curRetrofitModel.getId() +
                                "\nURI" + curRetrofitModel.getAvatarUrl() +
                                "\n-----------------");
                    }
                } else {
                    System.out.println("onResponse error: " + response.code());
                    mInfoTextView.setText("onResponse error: " + response.code());
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<RetrofitModel>> call, Throwable t) {
                System.out.println("onFailure " + t);
                mInfoTextView.setText("onFailure " + t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void downloadOneUser(Call<RetrofitModel> call) throws IOException {
        call.enqueue(new Callback<RetrofitModel>() {
            @Override
            public void onResponse(Call<RetrofitModel> call, Response<RetrofitModel> response) {
                if (response.isSuccessful()) {
                    RetrofitModel curRetrofitModel = null;
                        curRetrofitModel = response.body();
                        mInfoTextView.append("\nLogin = " + curRetrofitModel.getLogin() +
                                "\nId = " + curRetrofitModel.getId() +
                                "\nURI" + curRetrofitModel.getAvatarUrl() +
                                "\n-----------------");


                } else {
                    System.out.println("onResponse error: " + response.code());
                    mInfoTextView.setText("onResponse error: " + response.code());
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<RetrofitModel> call, Throwable t) {
                System.out.println("onFailure " + t);
                mInfoTextView.setText("onFailure " + t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }



}
