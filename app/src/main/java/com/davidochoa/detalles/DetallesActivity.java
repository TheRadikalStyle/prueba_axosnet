package com.davidochoa.detalles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.davidochoa.R;
import com.davidochoa.formulario.FormularioActivity;

public class DetallesActivity extends AppCompatActivity implements View.OnClickListener {
    private DetallesViewModel detallesViewModel;
    private int id;
    private String provider;
    private float amount;
    private String emission;
    private String comment;
    private String currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        if(getIntent().getExtras() != null){
            if(getSupportActionBar() != null)
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            TextView idTXV = findViewById(R.id.activity_detalles_textview_id);
            TextView providerTXV = findViewById(R.id.activity_detalles_textview_provider);
            TextView amountTXV = findViewById(R.id.activity_detalles_textview_amount);
            TextView emissionTXV = findViewById(R.id.activity_detalles_textview_emission);
            TextView commentTXV = findViewById(R.id.activity_detalles_textview_comment);

            id = getIntent().getIntExtra("id", 0);
            provider = getIntent().getStringExtra("provider");
            amount = getIntent().getFloatExtra("amount", 0);
            currency = getIntent().getStringExtra("currency");
            emission = getIntent().getStringExtra("emission");
            comment = getIntent().getStringExtra("comment");

            idTXV.setText(String.valueOf(id));
            providerTXV.setText(provider);
            amountTXV.setText(String.format(getResources().getString(R.string.model_recibos_amount_currency), amount, currency));
            emissionTXV.setText(emission);
            commentTXV.setText(comment);


            Button deleteBtn = findViewById(R.id.detalles_button_delete);
            Button editBtn = findViewById(R.id.detalles_button_edit);
            deleteBtn.setOnClickListener(this);
            editBtn.setOnClickListener(this);

            detallesViewModel = new ViewModelProvider(this).get(DetallesViewModel.class);
        }
    }

    private void OperationDelete(){
        detallesViewModel.deleteById(this, id).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                finish();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.detalles_button_delete){
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.detalles_operaciones_delete))
                    .setMessage(getResources().getString(R.string.detalles_operaciones_delete_message))
                    .setPositiveButton(getResources().getString(R.string.general_yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            OperationDelete();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.general_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
        }

        if(id == R.id.detalles_button_edit){
            Intent intent = new Intent(this, FormularioActivity.class);
            intent.putExtra("id", this.id);
            intent.putExtra("provider", provider);
            intent.putExtra("amount", amount);
            intent.putExtra("currency", currency);
            intent.putExtra("emission", emission);
            intent.putExtra("comment", comment);
            startActivity(intent);
            finish();
        }
    }
}