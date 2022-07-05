package com.example.myversity;

import android.app.Activity;
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
import com.example.myversity.db.DbNotas;
import com.example.myversity.db.DbTipoPromedio;
import com.example.myversity.entidades.TipoPromedio;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class DialogFragmentEditarEval extends androidx.fragment.app.DialogFragment {

    //---- VARIABLES PARA AGREGAR EVALUACIONES ----//
    private Button btn_dialogFragment_editar_eval_cancelar, btn_dialogFragment_editar_eval_confirmar;
    private EditText input_nombre_eval_agregar, input_cant_eval_agregar;
    private List<TipoPromedio> lista_tipoPromedio;
    private TipoPromedio tipoPromedio_seleccionado;
    private TextInputEditText nota_min_eval_opcional;

    //---- VARIABLES PARA AGREGAR CONDICIONES ----//


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_editar_eval,container,false);
        super.onCreateView(inflater, container, savedInstanceState);
        btn_dialogFragment_editar_eval_cancelar = view.findViewById(R.id.dialogFragment_editar_eval_cancelar);
        btn_dialogFragment_editar_eval_confirmar = view.findViewById(R.id.dialogFragment_editar_eval_confirmar);
        input_nombre_eval_agregar = view.findViewById(R.id.nombre_eval_agregar);
        input_nombre_eval_agregar.setText(getArguments().getString("name"));
        nota_min_eval_opcional = (TextInputEditText) view.findViewById(R.id.editText_minimo_nota_evaluacion_opcional);
        nota_min_eval_opcional.setText(getArguments().getString("cond"));

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
        Spinner dropdown = view.findViewById(R.id.spinner_tipo_promedio_editar_eval);
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.adapter_spinner_tipopromedio, listaNombres_tipoPromedio);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        dropdown.setSelection(getArguments().getInt("tipo"));

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
        btn_dialogFragment_editar_eval_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        // ---- BOTON CONFIRMAR ---- //
        btn_dialogFragment_editar_eval_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_nombre = input_nombre_eval_agregar.getText().toString();
                String input_nota_min_eval_opcional = nota_min_eval_opcional.getText().toString();
                Boolean agregarCond = false;


                // Se valida que se ingrese nombre, cantidad y se seleccione un tipo de promedio
                if(!input_nombre.equals("") && tipoPromedio_seleccionado != null){
                    Integer casos_nota_min = 0;

                    // se verifica que no ingresó nota minima
                    if(input_nota_min_eval_opcional.equals("")){
                        casos_nota_min = 1;
                        agregarCond = false;
                        input_nota_min_eval_opcional = null;
                    } // caso contrario: se ingresó una nota mínima
                    else{
                        Integer min_aux = Integer.parseInt(AsignaturasFragment.getAsignatura_seleccionada().getConfig().getMin());
                        Integer max_aux = Integer.parseInt(AsignaturasFragment.getAsignatura_seleccionada().getConfig().getMax());
                        Integer input_nota = Integer.parseInt(input_nota_min_eval_opcional);
                        // se valida que el valor ingresado esté dentro del rango de nota de la asignatura
                        if(input_nota >= min_aux && input_nota <= max_aux) {
                            casos_nota_min = 2;
                            agregarCond = true;
                        }
                    }

                    // si el caso_nota_min es 0: no se ingresó un valor min válido
                    if(casos_nota_min == 0) {
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(),"Nota mínima evaluación fuera de rango", Toast.LENGTH_SHORT);
                        toast.show();
                    } // en caso contrario si o es vacío
                    else {
                        // ---- SE PASA AL SIGUIENTE DIALOGO SI TIPO DE PROMEDIO ES MEDIA PONDERADA ---- //
                        if (tipoPromedio_seleccionado.getId() == 2) {
                            DialogFragmentEditarEval2 dialogFragmentEditarEval2 = new DialogFragmentEditarEval2();

                            Bundle args = new Bundle();
                            args.putString("name", input_nombre);
                            args.putInt("id", getArguments().getInt("id"));
                            args.putInt("cantidad", getArguments().getInt("cantidad"));
                            args.putInt("tipo",tipoPromedio_seleccionado.getId());
                            args.putBoolean("cond",agregarCond);
                            args.putString("notaCond",input_nota_min_eval_opcional);
                            dialogFragmentEditarEval2.setArguments(args);

                            dialogFragmentEditarEval2.show(getActivity().getSupportFragmentManager(), "DialogFragmentEditarEval2");

                            getDialog().dismiss();
                        } else {
                            Integer eval_id_asignaturas = AsignaturasFragment.getAsignatura_seleccionada().getId();
                            Integer eval_id_tipoPromedio = AsignaturasFragment.getAsignatura_seleccionada().getId_tipoPromedio();

                            DbEvaluaciones dbEvaluaciones = new DbEvaluaciones(getActivity().getApplicationContext());
                            dbEvaluaciones.actualizarIdTipoPromedioEvaluacion(getArguments().getInt("id"), tipoPromedio_seleccionado.getId());
                            dbEvaluaciones.actualizarTipoEvaluacion(getArguments().getInt("id"),input_nombre);
                            dbEvaluaciones.actualizarCondicion(getArguments().getInt("id"),agregarCond);
                            dbEvaluaciones.actualizarNotaCond(getArguments().getInt("id"),input_nota_min_eval_opcional);
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