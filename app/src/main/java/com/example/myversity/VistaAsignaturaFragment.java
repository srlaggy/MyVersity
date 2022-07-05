package com.example.myversity;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myversity.adapters.RvEvalAdapter;
import com.example.myversity.entidades.Asignaturas;
import com.example.myversity.entidades.Evaluaciones;
import com.example.myversity.entidades.TipoPromedio;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class VistaAsignaturaFragment extends Fragment {
    ExtendedFloatingActionButton efabAgregar;
    FloatingActionButton fabAgregarEval, fabAgregarCond;
    TextView agregarEvalText, agregarCondText, asignPromedio;
    Boolean fabsVisible;
    RecyclerView recyclerEval;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    public static RvEvalAdapter rvEvalAdapter;
    public static List<Evaluaciones> listaEvaluaciones;
    FloatingActionButton BtnGuardar;

    // ---- VARIABLES ESTÁTICAS PARA DIALOG FRAGMENT DE AGREGAR EVALUACIÓN ---- //
    public static String nombre_eval_agregar;
    public static String cant_eval_agregar;
    public static TipoPromedio tipoPromedio_eval_agregar;
    public static Boolean cond_eval_agregar;
    public static String notaCond_eval_agregar;

    // ---- VARIABLES ESTÁTICAS PARA DIALOG FRAGMENT DE AGREGAR CONDICIÓN ---- //
    public static String static_id_tipoCondicion;
    public static String static_texto_condicion;
    public static String static_valor_condicion_opc;


    public VistaAsignaturaFragment() {
        // Required empty public constructor
    }
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
        Asignaturas asignatura = AsignaturasFragment.getAsignatura_seleccionada();
        listaEvaluaciones = asignatura.getEv();

        System.out.println("ID ASIGNATURA: "+id_asign);
        System.out.println("NOMBRE ASIGNATURA: "+name_asign);

        List<String> nombreEvaluaciones = new ArrayList<>();
        List<Integer> cantidadEvaluaciones = new ArrayList<>();
        List<Integer> idEvaluaciones = new ArrayList<>();
        List<String> valueNotas = new ArrayList<>();

        // SE VERIFICA SI HAY DATOS O NO
        if( listaEvaluaciones.size() == 0){
            TextView view_null = view.findViewById(R.id.view_null_eval);
            System.out.println("NO HAY EVALUACIONES");
        }else{
            System.out.println("LISTA EVALUACIONES:");
            System.out.println(listaEvaluaciones);
            for (Evaluaciones a : listaEvaluaciones){
                nombreEvaluaciones.add(a.getTipo());
                cantidadEvaluaciones.add(a.getCantidad());
                idEvaluaciones.add(a.getId());
                valueNotas.add(a.getNota_evaluacion());
            }
            System.out.println("TIPO EVALUACIONES:");
            System.out.println(nombreEvaluaciones);
            System.out.println("CANTIDAD EVALUACIONES:");
            System.out.println(cantidadEvaluaciones);
            System.out.println("ID EVALUACIONES:");
            System.out.println(idEvaluaciones);
            System.out.println("NOTA EVALUACIONES:");
            System.out.println(valueNotas);

            recyclerEval = view.findViewById(R.id.recyclerview_eval);
            rvEvalAdapter = new RvEvalAdapter(getActivity().getApplicationContext(), listaEvaluaciones);
            recyclerEval.setAdapter(rvEvalAdapter);
            recyclerEval.setHasFixedSize(true);
            recyclerEval.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

            asignPromedio = view.findViewById(R.id.asignatura_promedio);
            if(asignatura.getNota_final() != null){
                asignPromedio.setText(df.format(Float.parseFloat(asignatura.getNota_final())));
            }else {
                asignPromedio.setText(df.format(asignatura.getTp().calcularPromedioAsignaturas(listaEvaluaciones)));
            }

        }


        //---- BOTONES PARA AGREGAR ----//
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
                DialogFragmentAgregarEval dialogFragmentAgregarEval = new DialogFragmentAgregarEval();
                dialogFragmentAgregarEval.show(getActivity().getSupportFragmentManager(), "DialogFragmentAgregarEval");
            }
        });
        fabAgregarCond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragmentAgregarCond dialogFragmentAgregarCond = new DialogFragmentAgregarCond();
                dialogFragmentAgregarCond.show(getActivity().getSupportFragmentManager(), "DialogFragmentAgregarCond");
            }
        });

        return view;
    }
}