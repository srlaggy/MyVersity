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
import com.example.myversity.db.DbEvaluaciones;
import com.example.myversity.db.DbNotas;
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
                // ---- SE SETEAN LOS VALORES GLOBALES DE AGREGAR EVAL EN VistaAsignaturaFragment ---- //
                VistaAsignaturaFragment.nombre_eval_agregar = "";
                VistaAsignaturaFragment.cant_eval_agregar = "";
                VistaAsignaturaFragment.tipoPromedio_eval_agregar = null;
                VistaAsignaturaFragment.cond_eval_agregar = null;
                VistaAsignaturaFragment.notaCond_eval_agregar = "";

                getDialog().dismiss();
            }
        });

        // BOTON CONFIRMAR
        btn_dialogFragment_agre_eval2_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // debug
                StringBuilder debugNoUsar = new StringBuilder("|");
                for(RowNotasEvaluacion r : rows){
                    debugNoUsar.append(r.getNota().getPeso()).append("|");
                }
                Log.d("ejemplo_ponderacion", debugNoUsar.toString());
                // estados
                // 0 -> esta bien
                // 1 -> campos vacios
                // 2 -> error de formato
                // 3 -> no suman 100
                int estado = revisarRows();
                Log.d("ejemplo_ponderacion", debugNoUsar.toString());
                switch (estado){
                    case 0:
                        // hacer el redirect hacia la siguiente pesta;a
                        // to do esta bien
                        // deberias guardar la `Notas` que esta dentro de `rows`
                        //      Esta contiene una Nota vacia donde el unico valor cambiado es el valor del `Peso`

                        // ---- se definen las variables locales dadas las globales ---- //
                        Integer eval_id_asignaturas = AsignaturasFragment.getAsignatura_seleccionada().getId();
                        String input_nombre = VistaAsignaturaFragment.nombre_eval_agregar;
                        String input_cant = VistaAsignaturaFragment.cant_eval_agregar;
                        TipoPromedio input_tipoPromedio = VistaAsignaturaFragment.tipoPromedio_eval_agregar;
                        Boolean input_cond_eval_agregar = VistaAsignaturaFragment.cond_eval_agregar;
                        String input_nota_min_eval_opcional = VistaAsignaturaFragment.notaCond_eval_agregar;

                        // ---- se agrega la evaluación en la asignatura (bd) ---- //
                        DbEvaluaciones dbEvaluaciones = new DbEvaluaciones(getActivity().getApplicationContext());
                        Long idAuxEval = dbEvaluaciones.insertarEvaluacionesFull(eval_id_asignaturas, input_tipoPromedio.getId(), input_nombre, Integer.parseInt(input_cant), input_cond_eval_agregar, input_nota_min_eval_opcional, null);
                        dbEvaluaciones.close();

                        // ---- se agregan las notas de la evaluación según la cantidad ---- //
                        DbNotas dbNotas = new DbNotas(getActivity().getApplicationContext());
                        for(RowNotasEvaluacion r : rows){
                            Long idAuxNota = dbNotas.insertarNotaFull(idAuxEval.intValue(), false, null, r.getNota().getPeso());
                            Log.d("ejemplo_ponderacion", r.getNota().getPeso());
                        }
                        dbNotas.close();

                        // ---- se agrega la evaluación en la asignatura (clase) ---- //
                        DbAsignaturas dbAsignaturas = new DbAsignaturas(getActivity().getApplicationContext());
                        AsignaturasFragment.setAsignatura_seleccionada(dbAsignaturas.buscarAsignaturaPorId(eval_id_asignaturas));
                        dbAsignaturas.close();

                        // ---- SE SETEAN LOS VALORES GLOBALES DE AGREGAR EVAL EN VistaAsignaturaFragment ---- //
                        VistaAsignaturaFragment.nombre_eval_agregar = "";
                        VistaAsignaturaFragment.cant_eval_agregar = "";
                        VistaAsignaturaFragment.tipoPromedio_eval_agregar = null;
                        VistaAsignaturaFragment.cond_eval_agregar = null;
                        VistaAsignaturaFragment.notaCond_eval_agregar = "";

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