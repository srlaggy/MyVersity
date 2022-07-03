package com.example.myversity.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myversity.AsignaturasFragment;
import com.example.myversity.MainActivity;
import com.example.myversity.R;
import com.example.myversity.db.DbEvaluaciones;
import com.example.myversity.db.DbNotas;
import com.example.myversity.entidades.Evaluaciones;
import com.example.myversity.entidades.Notas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class GridNotasAdapter extends RecyclerView.Adapter<GridNotasAdapter.NotaViewHolder> {
    public List<Notas> notaItemList;
    boolean isOnValueChanged = false;
    boolean notasUpdated = false;
    Context context, ctx;
    View rootView;
    public static ArrayList<Notas> notasActualizadas = new ArrayList<Notas>();
    public static ArrayList<Integer> IdNotas = new ArrayList<Integer>();
    public static ArrayList<Long> IdAux = new ArrayList<Long>();
    FloatingActionButton BtnGuardar;
    Evaluaciones currEvaluacion;
    List<Notas> notasActualesPorEval;
    public RecyclerView gridNotas;

    public static class NotaViewHolder extends RecyclerView.ViewHolder{
        public EditText NotaText;

        public NotaViewHolder(@NonNull View itemView) {
            super(itemView);
            NotaText = itemView.findViewById(R.id.editText_nota);
        }
    }
    public GridNotasAdapter(Context ct, List<Notas> notaItemlist){
        this.context = ct;
        this.notaItemList = notaItemlist;
    }

    @NonNull
    @Override
    public GridNotasAdapter.NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota, parent,false);

        ctx = parent.getContext();
        rootView = ((Activity) ctx).getWindow().getDecorView().findViewById(android.R.id.content);
        BtnGuardar = (FloatingActionButton) rootView.findViewById(R.id.btn_guardar_notas);
        /*
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        adapter = new RecyclerViewAdapter(items);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged(); algo así?
         */
        //rvEvalView = (RecyclerView) view.findViewById(R.id.recyclerview_eval);
        //rvEvalView.setAdapter(new RvEvalAdapter(ctx,));
        return new NotaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridNotasAdapter.NotaViewHolder holder, int position) {
        Notas nota = notaItemList.get(position);
        holder.NotaText.setText(nota.getNota());
        EditText NotaText = holder.NotaText;

        NotaText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isOnValueChanged = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isOnValueChanged) {
                    isOnValueChanged = false;

                    try {
                        if(editable.toString().isEmpty()){
                            nota.setNota("Null");
                        }else {
                            nota.setNota(editable.toString());
                        }

                        System.out.println("ID Nota "+nota.getId().toString()+" ID Evaluación "+nota.getId_evaluaciones().toString());
                        System.out.println("Nota "+nota.getNota());

                        if(notasActualizadas.size() > 0){
                            for(int i = 0; i < notasActualizadas.size(); i++){
                                if(IdNotas.contains(nota.getId())){
                                    notasActualizadas.set(IdNotas.indexOf(nota.getId()), nota);
                                    System.out.println("Nota with existing ID replaced in: "+i+", IdNotas: " + IdNotas);
                                }else {
                                    notasActualizadas.add(nota);
                                    IdNotas.add(nota.getId());
                                    System.out.println("New nota added to notasActualizadas in: "+i+", IdNotas: "+IdNotas);
                                }
                                System.out.println("Entry "+i+"in notasActualizadas: "+notasActualizadas.get(i).getId());
                            }
                        }else{
                            notasActualizadas.add(nota);
                            IdNotas.add(nota.getId());
                        }
                        System.out.println("Size Lista notas actualizadas "+ notasActualizadas.size());
                    } catch (NumberFormatException e) {

                    }

                }
            }
        });

        BtnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("List size on click: " + notasActualizadas.size());

                if(!notasActualizadas.isEmpty()){
                    DbNotas dbNotas = new DbNotas(context.getApplicationContext());
                    DbEvaluaciones dbEvaluaciones = new DbEvaluaciones(context.getApplicationContext());

                    for(int j=0; j < notasActualizadas.size(); j++){
                        Long id = dbNotas.actualizarNota(notasActualizadas.get(j).getId(), notasActualizadas.get(j).getNota());
                        IdAux.add(id);
                        //TODO: correct behavior of setNota_evaluación -> currently not storing value (but Null)
                        //get list of all notas for the current EvalID
                        notasActualesPorEval = dbNotas.buscarNotasPorIdEvaluacion(notasActualizadas.get(j).getId_evaluaciones());
                        //get an Evaluaciones item for the current EvalID
                        currEvaluacion = dbEvaluaciones.buscarEvaluacionPorId(notasActualizadas.get(j).getId_evaluaciones());
                        //actualizar promedio para esta evaluación
                        currEvaluacion.setNota_evaluacion(currEvaluacion.getTp().calcularPromedioEvaluaciones(currEvaluacion,notasActualesPorEval).toString());
                        System.out.println("SAVED PROMEDIO: "+ currEvaluacion.getNota_evaluacion());

                        System.out.println("Successfully saved notas: "+id);
                        System.out.println("ID: "+notasActualizadas.get(j).getId());
                        System.out.println("Nota: "+notasActualizadas.get(j).getNota());
                    }
                    if(!IdAux.contains(0)){
                        //TODO: update fragment to update promedio values
                        Toast.makeText(context.getApplicationContext(), "Notas actualizadas!", Toast.LENGTH_LONG).show();
                        /*
                        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
                        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
                        adapter = new RecyclerViewAdapter(items);
                        adapter.setOnItemClickListener(this);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged(); algo así?
                         */
                        holder.getBindingAdapter().notifyDataSetChanged();
                        notasUpdated = true;
                    } else {
                        Toast.makeText(context.getApplicationContext(), "Error al actualizar notas", Toast.LENGTH_LONG).show();
                    }

                    dbNotas.close();
                    dbEvaluaciones.close();
                    notasActualizadas.clear();
                    IdNotas.clear();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        if(notaItemList != null) {
            return notaItemList.size();
        }else{
            return 0;
        }
    }
}