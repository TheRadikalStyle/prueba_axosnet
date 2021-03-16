package com.davidochoa.formulario;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.davidochoa.retrofit.RetrofitConstructor;
import com.davidochoa.retrofit.services.RecibosServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormularioViewModel extends ViewModel {
    private MutableLiveData<String> recibosUpdateMutableLiveData;
    private MutableLiveData<String> recibosCreateMutableLiveData;


    public LiveData<String> update(Context context, int id, String provider, float amount, String comment, String emission, String currency){
        if(recibosUpdateMutableLiveData == null){
            recibosUpdateMutableLiveData = new MutableLiveData<>();
        }

        Update(context, id, provider, amount, comment, emission, currency);

        return recibosUpdateMutableLiveData;
    }

    public LiveData<String> create(Context context, String provider, float amount, String comment, String emission, String currency){
        if(recibosCreateMutableLiveData == null){
            recibosCreateMutableLiveData = new MutableLiveData<>();
        }

        Create(context, provider, amount, comment, emission, currency);

        return recibosCreateMutableLiveData;
    }




    private void Update(Context context, int id, String provider, float amount, String comment, String emission, String currency) {
        RecibosServices services = new RetrofitConstructor(context).GetInstance().create(RecibosServices.class);
        services.update(id, provider, amount, comment, emission, currency).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("UpdateVM", String.valueOf(call.request().url()));
                recibosUpdateMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                recibosUpdateMutableLiveData.setValue(t.getMessage());
            }
        });
    }

    private void Create(Context context, String provider, float amount, String comment, String emission, String currency) {
        RecibosServices services = new RetrofitConstructor(context).GetInstance().create(RecibosServices.class);
        services.insert(provider, amount, comment, emission, currency).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("CreateVM", String.valueOf(call.request().url()));
                recibosCreateMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                recibosCreateMutableLiveData.setValue(t.getMessage());
            }
        });
    }
}
