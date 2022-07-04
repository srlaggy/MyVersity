package com.example.myversity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DialogFragmentOpciones extends androidx.fragment.app.DialogFragment {
    private Button btn_editar, btn_eliminar, btn_cancelar;
    private EditText mInput;
    private TextView nameAsignatura;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_opciones_asignatura,container,false);
        super.onCreateView(inflater, container, savedInstanceState);

        nameAsignatura = view.findViewById(R.id.asignatura_opciones_nombre);
        btn_cancelar = view.findViewById(R.id.botonDialogOpcionesCancelar);
        btn_editar = view.findViewById(R.id.botonDialogOpcionesEditar);
        btn_eliminar = view.findViewById(R.id.botonDialogOpcionesEliminar);
        mInput = view.findViewById(R.id.nombre_asignatura_dialog);

       nameAsignatura.setText(getArguments().getString("name"));


        // BOTON CANCELAR
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsignaturasFragment.setNombre_Asignatura_ingresada("");
                getDialog().dismiss();
            }
        });

        btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragmentEliminar dialogFragmentEliminar = new DialogFragmentEliminar();

                Bundle args = new Bundle();
                args.putString("name", getArguments().getString("name"));
                args.putInt("id", getArguments().getInt("id"));
                args.putInt("idConfig", getArguments().getInt("idConfig"));
                dialogFragmentEliminar.setArguments(args);

                dialogFragmentEliminar.show(getActivity().getSupportFragmentManager(), "My Fragment Delete");

                // ---- SE CIERRA EL DIALOGO ACTUAL ---- //
                getDialog().dismiss();
            }
        });

        // BOTON CONFIRMAR
        btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });


        return view;
    }

}