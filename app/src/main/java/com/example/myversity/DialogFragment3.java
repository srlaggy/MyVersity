package com.example.myversity;

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

public class DialogFragment3 extends androidx.fragment.app.DialogFragment {
    private Button btn_modificar, btn_si;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment3,container,false);
        super.onCreateView(inflater, container, savedInstanceState);
        btn_modificar = view.findViewById(R.id.botonTresModificar);
        btn_si = view.findViewById(R.id.botonTresSi);


        // BOTON CANCELAR
        btn_modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        // BOTON CONFIRMAR
        btn_si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ---- AVANZAR AL SIGUIENTE DIALOGO 4 DE 3 (modificar) ---- //
                DialogFragment4 dialogFragment4 = new DialogFragment4();
                dialogFragment4.show(getActivity().getSupportFragmentManager(), "My  Fragment4");

                Toast toast = Toast.makeText(getActivity().getApplicationContext(),"LISTO!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });


        return view;
    }

}