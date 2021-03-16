package com.davidochoa.retrofit.models;

public class RecibosModel {
    private int id;
    private String provider;
    private float amount;
    private String emission_date;
    private String comment;
    private String currency_code;


    public RecibosModel(int id, String provider, float amount, String emission, String comment, String currency){
        this.id = id;
        this.provider = provider;
        this.amount = amount;
        this.emission_date = emission;
        this.comment = comment;
        this.currency_code = currency;
    }

    public int getId() { return id; }
    public String getProvider() { return provider; }
    public float getAmount() { return amount; }
    public String getEmission_date() { return emission_date; }
    public String getComment() { return comment; }
    public String getCurrency_code() { return currency_code; }
}
