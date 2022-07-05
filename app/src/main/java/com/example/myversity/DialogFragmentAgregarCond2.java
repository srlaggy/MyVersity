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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myversity.db.DbAsignaturas;
import com.example.myversity.db.DbCondAsignatura;
import com.example.myversity.entidades.CondAsignatura;

public class DialogFragmentAgregarCond2 extends androidx.fragment.app.DialogFragment {
    private Button btn_confirmar, btn_cancelar;
    private EditText valorCond;
    private TextView texto_tipoCondicion;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_agregar_condicion2,container,false);
        super.onCreateView(inflater, container, savedInstanceState);
        btn_cancelar = view.findViewById(R.id.botonCond2Cancelar);
        btn_confirmar = view.findViewById(R.id.botonCond2Confirmar);
        valorCond = view.findViewById(R.id.editText_valorCond);
        texto_tipoCondicion = view.findViewById(R.id.textoTipoCond);

        if(VistaAsignaturaFragment.static_id_tipoCondicion.equals("2")){
            texto_tipoCondicion.setText("Descuento porcentaje nota final");
        }
        else if(VistaAsignaturaFragment.static_id_tipoCondicion.equals("3")){
            texto_tipoCondicion.setText("Descuento puntaje nota final");
        }
        else if(VistaAsignaturaFragment.static_id_tipoCondicion.equals("4")){
            texto_tipoCondicion.setText("Adición puntaje nota final");
        }

        // BOTON CANCELAR
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VistaAsignaturaFragment.static_id_tipoCondicion = null;
                VistaAsignaturaFragment.static_texto_condicion = null;
                VistaAsignaturaFragment.static_id_tipoCondicion = null;
                getDialog().dismiss();
            }
        });

        // BOTON CONFIRMAR
        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String input_valorCond = valorCond.getText().toString();
                Boolean flag_valido = false;
                if(!input_valorCond.equals("")){
                    // 2: Descuento porcentaje nota final       SI REQUIERE_VALOR
                    if(VistaAsignaturaFragment.static_id_tipoCondicion.equals("2")){
                        if(Integer.parseInt(input_valorCond)<= 100 && Integer.parseInt(input_valorCond)>= 0){
                            flag_valido = true;
                        }
                    }
                    // 3: Descuento puntaje nota final          SI REQUIERE_VALOR
                    // 4: Adición puntaje nota final            SI REQUIERE_VALOR
                    else if(VistaAsignaturaFragment.static_id_tipoCondicion.equals("3") | VistaAsignaturaFragment.static_id_tipoCondicion.equals("4")){
                        flag_valido = true;
                    }

                    if(flag_valido){
                        VistaAsignaturaFragment.static_valor_condicion_opc = input_valorCond;

                        // ---- variables ---- //
                        Integer id_asignatura = AsignaturasFragment.getAsignatura_seleccionada().getId();
                        Integer id_tiposPenalizacion = Integer.parseInt(VistaAsignaturaFragment.static_id_tipoCondicion);
                        String condicion = VistaAsignaturaFragment.static_texto_condicion;
                        CondAsignatura condAsignatura = new CondAsignatura(id_asignatura, id_tiposPenalizacion, condicion, false, input_valorCond);

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
                    else{
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(),"Valor debe ser entre 0 y 100", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                else{
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),"Ingrese un valor", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });


        return view;
    }

}