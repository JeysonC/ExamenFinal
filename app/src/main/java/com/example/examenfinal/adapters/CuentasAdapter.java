package com.example.examenfinal.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examenfinal.CuentaDetalleActivity;
import com.example.examenfinal.R;
import com.example.examenfinal.entities.Cuenta;
import com.google.gson.Gson;

import java.util.List;

public class CuentasAdapter extends RecyclerView.Adapter {

    List<Cuenta> data;

    public CuentasAdapter(List<Cuenta> data){
        this.data=data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_cuenta,parent,false);

        return new CuentasViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Cuenta cuenta = data.get(position);

        TextView tvNombreCuenta = holder.itemView.findViewById(R.id.tvNombreCuenta);
        tvNombreCuenta.setText(data.get(position).nombre);

        tvNombreCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), CuentaDetalleActivity.class);
                intent.putExtra("CUENTA_NOMBRE", new Gson().toJson(cuenta));
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class CuentasViewHolder extends RecyclerView.ViewHolder{

        public CuentasViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
