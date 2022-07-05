package com.example.myversity;

import android.app.Activity;
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

import com.example.myversity.db.DbAsignaturas;

public class DialogFragmentEditarNombre extends androidx.fragment.app.DialogFragment {
    private Button btn_confirmar, btn_cancelar;
    private EditText mInput;
    private TextView nameAsignatura;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_editar_nombre_asignatura,container,false);
        super.onCreateView(inflater, container, savedInstanceState);
        btn_cancelar = view.findViewById(R.id.botonDialogEditarNombreCancelar);
        btn_confirmar = view.findViewById(R.id.botonDialogEditarNombreConfirmar);
        mInput = view.findViewById(R.id.editDialogEditarNombre);
        nameAsignatura = view.findViewById(R.id.asignatura_editar_nombre_nombre);

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
            @Override
            public void onClick(View v) {
                String input = mInput.getText().toString();
                if(!input.equals("")){

                    DbAsignaturas dbAsignaturas = new DbAsignaturas(getActivity().getApplicationContext());
                    Integer auxID = getArguments().getInt("id");
                    Long idAux = dbAsignaturas.actualizarNombre(auxID,input);
                    dbAsignaturas.close();

                    getDialog().dismiss();

                    Activity activity = getActivity();
                    if (activity instanceof MainActivity){
                        ((MainActivity) activity).replaceFragment(new AsignaturasFragment(), ((MainActivity) activity).getSupportFragmentManager(), R.id.framecentral);
                    }
                }
                else{
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),"Ingrese nuevo nombre asignatura", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });


        return view;
    }

}