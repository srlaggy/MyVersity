package com.example.myversity;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.example.myversity.adapters.GridNotasAdapter;
import com.example.myversity.adapters.RvEvalAdapter;
import com.example.myversity.db.DbAsignaturas;
import com.example.myversity.db.DbEvaluaciones;
import com.example.myversity.db.DbNotas;
import com.example.myversity.entidades.Asignaturas;
import com.example.myversity.entidades.Evaluaciones;
import com.example.myversity.entidades.Notas;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class VistaAsignaturaFragment extends Fragment {
    ExtendedFloatingActionButton efabAgregar;
    FloatingActionButton fabAgregarEval, fabAgregarCond;
    TextView agregarEvalText, agregarCondText;
    Boolean fabsVisible;
    RecyclerView recyclerEval;

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

        // ---- LISTA NOTAS ---- //
        DbNotas dbNotas = new DbNotas(getActivity().getApplicationContext());
        List<Notas> listaNotas = dbNotas.buscarNotas();
        dbNotas.close();

        List<String> nombreEvaluaciones = new ArrayList<>();
        List<Integer> cantidadEvaluaciones = new ArrayList<>();
        List<Integer> idEvaluaciones = new ArrayList<>();
        List<String> valueNotas = new ArrayList<>();

        // SE VERIFICA SI HAY DATOS O NO
        if( listaEvaluaciones.size() == 0){
            TextView view_null = (TextView) view.findViewById(R.id.view_null_eval);
            System.out.println("NO HAY EVALUACIONES");
        }else{
            System.out.println("LISTA EVALUACIONES:");
            System.out.println(listaEvaluaciones);
            for (Evaluaciones a : listaEvaluaciones){
                nombreEvaluaciones.add(a.getTipo());
                cantidadEvaluaciones.add(a.getCantidad());
                idEvaluaciones.add(a.getId());
            }
            System.out.println("TIPO EVALUACIONES:");
            System.out.println(nombreEvaluaciones);
            System.out.println("CANTIDAD EVALUACIONES:");
            System.out.println(cantidadEvaluaciones);
            System.out.println("ID EVALUACIONES:");
            System.out.println(idEvaluaciones);

            // ---- LISTA NOTAS PRIMER EVALUACIÓN ---- //
            DbNotas dbNotasID = new DbNotas(getActivity().getApplicationContext());
            List<Notas> listaNotasID = dbNotasID.buscarNotasPorIdEvaluacion(idEvaluaciones.get(0));
            /*for (int i : idEvaluaciones){
                List<Notas> listaNotas = dbNotas.buscarNotasPorIdEvaluacion(i);
                for (Notas n : listaNotas){
                }
            }*/
            dbNotas.close();
            System.out.println("LISTA NOTAS:");
            System.out.println(listaNotas);
            for (Notas n : listaNotasID){
                valueNotas.add(n.getNota());
            }
            System.out.println("LISTA NOTAS PRIMER EVALUACIÓN:");
            System.out.println(valueNotas);

            recyclerEval = view.findViewById(R.id.recyclerview_eval);
            RvEvalAdapter rvEvalAdapter = new RvEvalAdapter(getActivity().getApplicationContext(), listaEvaluaciones /*, nombreEvaluaciones, cantidadEvaluaciones, idEvaluaciones*/);
            rvEvalAdapter.notifyDataSetChanged();
            recyclerEval.setAdapter(rvEvalAdapter);
            recyclerEval.setHasFixedSize(true);
            recyclerEval.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

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