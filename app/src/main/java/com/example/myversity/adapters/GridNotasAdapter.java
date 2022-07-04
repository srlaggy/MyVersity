package com.example.myversity.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myversity.AsignaturasFragment;
import com.example.myversity.R;
import com.example.myversity.VistaAsignaturaFragment;
import com.example.myversity.db.DbAsignaturas;
import com.example.myversity.db.DbEvaluaciones;
import com.example.myversity.db.DbNotas;
import com.example.myversity.entidades.Asignaturas;
import com.example.myversity.entidades.CondAsignatura;
import com.example.myversity.entidades.Evaluaciones;
import com.example.myversity.entidades.Notas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

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
    TextView notaFinal;
    Evaluaciones currEvaluacion;
    public Asignaturas currAsignatura = AsignaturasFragment.getAsignatura_seleccionada();
    public List<CondAsignatura> listaCondiciones = AsignaturasFragment.getAsignatura_seleccionada().getCa();
    public List<Evaluaciones> evaluaciones;
    public List<String> listaCond = new ArrayList<>();
    public List<Boolean> listaCheck = new ArrayList<>();
    public RvEvalAdapter adapter = VistaAsignaturaFragment.rvEvalAdapter;

    public static class NotaViewHolder extends RecyclerView.ViewHolder{
        public EditText NotaText;
        public TextInputLayout notaField;

        public NotaViewHolder(@NonNull View itemView) {
            super(itemView);
            NotaText = itemView.findViewById(R.id.editText_nota);
            notaField = itemView.findViewById(R.id.nota_field);
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
        notaFinal = rootView.findViewById(R.id.asignatura_promedio);

        for (CondAsignatura c: listaCondiciones){
            if(c.getCondicion()!=null){
                listaCond.add(c.getCondicion());
                listaCheck.add(c.getChequeado());
            }
        }

        return new NotaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridNotasAdapter.NotaViewHolder holder, int position) {
        Notas nota = notaItemList.get(position);
        holder.NotaText.setText(nota.getNota());
        EditText NotaText = holder.NotaText;
        evaluaciones = VistaAsignaturaFragment.listaEvaluaciones;

        if(!nota.getCond() && nota.getNota_cond() != null ){
            holder.notaField.setErrorEnabled(true);
            holder.notaField.setError("insuficiente");
        }

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

                if(!notasActualizadas.isEmpty()){
                    DbNotas dbNotas = new DbNotas(ctx.getApplicationContext());
                    DbEvaluaciones dbEvaluaciones = new DbEvaluaciones(ctx.getApplicationContext());
                    DbAsignaturas dbAsignaturas = new DbAsignaturas((ctx.getApplicationContext()));

                    //dbNotas.actualizarNotaCond(1,"55");
                    //dbNotas.actualizarNotaCond(4,"50");
                    //dbNotas.actualizarNotaCond(6,"75");
                    //dbNotas.actualizarNotaCond(8,"55");
                    //dbEvaluaciones.actualizarNotaCond(0,"55");
                    //dbEvaluaciones.actualizarNotaCond(1,"50");
                    //dbEvaluaciones.actualizarNotaCond(2,"60");
                    //dbEvaluaciones.actualizarNotaCond(3,"55");

                    for(int j=0; j < notasActualizadas.size(); j++){
                        Long id = dbNotas.actualizarNota(notasActualizadas.get(j).getId(), notasActualizadas.get(j).getNota());

                        //actualizar estado de condición en DB comparando la nota actual con el valor requirido
                        if(notasActualizadas.get(j).getNota_cond() != null){
                            Float cond = Float.parseFloat(notasActualizadas.get(j).getNota_cond());
                            Float notaval = Float.parseFloat(notasActualizadas.get(j).getNota());
                            Long stateCond;
                            if(notaval >= cond){
                                stateCond = dbNotas.actualizarCond(notasActualizadas.get(j).getId(), true);
                            }else{
                                stateCond = dbNotas.actualizarCond(notasActualizadas.get(j).getId(), false);
                            }
                            System.out.println("CONDICIÒN NOTA actualizada: " + stateCond);
                        }

                        //get Evaluaciones item for the current EvalID
                        currEvaluacion = dbEvaluaciones.buscarEvaluacionPorId(notasActualizadas.get(j).getId_evaluaciones());
                        //actualizar promedio para esta evaluación
                        currEvaluacion.setNota_evaluacion(currEvaluacion.getTp().calcularPromedioEvaluaciones(currEvaluacion,currEvaluacion.getNotas()).toString());
                        //actualizar notas promedios en DB
                        Long statePromedio = dbEvaluaciones.actualizarNotaEvaluacion(currEvaluacion.getId(), currEvaluacion.getNota_evaluacion());

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

                        //actualizar estado de condición en DB comparando el promedio con el valor requirido
                        if(currEvaluacion.getNota_cond() != null){
                            Float condEval = Float.parseFloat(currEvaluacion.getNota_cond());
                            Float promedioval = Float.parseFloat(currEvaluacion.getNota_evaluacion());
                            Long stateCondEval;
                            if(promedioval >= condEval){
                                stateCondEval = dbEvaluaciones.actualizarCondicion(currEvaluacion.getId(), true);
                            }else{
                                stateCondEval = dbEvaluaciones.actualizarCondicion(currEvaluacion.getId(), false);
                            }
                            System.out.println("CONDICIÒN PROMEDIO actualizada: " + stateCondEval);
                        }

                        //actualizar nota final para asignatura
                        currAsignatura.setNota_final(currAsignatura.getTp().calcularPromedioAsignaturas(evaluaciones).toString());
                        //actualizar nota final en DB
                        Long stateNotaFinal = dbAsignaturas.actualizarNotaAsignatura(currAsignatura.getId(),currAsignatura.getNota_final());

                        notaFinal.setText(currAsignatura.getNota_final());
                        //actualizar color del campo nota final dependiente de condiciones
                        Float notaAsign = Float.parseFloat(currAsignatura.getNota_final());
                        Float notaAprobacion = Float.parseFloat(currAsignatura.getConfig().getNotaAprobacion());
                        if(notaAsign < notaAprobacion){
                            notaFinal.setBackground(ctx.getDrawable(R.drawable.roundstyle_error_final));
                        } else if(!listaCond.isEmpty() && listaCheck.contains(false)){
                            notaFinal.setBackground(ctx.getDrawable(R.drawable.roundstyle_error_final));
                        }else{
                            notaFinal.setBackground(ctx.getDrawable(R.drawable.roundstyle_nota_final));
                        }

                        IdAux.add(id);
                        IdAux.add(statePromedio);
                        IdAux.add(stateNotaFinal);
                        System.out.println("Successfully saved nota: "+id + " y promedio: "+statePromedio + " y nota final: "+stateNotaFinal);
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
                    dbAsignaturas.close();
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