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

public class DialogFragment4 extends androidx.fragment.app.DialogFragment {
    private Button btn_cancelar, btn_guardar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment4,container,false);
        super.onCreateView(inflater, container, savedInstanceState);
        btn_cancelar = view.findViewById(R.id.botonCuatroCancelar);
        btn_guardar = view.findViewById(R.id.botonCuatroGuardar);

        // ---- BOTON CANCELAR ---- //
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        // ---- BOTON GUARDAR ---- //
        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // ---- GUARDAR EN LA BASE DE DATOS LA CONFIGURACIÓN Y LA ASIGNATURA ---- //

                // ---- para imprimir ---- //
                Toast toast = Toast.makeText(getActivity().getApplicationContext(),"LISTO!", Toast.LENGTH_SHORT);
                toast.show();

                // ---- VOLVER A LA LISTA DE ASIGNATURAS ACTUALIZADA ---- //
                AsignaturasFragment.setNombre_Asignatura_ingresada("");
                AsignaturasFragment.setTipoPromedio_ingresada("");
                Activity activity = getActivity();
                if (activity instanceof MainActivity){
                    ((MainActivity) activity).replaceFragment(new AsignaturasFragment(), ((MainActivity) activity).getSupportFragmentManager(), R.id.framecentral);
                }
            }
        });

        return view;
    }

}