package com.example.myversity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.regex.Pattern;

public class ConfiguracionInicialFragment extends Fragment {
    private TextInputEditText min, max, nota;
    private RadioGroup orientacion, tipo;

    public ConfiguracionInicialFragment() {
        // Required empty public constructor
    }

    public static ConfiguracionInicialFragment newInstance(String param1, String param2) {
        ConfiguracionInicialFragment fragment = new ConfiguracionInicialFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_configuracion_inicial, container, false);
        Button btn = view.findViewById(R.id.btn_guardar_config_inicial);

        min = (TextInputEditText) view.findViewById(R.id.editText_min_rango_nota_config_inicial);
        max = (TextInputEditText) view.findViewById(R.id.editText_max_rango_nota_config_inicial);
        nota = (TextInputEditText) view.findViewById(R.id.editText_nota_aprobacion_config_inicial);
        orientacion = (RadioGroup) view.findViewById(R.id.radio_orientacion_config_inicial);
        tipo = (RadioGroup) view.findViewById(R.id.radio_tiponota_config_inicial);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean validado = validadorConfigInicial();
                if(validado){
                    Toast.makeText(getActivity().getApplicationContext(), "LAS COSAS ESTAN VALIDADAS", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "HAY ERRORES, NO VALIDADO", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    public Boolean validadorConfigInicial(){
        min.setError(null);
        max.setError(null);
        nota.setError(null);

        Boolean validado = true;
        Boolean minCorrecto = false;
        Boolean maxCorrecto = false;
        Float minNumF = 0f;
        Float maxNumF = 0f;
        Integer minNumI = 0;
        Integer maxNumI = 0;

        // EXPRESION REGULAR PARA CALZAR LOS NUMEROS CON Y SIN DECIMAL
        Pattern patternNumber = Pattern.compile("^(?:(?:\\d\\.\\d+)|(?:[1-9]\\d+\\.\\d+)|(?:\\d)|(?:[1-9]\\d+))$", Pattern.MULTILINE);

        // OBTENEMOS SELECCION DEL RADIO BUTTON DE TIPO DE NOTA Y SETEAMOS FLAG
        View vistaRG = tipo.findViewById(tipo.getCheckedRadioButtonId());
        int idIndiceSeleccion = tipo.indexOfChild(vistaRG);
        RadioButton rb = (RadioButton) tipo.getChildAt(idIndiceSeleccion);
        String seleccion = (String) rb.getText();
        Boolean decimal = Objects.equals(seleccion, getString(R.string.nota_continua_config_inicial));

        // VALIDADOR DE LONGITUD - RANGO DE NOTAS
        if(min.length() == 0 || max.length() == 0){
            // ALGUN CAMPO ESTA VACIO
            validado = false;
            if(min.length() == 0){
                min.setError(getString(R.string.e_campo_obligatorio));
            }
            if(max.length() == 0){
                max.setError(getString(R.string.e_campo_obligatorio));
            }
        } else {
            // AMBOS TIENEN DATOS

            // VEMOS FORMATO DE NUMERO CORRECTO Y RESPECTO A TIPO DE NOTA
            if(!patternNumber.matcher(min.getText().toString()).find()){
                validado = false;
                min.setError(getString(R.string.e_formato_incorrecto));
            } else if (min.getText().toString().contains(".") != decimal) {
                validado = false;
                min.setError(getString(R.string.e_tipo_nota_incorrecta));
            }

            if(!patternNumber.matcher(max.getText().toString()).find()){
                validado = false;
                max.setError(getString(R.string.e_formato_incorrecto));
            } else if (max.getText().toString().contains(".") != decimal) {
                validado = false;
                max.setError(getString(R.string.e_tipo_nota_incorrecta));
            }

            // EN CASO DE CUMPLIR LAS VALIDACIONES ANTERIORES
            // SE VALIDA RESPECTO AL MIN Y MAX
            if(validado){
                if(decimal){
                    minNumF = Float.parseFloat(min.getText().toString());
                    maxNumF = Float.parseFloat(max.getText().toString());
                    if(minNumF > maxNumF){
                        validado = false;
                        min.setError(getString(R.string.e_no_minimo));
                    } else {
                        minCorrecto = true;
                        maxCorrecto = true;
                    }
                } else {
                    minNumI = Integer.parseInt(min.getText().toString());
                    maxNumI = Integer.parseInt(max.getText().toString());
                    if(minNumI > maxNumI){
                        validado = false;
                        min.setError(getString(R.string.e_no_minimo));
                    } else {
                        minCorrecto = true;
                        maxCorrecto = true;
                    }
                }
            }
        }

        // VALIDADOR DE NOTA DE APROBACION
        if(nota.length() == 0){
            validado = false;
            nota.setError(getString(R.string.e_campo_obligatorio));
        } else {
            // NO ESTA VACIO
            if(!patternNumber.matcher(nota.getText().toString()).find()){
                validado = false;
                nota.setError(getString(R.string.e_formato_incorrecto));
            } else if (nota.getText().toString().contains(".") != decimal) {
                validado = false;
                nota.setError(getString(R.string.e_tipo_nota_incorrecta));
            } else if (minCorrecto && maxCorrecto){
                if(decimal){
                    Float tipoNum = Float.parseFloat(nota.getText().toString());
                    if( !( (minNumF < tipoNum) && (tipoNum < maxNumF) ) ){
                        validado = false;
                        nota.setError(getString(R.string.e_no_rango));
                    }
                } else {
                    Integer tipoNum = Integer.parseInt(nota.getText().toString());
                    if( !( (minNumI < tipoNum) && (tipoNum < maxNumI) ) ){
                        validado = false;
                        nota.setError(getString(R.string.e_no_rango));
                    }
                }
            }
        }

        return validado;
    }
}