package com.example.examenfinal.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examenfinal.DetalleMovimientoActivity2;
import com.example.examenfinal.R;
import com.example.examenfinal.entities.Movimiento;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovimientoAdapter extends RecyclerView.Adapter {

    List<Movimiento> data;

    public MovimientoAdapter(List<Movimiento> data){
        this.data = data;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_movimiento,parent, false);
        return new MovimientoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Movimiento movimiento = data.get(position);


        ImageView ivBoucher = holder.itemView.findViewById(R.id.ivBoucher);
        Picasso.get().load(data.get(position).imagenURL).into(ivBoucher);

        TextView tvTipoMovimiento = holder.itemView.findViewById(R.id.tvTipoMovimiento);
        tvTipoMovimiento.setText(data.get(position).tipo);

        TextView tvMonto = holder.itemView.findViewById(R.id.tvMonto);
        tvMonto.setText(data.get(position).monto.toString());

        TextView tvMotivo = holder.itemView.findViewById(R.id.tvMotivo);
        tvMotivo.setText(data.get(position).motivo);

        TextView tvLatitud = holder.itemView.findViewById(R.id.tvLatitud);
        tvLatitud.setText(data.get(position).latitud.toString());

        TextView tvLongitud = holder.itemView.findViewById(R.id.tvLongitud);
        tvLongitud.setText(data.get(position).longitud.toString());

        ivBoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), DetalleMovimientoActivity2.class);
                intent.putExtra("DATOS_MOVIMIENTO", new Gson().toJson(movimiento));
                holder.itemView.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    class MovimientoViewHolder extends RecyclerView.ViewHolder{

        public MovimientoViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
