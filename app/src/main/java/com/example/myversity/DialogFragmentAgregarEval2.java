package com.example.myversity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myversity.adapters.RowExportar;
import com.example.myversity.adapters.RowExportarAdapter;
import com.example.myversity.adapters.RowNotasEvaluacion;
import com.example.myversity.adapters.RowNotasEvaluacionAdapter;
import com.example.myversity.db.DbAsignaturas;
import com.example.myversity.db.DbTipoPromedio;
import com.example.myversity.entidades.Asignaturas;
import com.example.myversity.entidades.Notas;
import com.example.myversity.entidades.TipoPromedio;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DialogFragmentAgregarEval2 extends androidx.fragment.app.DialogFragment {
    private Button btn_dialogFragment_agre_eval2_cancelar, btn_dialogFragment_agre_eval2_confirmar;
    private List<RowNotasEvaluacion> rows = new ArrayList<>();
    private ListView listViewPonderaciones;
    private Integer nroNotas = Integer.parseInt(VistaAsignaturaFragment.cant_eval_agregar);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_agregar_eval2,container,false);
        super.onCreateView(inflater, container, savedInstanceState);
        btn_dialogFragment_agre_eval2_cancelar = view.findViewById(R.id.dialogFragment_agre_eval2_cancelar);
        btn_dialogFragment_agre_eval2_confirmar = view.findViewById(R.id.dialogFragment_agre_eval2_confirmar);
        listViewPonderaciones = (ListView) view.findViewById(R.id.lista_ponderaciones_notas_evaluacion);
        if(nroNotas > 0){
            setearRows();
            listViewPonderaciones.setAdapter(new RowNotasEvaluacionAdapter(getActivity().getApplicationContext(), rows));
        }
        if(rows.isEmpty()){
            Toast.makeText(getActivity().getApplicationContext(), "No existen notas", Toast.LENGTH_LONG).show();
        }

        // BOTON CANCELAR
        btn_dialogFragment_agre_eval2_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        // BOTON CONFIRMAR
        btn_dialogFragment_agre_eval2_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aux = "|" + rows.get(0).getValor() + "|" + rows.get(1).getValor() + "|" + rows.get(2).getValor();
                Log.d("ejemplo_ponderacion", aux);
                // estados
                // 0 -> esta bien
                // 1 -> campos vacios
                // 2 -> error de formato
                // 3 -> no suman 100
                int estado = revisarRows();
                switch (estado){
                    case 0:
                        // hacer el redirect hacia la siguiente pesta;a
                        // to do esta bien
                        // deberias guardar la `Notas` que esta dentro de `rows`
                        //      Esta contiene una Nota vacia donde el unico valor cambiado es el valor del `Peso`
                        for(RowNotasEvaluacion r : rows){
                            Log.d("ejemplo_ponderacion", r.getNota().getPeso());
                        }
                        Toast.makeText(getActivity().getApplicationContext(), "LISTO", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        Toast.makeText(getActivity().getApplicationContext(), "Campos vacios", Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        Toast.makeText(getActivity().getApplicationContext(), "Error en el formato", Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        Toast.makeText(getActivity().getApplicationContext(), "Las ponderaciones no suman 100%", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(getActivity().getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });


        return view;
    }

    private void setearRows(){
        RowNotasEvaluacion row = null;
        Notas n = null;
        for(int i=0; i<nroNotas; i++){
            row = new RowNotasEvaluacion();
            row.setTitulo("Nota " + (i+1) + " :");
            row.setValor("");
            row.setNota(new Notas());
            rows.add(row);
        }
    }

    private int revisarRows(){
        // estados
        // 0 -> esta bien
        // 1 -> campos vacios
        // 2 -> error de formato
        // 3 -> no suman 100
        int num, cont = 0;
        for(RowNotasEvaluacion r : rows) {
            if (Objects.equals(r.getValor(), "")) {
                return 1;
            }
            try{
                num = Integer.parseInt(r.getValor());
                cont += num;
            } catch (NumberFormatException e) {
                return 2;
            }
        }
        if(cont != 100){
            return 3;
        }
        return 0;
    }
}