package com.example.myversity;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myversity.db.DbAsignaturas;
import com.example.myversity.db.DbEvaluaciones;
import com.example.myversity.entidades.Asignaturas;
import com.example.myversity.entidades.Evaluaciones;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

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

        // ---- LISTA EVALUACIONES ---- //
        DbEvaluaciones dbEvaluaciones = new DbEvaluaciones(getActivity().getApplicationContext());
        List<Evaluaciones> listaEvaluaciones = dbEvaluaciones.buscarEvaluacionesPorIdAsignatura(id_asign);
        dbEvaluaciones.close();
        List<String> nombreEvaluaciones = new ArrayList<>();
        List<String> cantidadEvaluaciones = new ArrayList<>();

        // SE VERIFICA SI HAY DATOS O NO
        if( listaEvaluaciones.size() == 0){
            TextView view_null = (TextView) view.findViewById(R.id.view_null_eval);
            System.out.println("NO HAY EVALUACIONES");
        }else{
            System.out.println(listaEvaluaciones);
            System.out.println("LISTA EVALUACIONES:");
            for (Evaluaciones a : listaEvaluaciones){
                nombreEvaluaciones.add(a.getTipo());
                cantidadEvaluaciones.add(a.getCantidad().toString());
            }
            System.out.println(nombreEvaluaciones);
            System.out.println(cantidadEvaluaciones);

            ArrayAdapter adapterEvaluaciones = new ArrayAdapter(getActivity().getApplicationContext(), R.layout.list_actividades, nombreEvaluaciones);
            ListView lvEvaluaciones = (ListView) view.findViewById(R.id.lista_evaluaciones);
            lvEvaluaciones.setAdapter(adapterEvaluaciones);
        }

        //ScrollView scrollView = view.findViewById(R.id.scrollview_vista_asignatura);

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
                Toast.makeText(getActivity().getApplicationContext(), "Evaluación agregada!",Toast.LENGTH_SHORT).show();
            }
        });
        fabAgregarCond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "Condición agregada!",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}