package com.example.myversity.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myversity.R;
import com.example.myversity.entidades.Evaluaciones;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class RvEvalAdapter extends RecyclerView.Adapter<RvEvalAdapter.EvalViewHolder> {

    private List<Evaluaciones> evalItemList;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    Context context, ctx;
    View rootView;
    FloatingActionButton BtnGuardar;
    public GridNotasAdapter gridNotasAdapter;
    boolean evalPromedioUpdated = false;

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
        BtnGuardar = (FloatingActionButton) rootView.findViewById(R.id.btn_guardar_notas);

        return new EvalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EvalViewHolder holder, int position) {
        Evaluaciones evaluacion = evalItemList.get(position);
        holder.titleEval.setText(evaluacion.getTipo());

        //TODO: how to handle notas Null
        holder.promedioEval.setText(df.format(evaluacion.getTp().calcularPromedioEvaluaciones(evaluacion,evaluacion.getNotas())).toString());

        holder.notaRecyclerView.setHasFixedSize(true);
        holder.notaRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        gridNotasAdapter = new GridNotasAdapter(holder.notaRecyclerView.getContext(), evaluacion.getNotas());
        holder.notaRecyclerView.setAdapter(gridNotasAdapter);
        gridNotasAdapter.notifyDataSetChanged();

        if(gridNotasAdapter.notasUpdated){
            holder.promedioEval.setText(df.format(evaluacion.getNota_evaluacion()).toString());
            System.out.println("NOTASupdated getNota_evaluacion(): "+evaluacion.getNota_evaluacion());
            System.out.println("PROMEDIO CALCULADO ACA: "+ evaluacion.getTp().calcularPromedioEvaluaciones(evaluacion,evaluacion.getNotas()));
        }

        /*
        BtnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.promedioEval.setText(df.format(evaluacion.getNota_evaluacion()).toString());
            }
        });*/

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
            titleEval = (TextView) itemView.findViewById(R.id.title_evaluacion);
            notaRecyclerView = (RecyclerView) itemView.findViewById(R.id.grid_notas);
            promedioEval = (TextView) itemView.findViewById(R.id.eval_promedio);
        }
    }
}
