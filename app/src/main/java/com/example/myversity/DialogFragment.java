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

public class DialogFragment extends androidx.fragment.app.DialogFragment {
    private Button btn_confirmar, btn_cancelar;
    private EditText mInput;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment,container,false);
        super.onCreateView(inflater, container, savedInstanceState);
        btn_cancelar = view.findViewById(R.id.botonUnoCancelar);
        btn_confirmar = view.findViewById(R.id.botonUnoConfirmar);
        mInput = view.findViewById(R.id.nombre_asignatura_dialog);


        // BOTON CANCELAR
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsignaturasFragment.setNombre_Asignatura_ingresada("");
                getDialog().dismiss();
            }
        });

        // BOTON CONFIRMAR
        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = mInput.getText().toString();
                if(!input.equals("")){
                    AsignaturasFragment.setNombre_Asignatura_ingresada(input);

                    // ---- para imprimir ---- //
                    // Toast toast = Toast.makeText(getActivity().getApplicationContext(),input, Toast.LENGTH_SHORT);
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),AsignaturasFragment.getNombre_Asignatura_ingresada(), Toast.LENGTH_SHORT);
                    toast.show();

                    // NO SE SI IR SACANDO EL DIALOGO ACTUAL CUANDO SE PASE A LA SIG VISTA O NO
                    // getDialog().dismiss();

                    // ---- AVANZAR AL SIGUIENTE DIALOGO 2 DE 3 ---- //
                    DialogFragment2 dialogFragment2 = new DialogFragment2();
                    dialogFragment2.show(getActivity().getSupportFragmentManager(), "My  Fragment2");
                }
                else{
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),"Ingrese nombre asignatura", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });


        return view;
    }

}