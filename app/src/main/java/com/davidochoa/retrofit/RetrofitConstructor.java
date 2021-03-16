package com.davidochoa.retrofit;

import android.content.Context;

import com.davidochoa.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConstructor {
    private Context context;
    private Retrofit retrofit;

    public RetrofitConstructor(Context ctx){
        this.context = ctx;
    }

    public Retrofit GetInstance(){
        if(retrofit != null){
            return retrofit;
        }else{
            retrofit = new Retrofit.Builder()
                    .baseUrl(context.getResources().getString(R.string.api_axosnet))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            return retrofit;
        }
    }
}
