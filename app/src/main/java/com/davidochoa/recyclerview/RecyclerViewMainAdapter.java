package com.davidochoa.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.davidochoa.R;
import com.davidochoa.detalles.DetallesActivity;
import com.davidochoa.retrofit.models.RecibosModel;

import java.util.List;

public class RecyclerViewMainAdapter extends RecyclerView.Adapter<RecyclerViewMainAdapter.ViewHolder> {
    List<RecibosModel> dataset;
    Context context;

    public RecyclerViewMainAdapter(List<RecibosModel> data, Context ctx){
        this.dataset = data;
        this.context = ctx;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView providerTXV, amountTXV, emissionTXV;
        private CardView hostCardview;

        public ViewHolder(View view) {
            super(view);

            hostCardview = view.findViewById(R.id.recyclerviewmaintemp_cardview_container);
            providerTXV = view.findViewById(R.id.recyclerviewmaintemp_textview_provider);
            amountTXV = view.findViewById(R.id.recyclerviewmaintemp_textview_amount);
            emissionTXV = view.findViewById(R.id.recyclerviewmaintemp_textview_emission);
        }
    }




    @NonNull
    @Override
    public RecyclerViewMainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_main_template, parent, false);

        return new RecyclerViewMainAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewMainAdapter.ViewHolder holder, int position) {
        holder.providerTXV.setText(this.dataset.get(position).getProvider());
        holder.amountTXV.setText(
                String.format(context.getResources().getString(R.string.model_recibos_amount_currency),
                        this.dataset.get(position).getAmount(),
                        this.dataset.get(position).getCurrency_code()
                        ));
        holder.emissionTXV.setText(this.dataset.get(position).getEmission_date());

        holder.hostCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetallesActivity.class);
                intent.putExtra("id", dataset.get(position).getId());
                intent.putExtra("provider", dataset.get(position).getProvider());
                intent.putExtra("amount", dataset.get(position).getAmount());
                intent.putExtra("emission", dataset.get(position).getEmission_date());
                intent.putExtra("comment", dataset.get(position).getComment());
                intent.putExtra("currency", dataset.get(position).getCurrency_code());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.dataset.size();
    }
}
