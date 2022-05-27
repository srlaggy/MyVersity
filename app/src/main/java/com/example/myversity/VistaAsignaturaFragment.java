package com.example.myversity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class VistaAsignaturaFragment extends Fragment {
    ExtendedFloatingActionButton efabAgregar;
    FloatingActionButton fabAgregarEval, fabAgregarCond;
    TextView agregarEvalText, agregarCondText;
    Boolean fabsVisible;

    public VistaAsignaturaFragment() {
    }
    // TODO: Rename and change types and number of parameters
    public static VistaAsignaturaFragment newInstance() {
        VistaAsignaturaFragment fragment = new VistaAsignaturaFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vista_asignatura, container, false);

        Integer id_asign = AsignaturasFragment.getId_Asignatura();
        String name_asign = AsignaturasFragment.getName_Asignatura();
        System.out.println("NOMBRE ASIGNATURA ->");
        System.out.println(name_asign);

        TextView nameView = view.findViewById(R.id.id_nombre_asignatura);
        nameView.setText(name_asign);


        efabAgregar = view.findViewById(R.id.botonAgregarVistaAsignatura);
        fabAgregarEval = view.findViewById(R.id.botonAgregarEvaluacion);
        fabAgregarCond = view.findViewById(R.id.botonAgregarCondicion);
        agregarEvalText = view.findViewById(R.id.agregar_evaluacion_text);
        agregarCondText = view.findViewById(R.id.agregar_condicion_text);

        fabAgregarEval.setVisibility(View.GONE);
        fabAgregarCond.setVisibility(View.GONE);
        agregarEvalText.setVisibility(View.GONE);
        agregarCondText.setVisibility(View.GONE);
        fabsVisible = false;

        efabAgregar.shrink();
        efabAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fabsVisible){
                    fabAgregarEval.show();
                    fabAgregarCond.show();
                    agregarEvalText.setVisibility(View.VISIBLE);
                    agregarCondText.setVisibility(View.VISIBLE);
                    efabAgregar.extend();
                    fabsVisible = true;
                } else {
                    fabAgregarEval.hide();
                    fabAgregarCond.hide();
                    agregarEvalText.setVisibility(View.GONE);
                    agregarCondText.setVisibility(View.GONE);
                    efabAgregar.shrink();
                    fabsVisible = false;
                }
            }
        });

        fabAgregarEval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "Evaluación Agregado",Toast.LENGTH_SHORT).show();
            }
        });
        fabAgregarCond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "Condición Agregado",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}