package com.example.myversity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myversity.db.DbAsignaturas;
import com.example.myversity.db.DbEvaluaciones;
import com.example.myversity.db.DbHelper;
import com.example.myversity.db.DbNotas;
import com.example.myversity.db.DbTipoPromedio;
import com.example.myversity.entidades.Asignaturas;
import com.example.myversity.entidades.Evaluaciones;
import com.example.myversity.entidades.TipoPromedio;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class DialogFragmentAgregarEval extends androidx.fragment.app.DialogFragment {

    //---- VARIABLES PARA AGREGAR EVALUACIONES ----//
    private Button btn_dialogFragment_agre_eval_cancelar, btn_dialogFragment_agre_eval_confirmar;
    private EditText input_nombre_eval_agregar, input_cant_eval_agregar;
    private List<TipoPromedio> lista_tipoPromedio;
    private TipoPromedio tipoPromedio_seleccionado;
    private TextInputEditText nota_min_eval_opcional;

    //---- VARIABLES PARA AGREGAR CONDICIONES ----//


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_agregar_eval,container,false);
        super.onCreateView(inflater, container, savedInstanceState);
        btn_dialogFragment_agre_eval_cancelar = view.findViewById(R.id.dialogFragment_agre_eval_cancelar);
        btn_dialogFragment_agre_eval_confirmar = view.findViewById(R.id.dialogFragment_agre_eval_confirmar);
        input_nombre_eval_agregar = view.findViewById(R.id.nombre_eval_agregar);
        input_cant_eval_agregar = view.findViewById(R.id.cant_eval_agregar);
        nota_min_eval_opcional = (TextInputEditText) view.findViewById(R.id.editText_minimo_nota_evaluacion_opcional);

        DbTipoPromedio dbTipoPromedio = new DbTipoPromedio(getActivity().getApplicationContext());
        lista_tipoPromedio = dbTipoPromedio.buscarTiposPromedios();
        dbTipoPromedio.close();

        tipoPromedio_seleccionado = null;

        // ---- SPIDER ---- //
        //create a list of items for the spinner.
        String[] listaNombres_tipoPromedio = new String[lista_tipoPromedio.size()+1];
        Integer count = 1;
        for (TipoPromedio t: lista_tipoPromedio) {
            listaNombres_tipoPromedio[count] = t.getNombre();
            count ++;
        }
        listaNombres_tipoPromedio[0] = "Seleccionar";

        //get the spinner from the xml.
        Spinner dropdown = view.findViewById(R.id.spinner_tipo_promedio_agregar_eval);
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.adapter_spinner_tipopromedio, listaNombres_tipoPromedio);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        // SELECCIONAR UNA OPCIÓN DEL SPINNER
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(position != 0){
                    tipoPromedio_seleccionado = lista_tipoPromedio.get(position-1);
                }
            } // to close the onItemSelected
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        // ---- BOTON CANCELAR ---- //
        btn_dialogFragment_agre_eval_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VistaAsignaturaFragment.nombre_eval_agregar = "";
                VistaAsignaturaFragment.cant_eval_agregar = "";
                VistaAsignaturaFragment.tipoPromedio_eval_agregar = null;
                VistaAsignaturaFragment.cond_eval_agregar = null;
                VistaAsignaturaFragment.notaCond_eval_agregar = "";
                getDialog().dismiss();
            }
        });

        // ---- BOTON CONFIRMAR ---- //
        btn_dialogFragment_agre_eval_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_nombre = input_nombre_eval_agregar.getText().toString();
                String input_cant = input_cant_eval_agregar.getText().toString();
                String input_nota_min_eval_opcional = nota_min_eval_opcional.getText().toString();

                // Se valida que se ingrese nombre, cantidad y se seleccione un tipo de promedio
                if(!input_nombre.equals("") && !input_cant.equals("") && tipoPromedio_seleccionado != null){
                    Integer casos_nota_min = 0;

                    // la cantidad de notas debe ser mayor a 0
                    if(Integer.parseInt(input_cant) != 0){
                        // se verifica que no ingresó nota minima
                        if(input_nota_min_eval_opcional.equals("")){
                            casos_nota_min = 1;
                            VistaAsignaturaFragment.cond_eval_agregar = false;
                            input_nota_min_eval_opcional = null;
                        } // caso contrario: se ingresó una nota mínima
                        else{
                            Integer min_aux = Integer.parseInt(AsignaturasFragment.getAsignatura_seleccionada().getConfig().getMin());
                            Integer max_aux = Integer.parseInt(AsignaturasFragment.getAsignatura_seleccionada().getConfig().getMax());
                            Integer input_nota = Integer.parseInt(input_nota_min_eval_opcional);

                                // se valida que el valor ingresado esté dentro del rango de nota de la asignatura
                                if(input_nota >= min_aux && input_nota <= max_aux) {
                                    casos_nota_min = 2;
                                    VistaAsignaturaFragment.cond_eval_agregar = true;
                                    VistaAsignaturaFragment.notaCond_eval_agregar = input_nota_min_eval_opcional;
                                }

                        }
                    }
                    else{
                        casos_nota_min = 3;
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(),"La cantidad de notas debe ser mayor a 0", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    // si el caso_nota_min es 0: no se ingresó un valor min válido
                    if(casos_nota_min == 0) {
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(),"Nota mínima evaluación fuera de rango", Toast.LENGTH_SHORT);
                        toast.show();
                    } // en caso contrario si o es vacío
                    else if (casos_nota_min == 1 | casos_nota_min == 2){
                        // ---- setear variables globales para agregar evaluación ---- //
                        VistaAsignaturaFragment.nombre_eval_agregar = input_nombre;
                        VistaAsignaturaFragment.cant_eval_agregar = input_cant;
                        VistaAsignaturaFragment.tipoPromedio_eval_agregar = tipoPromedio_seleccionado;

                        // ---- SE PASA AL SIGUIENTE DIALOGO SI TIPO DE PROMEDIO ES MEDIA PONDERADA ---- //
                        if (tipoPromedio_seleccionado.getId() == 2) {
                            DialogFragmentAgregarEval2 dialogFragmentAgregarEval2 = new DialogFragmentAgregarEval2();
                            dialogFragmentAgregarEval2.show(getActivity().getSupportFragmentManager(), "DialogFragmentAgregarEval2");

                            getDialog().dismiss();
                        } else {
                            Integer eval_id_asignaturas = AsignaturasFragment.getAsignatura_seleccionada().getId();
                            TipoPromedio input_tipoPromedio = VistaAsignaturaFragment.tipoPromedio_eval_agregar;
                            Boolean input_cond_eval_agregar = VistaAsignaturaFragment.cond_eval_agregar;

                            // ---- se agrega la evaluación en la asignatura (bd) ---- //
                            DbEvaluaciones dbEvaluaciones = new DbEvaluaciones(getActivity().getApplicationContext());
                            Long idAuxEval = dbEvaluaciones.insertarEvaluacionesFull(eval_id_asignaturas, input_tipoPromedio.getId(), input_nombre, Integer.parseInt(input_cant), input_cond_eval_agregar, input_nota_min_eval_opcional, null);
                            dbEvaluaciones.close();

                            // ---- se agregan las notas de la evaluación según la cantidad ---- //
                            for (int i=1; i<=Integer.parseInt(input_cant); i++){
                                DbNotas dbNotas = new DbNotas(getActivity().getApplicationContext());
                                Long idAuxNota = dbNotas.insertarNotaFull (idAuxEval.intValue(), false, null, null);
                                dbNotas.close();
                            }

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

                        }
                    }
                }
                else{
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),"Campos vacíos", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });


        return view;
    }

}