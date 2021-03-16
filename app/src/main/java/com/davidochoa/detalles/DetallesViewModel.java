package com.davidochoa.detalles;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.davidochoa.retrofit.RetrofitConstructor;
import com.davidochoa.retrofit.models.RecibosModel;
import com.davidochoa.retrofit.services.RecibosServices;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetallesViewModel extends ViewModel {
    private MutableLiveData<String> recibosDeleteMutableLiveData;


    public LiveData<String> deleteById(Context context, int id){
        if(recibosDeleteMutableLiveData == null){
            recibosDeleteMutableLiveData = new MutableLiveData<>();
        }

        Delete(context, id);

        return recibosDeleteMutableLiveData;
    }

    private void Delete(Context context, int id) {
        RecibosServices services = new RetrofitConstructor(context).GetInstance().create(RecibosServices.class);
        services.deleteByID(id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                recibosDeleteMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                recibosDeleteMutableLiveData.setValue(t.getMessage());
            }
        });
    }
}
