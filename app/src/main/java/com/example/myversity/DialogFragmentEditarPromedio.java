package com.example.myversity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myversity.db.DbAsignaturas;

public class DialogFragmentEditarPromedio extends androidx.fragment.app.DialogFragment {
    private Button btn_confirmar, btn_cancelar;
    private RadioButton r1,r2,r3,r4,r5;
    private TextView nameAsignatura;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_editar_promedio_asignatura,container,false);
        super.onCreateView(inflater, container, savedInstanceState);

        r1 = (RadioButton) view.findViewById(R.id.radioButtonDialogEditarMediaArimetica);
        r2 = (RadioButton) view.findViewById(R.id.radioButtonDialogEditarMediaPonderada);
        r3 = (RadioButton) view.findViewById(R.id.radioButtonDialogEditarMediaGeometrica);
        r4 = (RadioButton) view.findViewById(R.id.radioButtonDialogEditarMediaCuadratica);
        r5 = (RadioButton) view.findViewById(R.id.radioButtonDialogEditarMediaSoloSuma);

        btn_cancelar = view.findViewById(R.id.botonDialogEditarPromedioCancelar);
        btn_confirmar = view.findViewById(R.id.botonDialogEditarPromedioConfirmar);

        nameAsignatura = view.findViewById(R.id.asignatura_editar_promedio_nombre);
        nameAsignatura.setText(getArguments().getString("name"));

        // BOTON CANCELAR
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    DbAsignaturas dbAsignaturas = new DbAsignaturas(getActivity().getApplicationContext());
                    Integer auxID = getArguments().getInt("id");
                    Long idAux = dbAsignaturas.actualizarIdTipoPromedioAsignatura(auxID,Integer.parseInt(seleccionTipoPromedio));
                    dbAsignaturas.close();

                    // ---- SE CIERRA EL DIALOGO ACTUAL ---- //
                    getDialog().dismiss();

                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),"Tipo de promedio cambiado exitosamente", Toast.LENGTH_SHORT);
                    toast.show();

                    Activity activity = getActivity();
                    if (activity instanceof MainActivity){
                        ((MainActivity) activity).replaceFragment(new AsignaturasFragment(), ((MainActivity) activity).getSupportFragmentManager(), R.id.framecentral);
                    }
                }

            }
        });


        return view;
    }
}