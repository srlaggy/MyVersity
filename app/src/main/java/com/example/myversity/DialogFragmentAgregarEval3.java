package com.example.myversity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myversity.db.DbAsignaturas;
import com.example.myversity.db.DbTipoPromedio;
import com.example.myversity.entidades.TipoPromedio;

import java.util.List;

public class DialogFragmentAgregarEval3 extends androidx.fragment.app.DialogFragment {
    private Button btn_dialogFragment_agre_eval3_cancelar, btn_dialogFragment_agre_eval3_confirmar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_agregar_eval3,container,false);
        super.onCreateView(inflater, container, savedInstanceState);
        btn_dialogFragment_agre_eval3_cancelar = view.findViewById(R.id.dialogFragment_agre_eval2_cancelar);
        btn_dialogFragment_agre_eval3_confirmar = view.findViewById(R.id.dialogFragment_agre_eval2_confirmar);

        // BOTON CANCELAR
        btn_dialogFragment_agre_eval3_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        // BOTON CONFIRMAR
        btn_dialogFragment_agre_eval3_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return view;
    }

}