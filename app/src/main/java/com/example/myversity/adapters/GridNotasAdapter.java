package com.example.myversity.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myversity.R;
import com.example.myversity.VistaAsignaturaFragment;
import com.example.myversity.db.DbEvaluaciones;
import com.example.myversity.db.DbNotas;
import com.example.myversity.entidades.Evaluaciones;
import com.example.myversity.entidades.Notas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GridNotasAdapter extends RecyclerView.Adapter<GridNotasAdapter.NotaViewHolder> {
    public List<Notas> notaItemList;
    boolean isOnValueChanged = false;
    Context context, ctx;
    View rootView;
    public static ArrayList<Notas> notasActualizadas = new ArrayList<>();
    public static ArrayList<Integer> IdNotas = new ArrayList<>();
    public static ArrayList<Long> IdAux = new ArrayList<>();
    FloatingActionButton BtnGuardar;
    Evaluaciones currEvaluacion;
    public List<Evaluaciones> evaluaciones;
    public RvEvalAdapter adapter = VistaAsignaturaFragment.rvEvalAdapter;

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
        BtnGuardar = rootView.findViewById(R.id.btn_guardar_notas);

        return new NotaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridNotasAdapter.NotaViewHolder holder, int position) {
        Notas nota = notaItemList.get(position);
        holder.NotaText.setText(nota.getNota());
        EditText NotaText = holder.NotaText;
        evaluaciones = VistaAsignaturaFragment.listaEvaluaciones;

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
                        e.toString();
                    }

                }
            }
        });

        BtnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("List size on click: " + notasActualizadas.size());

                if(!notasActualizadas.isEmpty()){
                    DbNotas dbNotas = new DbNotas(ctx.getApplicationContext());
                    DbEvaluaciones dbEvaluaciones = new DbEvaluaciones(ctx.getApplicationContext());

                    for(int j=0; j < notasActualizadas.size(); j++){
                        Long id = dbNotas.actualizarNota(notasActualizadas.get(j).getId(), notasActualizadas.get(j).getNota());
                        IdAux.add(id);
                        //get Evaluaciones item for the current EvalID
                        currEvaluacion = dbEvaluaciones.buscarEvaluacionPorId(notasActualizadas.get(j).getId_evaluaciones());
                        //actualizar promedio para esta evaluación
                        currEvaluacion.setNota_evaluacion(currEvaluacion.getTp().calcularPromedioEvaluaciones(currEvaluacion,currEvaluacion.getNotas()).toString());
                        //actualizar notas en BD
                        Long state = dbEvaluaciones.actualizarNotaEvaluacion(currEvaluacion.getId(), currEvaluacion.getNota_evaluacion());

                        Integer savedIndex = -1;
                        for(int k=0; k<evaluaciones.size(); k++){
                            if(Objects.equals(evaluaciones.get(k).getId(), currEvaluacion.getId())){
                                savedIndex = k;
                                break;
                            }
                        }
                        if(savedIndex != -1){
                            evaluaciones.set(savedIndex, currEvaluacion);
                            System.out.println("IMPRIMIR EVALUACION");
                            System.out.println(evaluaciones);
                        }
                        System.out.println("SAVED PROMEDIO: "+ currEvaluacion.getNota_evaluacion());

                        System.out.println("Successfully saved nota: "+id + " y promedio: "+state);
                        System.out.println("ID: "+notasActualizadas.get(j).getId());
                        System.out.println("Nota: "+notasActualizadas.get(j).getNota());
                    }
                    if(!IdAux.contains(0)){
                        Toast.makeText(context.getApplicationContext(), "Notas actualizadas!", Toast.LENGTH_LONG).show();
                        adapter.notifyDataSetChanged();
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