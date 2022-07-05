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

import com.google.android.material.textfield.TextInputEditText;

public class DialogFragmentNota extends androidx.fragment.app.DialogFragment {
    private Button btn_confirmar, btn_cancelar, btn_eliminar;
    private TextInputEditText nota_cond;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_notas,container,false);
        super.onCreateView(inflater, container, savedInstanceState);
        btn_cancelar = view.findViewById(R.id.botonNotaCancelar);
        btn_confirmar = view.findViewById(R.id.botonNotaConfirmar);
        btn_eliminar = view.findViewById(R.id.botonNotaEliminar);
        nota_cond = (TextInputEditText) view.findViewById(R.id.editText_cond_nota);


        // BOTON CANCELAR
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        // BOTON CONFIRMAR
        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String input_nota_cond = nota_cond.getText().toString();

                    // ---- AVANZAR AL SIGUIENTE DIALOGO 2 DE 3 ---- //
                    // DialogFragment2 dialogFragment2 = new DialogFragment2();
                    // dialogFragment2.show(getActivity().getSupportFragmentManager(), "My  Fragment2");

                    // ---- SE CIERRA EL DIALOGO ACTUAL ---- //
                    getDialog().dismiss();

            }
        });


        return view;
    }

}