package com.davidochoa;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.davidochoa.retrofit.RetrofitConstructor;
import com.davidochoa.retrofit.models.RecibosModel;
import com.davidochoa.retrofit.services.RecibosServices;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {
    private MutableLiveData<List<RecibosModel>> recibosGetAllMutableLiveData;
    private MutableLiveData<RecibosModel> recibosGetByIDMutableLiveData;

    public LiveData<List<RecibosModel>> getAllRecibos(Context context){
        if(recibosGetAllMutableLiveData == null){
            recibosGetAllMutableLiveData = new MutableLiveData<>();
        }

        GetAll(context);

        return recibosGetAllMutableLiveData;
    }

    public LiveData<RecibosModel> getByIDRecibos(Context context, String ID){
        if(recibosGetByIDMutableLiveData == null){
            recibosGetByIDMutableLiveData = new MutableLiveData<>();
        }

        GetByID(context, ID);

        return recibosGetByIDMutableLiveData;
    }







/**
 * Obtener todos los recibos
 * */
    private void GetAll(Context ctx){
        List<RecibosModel> listRecibos = new ArrayList<>();

        RecibosServices services = new RetrofitConstructor(ctx).GetInstance().create(RecibosServices.class);
        services.GetAll().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                RecibosModel rModel;
                JsonArray jsonArray = new JsonParser().parse(response.body()).getAsJsonArray();
                for (JsonElement element: jsonArray) {
                    JsonObject object = element.getAsJsonObject();

                    String provider;
                    try{
                        provider = object.get("provider").getAsString();
                    }catch (Exception ex){
                        provider = "N/A";
                    }

                    String emissionDate;
                    try{
                        emissionDate = object.get("emission_date").getAsString();
                    }catch (Exception ex){
                        emissionDate = "N/A";
                    }

                    String comment;
                    try{
                        comment = object.get("comment").getAsString();
                    }catch (Exception ex){
                        comment = "N/A";
                    }


                    String currency;
                    try{
                        currency = object.get("currency_code").getAsString();
                    }catch (Exception ex){
                        currency = "N/A";
                    }

                    float amount;
                    try{
                        amount = object.get("amount").getAsFloat();
                    }catch (Exception ex){
                        amount = 0;
                    }


                    rModel = new RecibosModel(
                            object.get("id").getAsInt(),
                            provider,
                            amount,
                            emissionDate,
                            comment,
                            currency
                    );

                    listRecibos.add(rModel);
                }

                recibosGetAllMutableLiveData.setValue(listRecibos);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                recibosGetAllMutableLiveData.setValue(listRecibos);
            }
        });
    }


    /**
     * Obtener recibo con el ID proporcionado
     * @param ID ID de recibo
     * */
    private void GetByID(Context ctx, String ID) {
        RecibosServices services = new RetrofitConstructor(ctx).GetInstance().create(RecibosServices.class);
        services.getByID(Integer.parseInt(ID)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("Respondiendo ->", String.valueOf(response.body().length()));
                Log.d("Respondiendo ->", String.valueOf(call.request().url()));
                if(response.body() == null || response.body().length() <= 2){ //API return empty set with []... lenght = 2
                    recibosGetByIDMutableLiveData.setValue(null);
                }else{
                    RecibosModel rModel;
                    JsonArray jsonArray = new JsonParser().parse(response.body()).getAsJsonArray();

                    JsonObject object = jsonArray.get(0).getAsJsonObject();

                    String provider;
                    try{
                        provider = object.get("provider").getAsString();
                    }catch (Exception ex){
                        provider = "N/A";
                    }

                    String emissionDate;
                    try{
                        emissionDate = object.get("emission_date").getAsString();
                    }catch (Exception ex){
                        emissionDate = "N/A";
                    }

                    String comment;
                    try{
                        comment = object.get("comment").getAsString();
                    }catch (Exception ex){
                        comment = "N/A";
                    }


                    String currency;
                    try{
                        currency = object.get("currency_code").getAsString();
                    }catch (Exception ex){
                        currency = "N/A";
                    }

                    float amount;
                    try{
                        amount = object.get("amount").getAsFloat();
                    }catch (Exception ex){
                        amount = 0;
                    }


                    rModel = new RecibosModel(
                            object.get("id").getAsInt(),
                            provider,
                            amount,
                            emissionDate,
                            comment,
                            currency
                    );

                    recibosGetByIDMutableLiveData.setValue(rModel);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("response fail ->", t.toString());
                Log.d("response fail ->", String.valueOf(call.request().url()));
                recibosGetByIDMutableLiveData.setValue(null);
            }
        });
    }
}
