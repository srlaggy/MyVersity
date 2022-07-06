package com.example.myversity;

import android.app.Activity;
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

import com.example.myversity.db.DbAsignaturas;

public class DialogFragmentEliminar extends androidx.fragment.app.DialogFragment {
    private Button btn_confirmar, btn_cancelar;
    private TextView nameAsignatura;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_eliminar_asignatura,container,false);
        super.onCreateView(inflater, container, savedInstanceState);

        nameAsignatura = view.findViewById(R.id.asignatura_eliminar_nombre);
        btn_cancelar = view.findViewById(R.id.botonDialogEliminarCancelar);
        btn_confirmar = view.findViewById(R.id.botonDialogEliminarConfirmar);

        nameAsignatura.setText(getArguments().getString("name"));


        // BOTON CANCELAR
        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DbAsignaturas dbAsignaturas = new DbAsignaturas(getActivity().getApplicationContext());
                int auxID = getArguments().getInt("id");
                int configID = getArguments().getInt("idConfig");
                Long idAux = dbAsignaturas.borrarAsignatura(auxID,configID);
                dbAsignaturas.close();

                getDialog().dismiss();

                // ---- VOLVER A LA LISTA DE ASIGNATURAS ACTUALIZADA ---- //
                Activity activity = getActivity();
                if (activity instanceof MainActivity){
                    ((MainActivity) activity).replaceFragment(new AsignaturasFragment(), ((MainActivity) activity).getSupportFragmentManager(), R.id.framecentral);
                }
            }
        });

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getDialog().dismiss();
            }
        });

        return view;
    }

}