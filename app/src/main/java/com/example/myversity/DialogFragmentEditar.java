package com.example.myversity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DialogFragmentEditar extends androidx.fragment.app.DialogFragment {
    private Button btn_nombre, btn_tipoPromedio, btn_config, btn_cancelar;
    private TextView nameAsignatura;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_editar_asignatura,container,false);
        super.onCreateView(inflater, container, savedInstanceState);

        nameAsignatura = view.findViewById(R.id.asignatura_editar_nombre);
        btn_cancelar = view.findViewById(R.id.botonDialogEditarCancelar);
        btn_nombre = view.findViewById(R.id.botonDialogEditarNombre);
        btn_tipoPromedio = view.findViewById(R.id.botonDialogEditarPromedio);
        btn_config = view.findViewById(R.id.botonDialogEditarConfiguracion);

       nameAsignatura.setText(getArguments().getString("name"));

        // BOTON CANCELAR
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        btn_nombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragmentEditarNombre dialogFragmentEditarNombre = new DialogFragmentEditarNombre();

                Bundle args = new Bundle();
                args.putString("name", getArguments().getString("name"));
                args.putInt("id", getArguments().getInt("id"));
                args.putInt("idConfig", getArguments().getInt("idConfig"));
                dialogFragmentEditarNombre.setArguments(args);

                dialogFragmentEditarNombre.show(getActivity().getSupportFragmentManager(), "My Fragment Edit Name");

                // ---- SE CIERRA EL DIALOGO ACTUAL ---- //
                getDialog().dismiss();
            }
        });

        // BOTON CONFIRMAR
        btn_tipoPromedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragmentEditarPromedio dialogFragmentEditarPromedio = new DialogFragmentEditarPromedio();

                Bundle args = new Bundle();
                args.putString("name", getArguments().getString("name"));
                args.putInt("id", getArguments().getInt("id"));
                args.putInt("idConfig", getArguments().getInt("idConfig"));
                dialogFragmentEditarPromedio.setArguments(args);

                dialogFragmentEditarPromedio.show(getActivity().getSupportFragmentManager(), "My Fragment Edit Name");

                // ---- SE CIERRA EL DIALOGO ACTUAL ---- //
                getDialog().dismiss();
            }
        });

        btn_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ---- SE CIERRA EL DIALOGO ACTUAL ---- //
                getDialog().dismiss();

                // CASO CONFIGURACION INICIAL
                Activity activity = getActivity();
                if (activity instanceof MainActivity) {
                    ((MainActivity) activity).replaceFragment(new ConfiguracionInicialFragment(2), ((MainActivity) activity).getSupportFragmentManager(), R.id.framecentral);
                    activity.setTitle(getString(R.string.title_opcion_1_config));
                    ((MainActivity) activity).setFragmentActual(getString(R.string.title_opcion_1_config));
                    ((MainActivity) activity).setActionBarActivityArrow(false);
                }
            }
        });


        return view;
    }

}