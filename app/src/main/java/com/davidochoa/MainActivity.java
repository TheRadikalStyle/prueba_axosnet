package com.davidochoa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.davidochoa.about.AboutActivity;
import com.davidochoa.detalles.DetallesActivity;
import com.davidochoa.formulario.FormularioActivity;
import com.davidochoa.helpers.Tools;
import com.davidochoa.recyclerview.RecyclerViewMainAdapter;
import com.davidochoa.retrofit.models.RecibosModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayout loadingLayout, emptySetLayout;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Tools.getInstance().isDeviceOnline(this)){
            recyclerView = findViewById(R.id.main_recyclerview_list);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            loadingLayout = findViewById(R.id.main_linearlayout_loading);
            emptySetLayout = findViewById(R.id.main_linearlayout_emptyset);

            mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

            ConfigureFABS();
        }else{
            new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.main_alertdialog_internetconnection_title))
                .setMessage(getResources().getString(R.string.main_alertdialog_internetconnection_message))
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();
        }
    }

    /**
     * Obtener recibo con el ID proporcionado
     * @param id ID de recibo
     * */
    private void ActionGetByID(String id){
        mainViewModel.getByIDRecibos(this, id).observe(this, new Observer<RecibosModel>() {
            @Override
            public void onChanged(RecibosModel recibosModel) {
                if(recibosModel != null){
                    Intent intent = new Intent(MainActivity.this, DetallesActivity.class);
                    intent.putExtra("id", recibosModel.getId());
                    intent.putExtra("provider", recibosModel.getProvider());
                    intent.putExtra("amount", recibosModel.getAmount());
                    intent.putExtra("emission", recibosModel.getEmission_date());
                    intent.putExtra("comment", recibosModel.getComment());
                    intent.putExtra("currency", recibosModel.getCurrency_code());
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.main_actionsearch_dialog_notfound), Toast.LENGTH_SHORT).show();
                }

                mainViewModel.getByIDRecibos(MainActivity.this, id).removeObservers(MainActivity.this); //Remove observer to prevent "n" open activities
            }
        });
    }


    private void ConfigureFABS(){
        FloatingActionButton fab_commander = findViewById(R.id.main_fab_commander);
        FloatingActionButton fab_add = findViewById(R.id.main_fab_add);
        FloatingActionButton fab_search = findViewById(R.id.main_fab_search);

        fab_commander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fab_add.isOrWillBeHidden() && fab_search.isOrWillBeHidden()){
                    fab_commander.setImageDrawable(getResources().getDrawable(R.drawable.ic_close_grey));
                    fab_add.show();
                    fab_search.show();
                }else{
                    fab_commander.setImageDrawable(getResources().getDrawable(R.drawable.ic_plus_grey));
                    fab_add.hide();
                    fab_search.hide();
                }
            }
        });

        fab_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getResources().getString(R.string.main_actionsearch_dialog_title));

                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActionGetByID(input.getText().toString());
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.general_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FormularioActivity.class));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_main_about)
            startActivity(new Intent(this, AboutActivity.class));

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        if(Tools.getInstance().isDeviceOnline(this)){
            mainViewModel.getAllRecibos(this).observe(this, new Observer<List<RecibosModel>>() {
                @Override
                public void onChanged(List<RecibosModel> recibosModels) {
                    if(recibosModels.size() > 0){
                        recyclerView.setAdapter(new RecyclerViewMainAdapter(recibosModels, MainActivity.this));
                        OnAdapterFilled();
                    }else{
                        OnFilledError();
                    }
                }
            });
        }else{
            Toast.makeText(this, getResources().getString(R.string.main_alertdialog_internetconnection_title), Toast.LENGTH_SHORT).show();
        }
        super.onResume();
    }

    /**
     * Adapter RecyclerView lleno, ocultar view de carga y mostrar recycler
     * */
    private void OnAdapterFilled(){
        loadingLayout.setVisibility(View.GONE);
        emptySetLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * Adapter RecyclerView vacío, ocultar view de carga y mostrar alerta
     * */
    private void OnFilledError(){
        loadingLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        emptySetLayout.setVisibility(View.VISIBLE);
    }
}