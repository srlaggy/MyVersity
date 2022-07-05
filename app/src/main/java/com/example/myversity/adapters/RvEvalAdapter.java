package com.example.myversity.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myversity.R;
import com.example.myversity.entidades.Evaluaciones;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.List;

public class RvEvalAdapter extends RecyclerView.Adapter<RvEvalAdapter.EvalViewHolder> {

    private List<Evaluaciones> evalItemList;
    Context context, ctx;
    View rootView;
    FloatingActionButton BtnGuardar;
    public GridNotasAdapter gridNotasAdapter;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public RvEvalAdapter(Context ct, List<Evaluaciones> evalItemList){
        this.context = ct;
        this.evalItemList = evalItemList;
    }

    @NonNull
    @Override
    public EvalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evaluacion, parent,false);

        ctx = parent.getContext();
        rootView = ((Activity) ctx).getWindow().getDecorView().findViewById(android.R.id.content);
        BtnGuardar = rootView.findViewById(R.id.btn_guardar_notas);

        return new EvalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EvalViewHolder holder, int position) {
        Evaluaciones evaluacion = evalItemList.get(position);
        holder.titleEval.setText(evaluacion.getTipo());

        //TODO: how to handle notas Null
        if(evaluacion.getNota_evaluacion() != null){
            holder.promedioEval.setText(df.format(Float.parseFloat(evaluacion.getNota_evaluacion())));
        }else{
            holder.promedioEval.setText(df.format(evaluacion.getTp().calcularPromedioEvaluaciones(evaluacion,evaluacion.getNotas())));
        }

        if(!evaluacion.getCond() && evaluacion.getNota_cond() != null){
            holder.promedioEval.setBackground(context.getDrawable(R.drawable.roundstyle_error));
        }

        holder.notaRecyclerView.setHasFixedSize(true);
        holder.notaRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        gridNotasAdapter = new GridNotasAdapter(holder.notaRecyclerView.getContext(), evaluacion.getNotas());
        holder.notaRecyclerView.setAdapter(gridNotasAdapter);
    }

    @Override
    public int getItemCount() {
        if(evalItemList != null){
            return evalItemList.size();
        }else{
            return 0;
        }
    }

    public class EvalViewHolder extends RecyclerView.ViewHolder{
        private TextView titleEval, promedioEval;
        private RecyclerView notaRecyclerView;

        public EvalViewHolder(@NonNull View itemView) {
            super(itemView);
            titleEval = itemView.findViewById(R.id.title_evaluacion);
            notaRecyclerView = itemView.findViewById(R.id.grid_notas);
            promedioEval = itemView.findViewById(R.id.eval_promedio);
        }
    }
}
