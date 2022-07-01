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

import com.example.myversity.R;
import com.example.myversity.db.DbNotas;
import com.example.myversity.entidades.Notas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class GridNotasAdapter extends RecyclerView.Adapter<GridNotasAdapter.NotaViewHolder> {
    public List<Notas> notaItemList;
    boolean isOnValueChanged = false;
    Context context;
    Context ctx;
    View rootView;
    public static ArrayList<Notas> notasActualizadas = new ArrayList<Notas>();
    public static ArrayList<Integer> IdNotas = new ArrayList<Integer>();
    FloatingActionButton BtnGuardar;

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
                                    //curr_Id = nota.getId();
                                    //pos = IdNotas.indexOf(curr_Id);
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

                    for(int j=0; j < notasActualizadas.size(); j++){
                        Long id = dbNotas.actualizarNota(notasActualizadas.get(j).getId(), notasActualizadas.get(j).getNota());

                        System.out.println("Successfully saved notas: "+id);
                        System.out.println("ID: "+notasActualizadas.get(j).getId());
                        System.out.println("Nota: "+notasActualizadas.get(j).getNota());
                    }
                    dbNotas.close();
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