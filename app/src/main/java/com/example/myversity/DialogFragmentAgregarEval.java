package com.example.myversity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DialogFragmentAgregarEval extends androidx.fragment.app.DialogFragment {

    //---- VARIABLES PARA AGREGAR EVALUACIONES ----//
    private Button btn_dialogFragment_agre_eval_cancelar, btn_dialogFragment_agre_eval_confirmar;
    private EditText input_nombre_eval_agregar, input_cant_eval_agregar;

    //---- VARIABLES PARA AGREGAR CONDICIONES ----//


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_agregar_eval,container,false);
        super.onCreateView(inflater, container, savedInstanceState);
        btn_dialogFragment_agre_eval_cancelar = view.findViewById(R.id.dialogFragment_agre_eval_cancelar);
        btn_dialogFragment_agre_eval_confirmar = view.findViewById(R.id.dialogFragment_agre_eval_confirmar);
        input_nombre_eval_agregar = view.findViewById(R.id.nombre_asignatura_dialog);
        input_cant_eval_agregar = view.findViewById(R.id.cant_eval_agregar);


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
                if(!input_nombre.equals("") && !input_cant.equals("")){
                    // ---- se agrega la evaluación en la asignatura (clase) ----//


                    // ---- se agrega la evaluación en la asignatura (bd) ----//


                    // ---- para imprimir ---- //
                    // Toast toast = Toast.makeText(getActivity().getApplicationContext(),input, Toast.LENGTH_SHORT);
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),input_nombre + input_cant, Toast.LENGTH_SHORT);
                    toast.show();

                    // ---- SE CIERRA EL DIALOGO ACTUAL ---- //
                    getDialog().dismiss();

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
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),"Ingrese nombre y/o cantidad", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });


        return view;
    }

}