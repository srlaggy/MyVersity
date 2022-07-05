package com.example.myversity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myversity.db.DbAsignaturas;
import com.example.myversity.db.DbCondAsignatura;
import com.example.myversity.db.DbEvaluaciones;
import com.example.myversity.entidades.CondAsignatura;

public class DialogFragmentAgregarCond extends androidx.fragment.app.DialogFragment {
    private Button btn_confirmar, btn_cancelar;
    private EditText textoCondicion;
    private RadioButton r1,r2,r3,r4;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_agregar_condicion,container,false);
        super.onCreateView(inflater, container, savedInstanceState);
        btn_cancelar = view.findViewById(R.id.botonCondCancelar);
        btn_confirmar = view.findViewById(R.id.botonCondConfirmar);
        textoCondicion = view.findViewById(R.id.editText_textoCondicion);

        r1 = (RadioButton) view.findViewById(R.id.RadioButtonCond_Reprobación);
        r2 = (RadioButton) view.findViewById(R.id.RadioButtonCond_DctoPorcentaje);
        r3 = (RadioButton) view.findViewById(R.id.RadioButtonCond_DctoPuntaje);
        r4 = (RadioButton) view.findViewById(R.id.RadioButtonCond_AdcPuntaje);

        // BOTON CANCELAR
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VistaAsignaturaFragment.static_id_tipoCondicion = null;
                VistaAsignaturaFragment.static_texto_condicion = null;
                getDialog().dismiss();
            }
        });

        // BOTON CONFIRMAR
        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            // 1: Reprobación                           NO REQUIERE_VALOR
            // 2: Descuento porcentaje nota final       SI REQUIERE_VALOR
            // 3: Descuento puntaje nota final          SI REQUIERE_VALOR
            // 4: Adición puntaje nota final            SI REQUIERE_VALOR

            String seleccionTipoCondicion;
            @Override
            public void onClick(View v) {
                if (r1.isChecked()){
                    seleccionTipoCondicion = "1";
                }
                else if (r2.isChecked()){
                    seleccionTipoCondicion = "2";
                }
                else if (r3.isChecked()){
                    seleccionTipoCondicion = "3";
                }
                else if (r4.isChecked()){
                    seleccionTipoCondicion = "4";
                }
                else {
                    seleccionTipoCondicion = "0";
                }

                String input_textoCondicion = textoCondicion.getText().toString();
                if(!input_textoCondicion.equals("") && !seleccionTipoCondicion.equals("0")){
                    // ---- se ingresan en las variables globlales ---- //
                    VistaAsignaturaFragment.static_id_tipoCondicion = seleccionTipoCondicion;
                    VistaAsignaturaFragment.static_texto_condicion = input_textoCondicion;

                    if(seleccionTipoCondicion.equals("2") | seleccionTipoCondicion.equals("3") | seleccionTipoCondicion.equals("4") ){
                        // ---- AVANZAR AL SIGUIENTE DIALOGO 2 DE 3 ---- //
                        DialogFragmentAgregarCond2 dialogFragmentAgregarCond2 = new DialogFragmentAgregarCond2();
                        dialogFragmentAgregarCond2.show(getActivity().getSupportFragmentManager(), "DialogFragmentAgregarCond2");

                        // ---- SE CIERRA EL DIALOGO ACTUAL ---- //
                        getDialog().dismiss();
                    }
                    else if(seleccionTipoCondicion.equals("1")){
                        // ---- variables ---- //
                        Integer id_asignatura = AsignaturasFragment.getAsignatura_seleccionada().getId();
                        Integer id_tiposPenalizacion = Integer.parseInt(VistaAsignaturaFragment.static_id_tipoCondicion);
                        String condicion = VistaAsignaturaFragment.static_texto_condicion;
                        CondAsignatura condAsignatura = new CondAsignatura(id_asignatura, id_tiposPenalizacion, condicion, false, null);

                        // ---- se agrega la condición a la bd ---- //
                        DbCondAsignatura dbCondAsignatura = new DbCondAsignatura(getActivity().getApplicationContext());
                        Long idNuevaCondicion = dbCondAsignatura.insertarCondAsignatura(condAsignatura);
                        dbCondAsignatura.close();

                        // ---- se agrega la evaluación en la asignatura (clase) ---- //
                        DbAsignaturas dbAsignaturas = new DbAsignaturas(getActivity().getApplicationContext());
                        AsignaturasFragment.setAsignatura_seleccionada(dbAsignaturas.buscarAsignaturaPorId(id_asignatura));
                        dbAsignaturas.close();

                        // ---- SE SETEAN LOS VALORES GLOBALES DE AGREGAR COND EN VistaAsignaturaFragment ---- //
                        VistaAsignaturaFragment.static_id_tipoCondicion = null;
                        VistaAsignaturaFragment.static_texto_condicion = null;

                        // ---- SE CIERRA EL DIALOGO ACTUAL ---- //
                        getDialog().dismiss();

                        // ---- ACTUALIZAR LA VISTA DE LA ASIGNATURA ---- //
                        Activity activity = getActivity();
                        if (activity instanceof MainActivity) {
                            ((MainActivity) activity).replaceFragment(new VistaAsignaturaFragment(), ((MainActivity) activity).getSupportFragmentManager(), R.id.framecentral);
                            activity.setTitle(AsignaturasFragment.getAsignatura_seleccionada().getNombre());
                            ((MainActivity) activity).setFragmentActual(AsignaturasFragment.getAsignatura_seleccionada().getNombre());
                            ((MainActivity) activity).setActionBarActivityArrow(true);
                        }

                    }

                }
                else{
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),"Ingrese los campos", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });


        return view;
    }

}