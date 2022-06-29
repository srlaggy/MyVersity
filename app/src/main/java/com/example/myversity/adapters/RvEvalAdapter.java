package com.example.myversity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myversity.R;

import java.util.ArrayList;
import java.util.List;

public class RvEvalAdapter extends RecyclerView.Adapter<RvEvalAdapter.EvalViewHolder> {

    List<String> nombreEvaluaciones = new ArrayList<>();
    List<String> cantidadEvaluaciones = new ArrayList<>();
    Context context;

    public RvEvalAdapter(Context ct, List<String> nombreEval, List<String> cantidadEval){
        context = ct;
        nombreEvaluaciones = nombreEval;
        cantidadEvaluaciones = cantidadEval;
    }

    @NonNull
    @Override
    public EvalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evaluacion, parent,false);
        return new EvalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EvalViewHolder holder, int position) {
        holder.titleEval.setText(nombreEvaluaciones.get(position));
        //holder.nota_1.setText("80");
        //holder.nota_2.setText("55");
        //holder.nota_3.setText("75");
    }

    @Override
    public int getItemCount() {
        return nombreEvaluaciones.size();
    }

    public class EvalViewHolder extends RecyclerView.ViewHolder{
        TextView titleEval, nota1;
        //com.google.android.material.textfield.TextInputEditText nota_1, nota_2, nota_3;

        public EvalViewHolder(@NonNull View itemView) {
            super(itemView);
            titleEval = (TextView) itemView.findViewById(R.id.title_evaluacion);
            //nota_1 = itemView.findViewById(R.id.nota_dummy1);
            //nota_2 = itemView.findViewById(R.id.nota_dummy2);
            //nota_3 = itemView.findViewById(R.id.nota_dummy3);
        }
    }
}
