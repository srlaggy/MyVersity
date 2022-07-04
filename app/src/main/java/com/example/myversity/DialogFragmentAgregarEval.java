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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myversity.db.DbAsignaturas;
import com.example.myversity.db.DbTipoPromedio;
import com.example.myversity.entidades.TipoPromedio;

import java.util.List;

public class DialogFragmentAgregarEval extends androidx.fragment.app.DialogFragment {

    //---- VARIABLES PARA AGREGAR EVALUACIONES ----//
    private Button btn_dialogFragment_agre_eval_cancelar, btn_dialogFragment_agre_eval_confirmar;
    private EditText input_nombre_eval_agregar, input_cant_eval_agregar;
    private List<TipoPromedio> lista_tipoPromedio;
    private TipoPromedio tipoPromedio_seleccionado;

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

        DbTipoPromedio dbTipoPromedio = new DbTipoPromedio(getActivity().getApplicationContext());
        lista_tipoPromedio = dbTipoPromedio.buscarTiposPromedios();
        dbTipoPromedio.close();

        tipoPromedio_seleccionado = null;


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

        // BOTON CANCELAR
        btn_dialogFragment_agre_eval_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        // BOTON CONFIRMAR
        btn_dialogFragment_agre_eval_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_nombre = input_nombre_eval_agregar.getText().toString();
                String input_cant = input_cant_eval_agregar.getText().toString();
                // dropdown.setSelection(0);

                if(!input_nombre.equals("") && !input_cant.equals("") && tipoPromedio_seleccionado != null){
                    // ---- se agrega la evaluación en la asignatura (bd) ----//



                    // ---- se agrega la evaluación en la asignatura (clase) ----//


                    // ---- para imprimir ---- //
                    // Toast toast = Toast.makeText(getActivity().getApplicationContext(),input, Toast.LENGTH_SHORT);
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),input_nombre + input_cant + tipoPromedio_seleccionado.getNombre(), Toast.LENGTH_SHORT);
                    toast.show();

                    // ---- SE CIERRA EL DIALOGO ACTUAL ---- //
                    getDialog().dismiss();

                    // ---- SE PASA AL SIGUIENTE DIALOGO SI TIPO DE PROMEDIO ES MEDIA PONDERADA ---- //
                    if(tipoPromedio_seleccionado.getId() == 2){
                        DialogFragmentAgregarEval2 dialogFragmentAgregarEval2 = new DialogFragmentAgregarEval2();
                        dialogFragmentAgregarEval2.show(getActivity().getSupportFragmentManager(), "DialogFragmentAgregarEval2");
                    }

                    // ---- ACTUALIZAR LA VISTA DE LA ASIGNATURA ---- //
                    Activity activity = getActivity();
                    if (activity instanceof MainActivity){
                        ((MainActivity) activity).replaceFragment(new VistaAsignaturaFragment(), ((MainActivity) activity).getSupportFragmentManager(), R.id.framecentral);
                        activity.setTitle(AsignaturasFragment.getAsignatura_seleccionada().getNombre());
                        ((MainActivity) activity).setFragmentActual(AsignaturasFragment.getAsignatura_seleccionada().getNombre());
                        ((MainActivity) activity).setActionBarActivityArrow(true);
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