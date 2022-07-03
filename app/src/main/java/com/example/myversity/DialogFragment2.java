package com.example.myversity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DialogFragment2 extends androidx.fragment.app.DialogFragment {
    private Button btn_confirmar, btn_cancelar;
    private RadioButton r1,r2,r3,r4,r5;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment2,container,false);
        super.onCreateView(inflater, container, savedInstanceState);

        r1 = (RadioButton) view.findViewById(R.id.MediaAritmetica);
        r2 = (RadioButton) view.findViewById(R.id.MediaPonderada);
        r3 = (RadioButton) view.findViewById(R.id.MediaGeometrica);
        r4 = (RadioButton) view.findViewById(R.id.MediaCuadratica);
        r5 = (RadioButton) view.findViewById(R.id.MediaSoloSuma);

        btn_cancelar = view.findViewById(R.id.botonDosCancelar);
        btn_confirmar = view.findViewById(R.id.botonDosConfirmar);

        // BOTON CANCELAR
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsignaturasFragment.setTipoPromedio_ingresada("");
                getDialog().dismiss();
            }
        });

        // BOTON CONFIRMAR
        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            String seleccionTipoPromedio;
            @Override
            public void onClick(View v) {
                if (r1.isChecked()){
                    seleccionTipoPromedio = "1";
                }
                else if (r2.isChecked()){
                    seleccionTipoPromedio = "2";
                }
                else if (r3.isChecked()){
                    seleccionTipoPromedio = "3";
                }
                else if (r4.isChecked()){
                    seleccionTipoPromedio = "4";
                }
                else if (r5.isChecked()){
                    seleccionTipoPromedio = "5";
                }
                else {
                    seleccionTipoPromedio = "0";
                }

                // ---- SE VERIFICA LA SELECCIÓN ---- //
                if(seleccionTipoPromedio.equals("0")){
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),"Seleccione una opción", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    // se almacena variable de TipoPromedio_ingresada
                    AsignaturasFragment.setTipoPromedio_ingresada(seleccionTipoPromedio);

                    // ---- para imprimir ---- //
                    // Toast toast = Toast.makeText(getActivity().getApplicationContext(),seleccionTipoPromedio, Toast.LENGTH_SHORT);
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),AsignaturasFragment.getTipoPromedio_ingresada(), Toast.LENGTH_SHORT);
                    toast.show();

                    // ---- AVANZAR AL SIGUIENTE DIALOGO 3 DE 3 ---- //
                    DialogFragment3 dialogFragment3 = new DialogFragment3();
                    dialogFragment3.show(getActivity().getSupportFragmentManager(), "My  Fragment3");

                    // ---- SE CIERRA EL DIALOGO ACTUAL ---- //
                    getDialog().dismiss();
                }

            }
        });


        return view;
    }
}