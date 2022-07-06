package com.example.myversity.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myversity.DialogFragmentAgregarEval;
import com.example.myversity.DialogFragmentEditarEval;
import com.example.myversity.DialogFragmentOpcionesEval;
import com.example.myversity.R;
import com.example.myversity.entidades.Evaluaciones;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.List;

public class RvEvalAdapter extends RecyclerView.Adapter<RvEvalAdapter.EvalViewHolder> {

    private List<Evaluaciones> evalItemList;
    Context context, ctx;
    View rootView;
    FloatingActionButton BtnGuardar, BtnEditar;
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

        BtnEditar = view.findViewById(R.id.btn_editar_evaluacion);

        return new EvalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EvalViewHolder holder, int position) {
        Evaluaciones evaluacion = evalItemList.get(position);
        holder.titleEval.setText(evaluacion.getTipo());

        BtnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                DialogFragmentEditarEval dialogFragmentEditarEval = new DialogFragmentEditarEval();

                Bundle args = new Bundle();
                args.putString("name", evaluacion.getTipo());
                args.putInt("id", evaluacion.getId());
                args.putInt("cantidad", evaluacion.getCantidad());
                args.putInt("tipo",(evaluacion.getId_tipoPromedio()));
                if(evaluacion.getCond())args.putString("cond",evaluacion.getNota_cond());
                else args.putString("cond","");
                dialogFragmentEditarEval.setArguments(args);

                //getArguments().getString("name");

                dialogFragmentEditarEval.show(((AppCompatActivity)ctx).getSupportFragmentManager(), "DialogFragmentEditarEval");
                */

                DialogFragmentOpcionesEval dialogFragmentOpcionesEval = new DialogFragmentOpcionesEval();

                Bundle args = new Bundle();
                args.putString("name", evaluacion.getTipo());
                args.putInt("id", evaluacion.getId());
                args.putInt("cantidad", evaluacion.getCantidad());
                args.putInt("tipo",(evaluacion.getId_tipoPromedio()));
                if(evaluacion.getCond())args.putString("cond",evaluacion.getNota_cond());
                else args.putString("cond","");
                dialogFragmentOpcionesEval.setArguments(args);

                dialogFragmentOpcionesEval.show(((AppCompatActivity)ctx).getSupportFragmentManager(), "DialogFragmentOpcionesEval");
            }
        });

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
