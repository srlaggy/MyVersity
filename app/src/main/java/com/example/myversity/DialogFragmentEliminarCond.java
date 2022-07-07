package com.example.myversity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myversity.db.DbAsignaturas;
import com.example.myversity.db.DbCondAsignatura;

public class DialogFragmentEliminarCond extends androidx.fragment.app.DialogFragment {
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

                DbCondAsignatura dbCondAsignatura = new DbCondAsignatura(getActivity().getApplicationContext());
                int auxID = getArguments().getInt("id");
                dbCondAsignatura.borrarCondAsignatura(auxID);
                dbCondAsignatura.close();

                getDialog().dismiss();

                Integer eval_id_asignaturas = AsignaturasFragment.getAsignatura_seleccionada().getId();
                DbAsignaturas dbAsignaturas = new DbAsignaturas(getActivity().getApplicationContext());
                AsignaturasFragment.setAsignatura_seleccionada(dbAsignaturas.buscarAsignaturaPorId(eval_id_asignaturas));
                dbAsignaturas.close();

                Activity activity = getActivity();
                if (activity instanceof MainActivity) {
                    ((MainActivity) activity).replaceFragment(new VistaAsignaturaFragment(), ((MainActivity) activity).getSupportFragmentManager(), R.id.framecentral);
                    activity.setTitle(AsignaturasFragment.getAsignatura_seleccionada().getNombre());
                    ((MainActivity) activity).setFragmentActual(AsignaturasFragment.getAsignatura_seleccionada().getNombre());
                    ((MainActivity) activity).setActionBarActivityArrow(true);
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