package com.example.myversity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DialogFragmentOpcionesEval extends androidx.fragment.app.DialogFragment {
    private Button btn_editar, btn_eliminar, btn_cancelar;
    private TextView nameAsignatura;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_opciones_evaluacion,container,false);
        super.onCreateView(inflater, container, savedInstanceState);

        nameAsignatura = view.findViewById(R.id.evaluacion_opciones_nombre);
        btn_cancelar = view.findViewById(R.id.botonDialogOpcionesCancelar);
        btn_editar = view.findViewById(R.id.botonDialogOpcionesEditar);
        btn_eliminar = view.findViewById(R.id.botonDialogOpcionesEliminar);

        nameAsignatura.setText(getArguments().getString("name"));


        // BOTON CANCELAR
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsignaturasFragment.setNombre_Asignatura_ingresada("");
                getDialog().dismiss();
            }
        });

        //BOTON ELIMINAR
        btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragmentEliminarEval dialogFragmentEliminarEval = new DialogFragmentEliminarEval();

                Bundle args = new Bundle();
                args.putString("name", getArguments().getString("name"));
                args.putInt("id", getArguments().getInt("id"));
                dialogFragmentEliminarEval.setArguments(args);

                dialogFragmentEliminarEval.show(getActivity().getSupportFragmentManager(), "My Fragment Delete");


                // ---- SE CIERRA EL DIALOGO ACTUAL ---- //
                getDialog().dismiss();
            }
        });

        // BOTON EDITAR
        btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragmentEditarEval dialogFragmentEditarEval = new DialogFragmentEditarEval();

                Bundle args = new Bundle();
                args.putString("name", getArguments().getString("name"));
                args.putInt("id", getArguments().getInt("id"));
                args.putInt("cantidad", getArguments().getInt("cantidad"));
                args.putInt("tipo",getArguments().getInt("tipo"));
                args.putString("cond",getArguments().getString("cond"));
                dialogFragmentEditarEval.setArguments(args);

                dialogFragmentEditarEval.show(getActivity().getSupportFragmentManager(), "My Fragment Edit Eval");

                // ---- SE CIERRA EL DIALOGO ACTUAL ---- //
                getDialog().dismiss();
            }
        });
        return view;
    }
}