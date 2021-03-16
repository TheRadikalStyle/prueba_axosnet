package com.davidochoa.formulario;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.davidochoa.R;
import com.davidochoa.helpers.DatePickerHelperActivity;
import com.davidochoa.helpers.Tools;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class FormularioActivity extends AppCompatActivity implements View.OnClickListener {
    private int mode;
    private TextInputLayout providerTIL, amountTIL, emissionTIL, commentTIL, currencyTIL;
    private TextInputEditText providerEDTX, amountEDTX, emissionEDTX, commentEDTX;
    private Button morphButton;
    private int id;
    private String provider;
    private float amount;
    private String emission;
    private String comment;
    private String currency;
    private FormularioViewModel formularioViewModel;
    private AutoCompleteTextView currencyAUTOTXV;

    private static final int DATEPICKERHELPER_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getIntent().getExtras() != null){
            mode = 1; //Mode -> 0 new, 1 edit
            id = getIntent().getIntExtra("id", 0);
            provider = getIntent().getStringExtra("provider");
            amount = getIntent().getFloatExtra("amount", 0);
            currency = getIntent().getStringExtra("currency");
            emission = getIntent().getStringExtra("emission");
            comment = getIntent().getStringExtra("comment");

            ConfigView();
            SetEditElements();
        }else{
            mode = 0;
            ConfigView();
        }
    }


    private void ConfigView(){
        providerTIL = findViewById(R.id.formulario_textinputlayout_provider);
        amountTIL = findViewById(R.id.formulario_textinputlayout_amount);
        emissionTIL = findViewById(R.id.formulario_textinputlayout_emission);
        commentTIL = findViewById(R.id.formulario_textinputlayout_comment);
        currencyTIL = findViewById(R.id.formulario_textinputlayout_currency);

        providerEDTX = findViewById(R.id.formulario_edittext_provider);
        amountEDTX = findViewById(R.id.formulario_edittext_amount);
        commentEDTX = findViewById(R.id.formulario_edittext_comment);

        morphButton = findViewById(R.id.formulario_button_morph);
        morphButton.setOnClickListener(this);
        if(mode == 0){
            morphButton.setText(getResources().getString(R.string.formulario_button_save));
        }else if(mode == 1){
            morphButton.setText(getResources().getString(R.string.formulario_button_edit));
        }

        currencyAUTOTXV = findViewById(R.id.formulario_autocompletetxv_currency);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.currencies));
        currencyAUTOTXV.setAdapter(arrayAdapter);

        emissionEDTX = findViewById(R.id.formulario_edittext_emission);
        emissionEDTX.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(FormularioActivity.this);
                        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                emissionEDTX.setText(Tools.getInstance().DateEquality(year, month, dayOfMonth));
                            }
                        });
                        datePickerDialog.show();
                    }else {
                        startActivityForResult(new Intent(FormularioActivity.this, DatePickerHelperActivity.class), DATEPICKERHELPER_REQUEST_CODE);
                    }
                }
            }
        });

        formularioViewModel = new ViewModelProvider(this).get(FormularioViewModel.class);
    }

    private void SetEditElements(){
        providerEDTX.setText(provider);
        amountEDTX.setText(String.valueOf(amount));
        emissionEDTX.setText(emission);
        currencyAUTOTXV.setText(currency);
        commentEDTX.setText(comment);
    }

    private Boolean Validate(){
        boolean errorDetected = false;
        if(mode == 1 && id == 0){
            errorDetected = true;
            Toast.makeText(this, getResources().getString(R.string.formulario_init_error), Toast.LENGTH_SHORT).show();
            finish();
        }
        if( TextUtils.isEmpty(providerEDTX.getText()) ){ providerTIL.setError(getResources().getString(R.string.formulario_recibos_provider_empty));  errorDetected = true; }else { provider = String.valueOf(providerEDTX.getText()); }
        if( TextUtils.isEmpty(emissionEDTX.getText()) ){ emissionTIL.setError(getResources().getString(R.string.formulario_recibos_emission_empty));  errorDetected = true; }else { emission = String.valueOf(emissionEDTX.getText()); }
        if( TextUtils.isEmpty(commentEDTX.getText()) ){ commentTIL.setError(getResources().getString(R.string.formulario_recibos_comment_empty));  errorDetected = true; }else { comment = String.valueOf(commentEDTX.getText()); }
        if( TextUtils.isEmpty(currencyAUTOTXV.getText()) ){ currencyTIL.setError(getResources().getString(R.string.formulario_recibos_currency_empty));  errorDetected = true; }else { currency = String.valueOf(currencyAUTOTXV.getText()); }
        if(TextUtils.isEmpty(amountEDTX.getText())) { amountEDTX.setError(getResources().getString(R.string.formulario_recibos_amount_empty)); errorDetected = true; }else{ amount = Float.parseFloat(String.valueOf(amountEDTX.getText())); }

        return !errorDetected;
    }


    private void Operation(){
        if(mode == 0){
            //Save
            formularioViewModel.create(this, provider,amount, comment, emission, currency).observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    finish();
                }
            });
        }else if(mode == 1){
            //update
            formularioViewModel.update(this, id, provider,amount, comment, emission, currency).observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    finish();
                }
            });
        }
    }

    //If android lower than 7 (Nougat) will request activity result for datepicker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == DATEPICKERHELPER_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                if(data != null){
                    int year  = data.getIntExtra("year", 0);
                    int month  = data.getIntExtra("month", 0);
                    int day  = data.getIntExtra("day", 0);

                    emissionEDTX.setText(Tools.getInstance().DateEquality(year, month, day));
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.formulario_button_morph){
            if(Validate())
                Operation();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}