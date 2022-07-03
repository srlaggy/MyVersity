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

import com.example.myversity.db.DbAsignaturas;

public class DialogFragment3 extends androidx.fragment.app.DialogFragment {
    private Button btn_modificar, btn_si, btn_cancelar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment3,container,false);
        super.onCreateView(inflater, container, savedInstanceState);
        btn_modificar = view.findViewById(R.id.botonTresModificar);
        btn_si = view.findViewById(R.id.botonTresSi);
        btn_cancelar = view.findViewById(R.id.botonTresCancelar);


        // BOTON MODIFICAR
        btn_modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ---- SE CIERRA EL DIALOGO ACTUAL ---- //
                getDialog().dismiss();

                // CASO CONFIGURACION INICIAL
                Activity activity = getActivity();
                if (activity instanceof MainActivity) {
                    ((MainActivity) activity).replaceFragment(new ConfiguracionInicialFragment(1), ((MainActivity) activity).getSupportFragmentManager(), R.id.framecentral);
                    activity.setTitle(getString(R.string.title_opcion_1_config));
                    ((MainActivity) activity).setFragmentActual(getString(R.string.title_opcion_1_config));
                    ((MainActivity) activity).setActionBarActivityArrow(false);
                }
            }
        });

        // BOTON SI (MANTENER CONFIGURACIÓN)
        btn_si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // ---- para imprimir ---- //
                Toast toast = Toast.makeText(getActivity().getApplicationContext(),"LISTO!", Toast.LENGTH_SHORT);
                toast.show();

                // ---- almacenar en BD ---- //
                DbAsignaturas dbAsignaturas = new DbAsignaturas(getActivity().getApplicationContext());
                Long idAux = dbAsignaturas.crearAsignatura(1, Integer.parseInt(AsignaturasFragment.getTipoPromedio_ingresada()), AsignaturasFragment.getNombre_Asignatura_ingresada());
                dbAsignaturas.close();

                // ---- SE CIERRA EL DIALOGO ACTUAL ---- //
                getDialog().dismiss();

                // ---- SETEAR VARIABLES ESTÁTICAS ---- //
                AsignaturasFragment.setNombre_Asignatura_ingresada("");
                AsignaturasFragment.setTipoPromedio_ingresada("");

                // ---- VOLVER A LA LISTA DE ASIGNATURAS ACTUALIZADA ---- //
                AsignaturasFragment.dismissAllDialogs(getActivity().getSupportFragmentManager());
                Activity activity = getActivity();
                if (activity instanceof MainActivity){
                    ((MainActivity) activity).replaceFragment(new AsignaturasFragment(), ((MainActivity) activity).getSupportFragmentManager(), R.id.framecentral);
                }
            }
        });


        // BOTON CANCELAR
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsignaturasFragment.setTipoPromedio_ingresada("");
                AsignaturasFragment.setNombre_Asignatura_ingresada("");
                getDialog().dismiss();
            }
        });


        return view;
    }

}