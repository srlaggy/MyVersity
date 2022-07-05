package com.example.myversity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myversity.adapters.RowNotasEvaluacion;
import com.example.myversity.adapters.RowNotasEvaluacionAdapter;
import com.example.myversity.db.DbAsignaturas;
import com.example.myversity.db.DbEvaluaciones;
import com.example.myversity.db.DbNotas;
import com.example.myversity.entidades.Notas;
import com.example.myversity.entidades.TipoPromedio;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DialogFragmentEditarEval2 extends androidx.fragment.app.DialogFragment {
    private Button btn_dialogFragment_agre_eval2_cancelar, btn_dialogFragment_agre_eval2_confirmar;
    private List<RowNotasEvaluacion> rows = new ArrayList<>();
    private ListView listViewPonderaciones;
    private Integer nroNotas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_agregar_eval2,container,false);
        super.onCreateView(inflater, container, savedInstanceState);
        btn_dialogFragment_agre_eval2_cancelar = view.findViewById(R.id.dialogFragment_agre_eval2_cancelar);
        btn_dialogFragment_agre_eval2_confirmar = view.findViewById(R.id.dialogFragment_agre_eval2_confirmar);
        listViewPonderaciones = (ListView) view.findViewById(R.id.lista_ponderaciones_notas_evaluacion);
        nroNotas = getArguments().getInt("cantidad");
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

                        Integer eval_id_asignaturas = AsignaturasFragment.getAsignatura_seleccionada().getId();

                        // ---- se agrega la evaluación en la asignatura (bd) ---- //
                        DbEvaluaciones dbEvaluaciones = new DbEvaluaciones(getActivity().getApplicationContext());
                        dbEvaluaciones.actualizarIdTipoPromedioEvaluacion(getArguments().getInt("id"), getArguments().getInt("tipo"));
                        dbEvaluaciones.actualizarTipoEvaluacion(getArguments().getInt("id"),getArguments().getString("name"));
                        dbEvaluaciones.actualizarCondicion(getArguments().getInt("id"),getArguments().getBoolean("cond"));
                        dbEvaluaciones.actualizarNotaCond(getArguments().getInt("id"),getArguments().getString("notaCond"));
                        dbEvaluaciones.close();
                        dbEvaluaciones.close();

                        // ---- se agrega la evaluación en la asignatura (clase) ---- //
                        DbAsignaturas dbAsignaturas = new DbAsignaturas(getActivity().getApplicationContext());
                        AsignaturasFragment.setAsignatura_seleccionada(dbAsignaturas.buscarAsignaturaPorId(eval_id_asignaturas));
                        dbAsignaturas.close();

                        // ---- SE CIERRA EL DIALOGO ACTUAL ---- //
                        getDialog().dismiss();

                        // ---- ACTUALIZAR LA VISTA DE LA ASIGNATURA ---- //
                        Activity activity = getActivity();
                        if (activity instanceof MainActivity) {
                            ((MainActivity) activity).replaceFragment(new VistaAsignaturaFragment(), ((MainActivity) activity).getSupportFragmentManager(), R.id.framecentral);
                            activity.setTitle(AsignaturasFragment.getAsignatura_seleccionada().getNombre());
                            ((MainActivity) activity).setFragmentActual(AsignaturasFragment.getAsignatura_seleccionada().getNombre());
                            ((MainActivity) activity).setActionBarActivityArrow(true);
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