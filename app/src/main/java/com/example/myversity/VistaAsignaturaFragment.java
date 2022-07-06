package com.example.myversity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myversity.adapters.CondAdapter;
import com.example.myversity.adapters.RvEvalAdapter;
import com.example.myversity.db.DbAsignaturas;
import com.example.myversity.db.DbEvaluaciones;
import com.example.myversity.entidades.Asignaturas;
import com.example.myversity.entidades.CondAsignatura;
import com.example.myversity.entidades.Evaluaciones;
import com.example.myversity.entidades.Notas;
import com.example.myversity.entidades.TipoPromedio;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VistaAsignaturaFragment extends Fragment {
    ExtendedFloatingActionButton efabAgregar;
    FloatingActionButton fabAgregarEval, fabAgregarCond;
    TextView agregarEvalText, agregarCondText, asignPromedio;
    Boolean fabsVisible;
    RecyclerView recyclerEval, recyclerCond;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    public static RvEvalAdapter rvEvalAdapter;
    public static CondAdapter condAdapter;
    public static List<Evaluaciones> listaEvaluaciones;
    public static List<CondAsignatura> listaCondiciones;

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
        listaCondiciones = asignatura.getCa();

        System.out.println("ID ASIGNATURA: "+id_asign);
        System.out.println("NOMBRE ASIGNATURA: "+name_asign);

        List<String> nombreEvaluaciones = new ArrayList<>();
        List<Integer> cantidadEvaluaciones = new ArrayList<>();
        List<Integer> idEvaluaciones = new ArrayList<>();
        List<String> valueNotas = new ArrayList<>();

        // SE VERIFICA SI HAY DATOS O NO
        if( listaEvaluaciones.size() == 0){
            DbAsignaturas dbAsignaturas = new DbAsignaturas(getActivity().getApplicationContext());
            Long estadoAsig = dbAsignaturas.actualizarNotaAsignatura(asignatura.getId(), "0.00");
            dbAsignaturas.close();

            asignatura.setNota_final("0.00");

            TextView view_null = view.findViewById(R.id.view_null_eval);
            System.out.println("NO HAY EVALUACIONES");
        }else{
            boolean evTieneNotaFinal = true;
            System.out.println("LISTA EVALUACIONES:");
            System.out.println(listaEvaluaciones);
            for (Evaluaciones a : listaEvaluaciones){
                // revisamos que las notas existan
                boolean notaTieneNota = true;
                for (Notas n : a.getNotas()) {
                    if(n.getNota()==null || Objects.equals(n.getNota(), "")){
                        notaTieneNota = false;
                        break;
                    }
                }

                // en caso de existir -> calculamos promedio de evaluacion
                // en caso de no existir -> entregamos un 0.00 de promedio
                String notaAuxEv;
                if(notaTieneNota){
//                    notaAuxEv = a.getTp().calcularPromedioEvaluaciones(a, a.getNotas()).toString();
                    notaAuxEv = df.format(a.getTp().calcularPromedioEvaluaciones(a, a.getNotas()));
                } else {
                    notaAuxEv = "0.00";
                    evTieneNotaFinal = false;
                }
                // actualizamos nota de la evaluacion
                a.setNota_evaluacion(notaAuxEv);
                DbEvaluaciones dbEvaluaciones = new DbEvaluaciones(getActivity().getApplicationContext());
                Long estadoEv = dbEvaluaciones.actualizarNotaEvaluacion(a.getId(), notaAuxEv);
                dbEvaluaciones.close();

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

            // en caso de existir notas para todas las evs -> calculamos promedio de asignatura
            // en caso de no existir -> entregamos un 0.00 de promedio
            String notaAuxAsig;
            if(evTieneNotaFinal){
//                notaAuxAsig = asignatura.getTp().calcularPromedioAsignaturas(listaEvaluaciones).toString();
                notaAuxAsig = df.format(asignatura.getTp().calcularPromedioAsignaturas(listaEvaluaciones));
            } else {
                notaAuxAsig = "0.00";
            }
            // actualizamos nota de la asignatura
            asignatura.setNota_final(notaAuxAsig);
            DbAsignaturas dbAsignaturas = new DbAsignaturas(getActivity().getApplicationContext());
            Long estadoAsig = dbAsignaturas.actualizarNotaAsignatura(asignatura.getId(), notaAuxAsig);
            dbAsignaturas.close();

            recyclerEval = view.findViewById(R.id.recyclerview_eval);
            rvEvalAdapter = new RvEvalAdapter(getActivity().getApplicationContext(), listaEvaluaciones);
            recyclerEval.setAdapter(rvEvalAdapter);
            recyclerEval.setHasFixedSize(true);
            recyclerEval.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

            asignPromedio = view.findViewById(R.id.asignatura_promedio);
            if(asignatura.getNota_final() != null){
                Float notaFinal = Float.parseFloat(asignatura.getNota_final());
                Float notaAprobacion = Float.parseFloat(asignatura.getConfig().getNotaAprobacion());
                asignPromedio.setText(df.format(notaFinal));
                List<String> listaCond = new ArrayList<>();
                List<Boolean> listaCheck = new ArrayList<>();
                for (CondAsignatura c: listaCondiciones){
                    if(c.getId_tiposPenalizacion()==1){
                        Log.d("test_cond", c.getCondicion() + " is " + c.getChequeado());
                        listaCond.add(c.getCondicion());
                        listaCheck.add(c.getChequeado());
                    }
                }

                if((notaFinal < notaAprobacion) || listaCheck.contains(false)){
                    Log.d("test_cond", "Se cambio el color en vivo");
                    asignPromedio.setBackground(getContext().getDrawable(R.drawable.roundstyle_error_final));
                } else {
                    Log.d("test_cond", "Se mantiene el color");
                    asignPromedio.setBackground(getContext().getDrawable(R.drawable.roundstyle_nota_final));
                }


            }else {
                asignPromedio.setText(df.format(asignatura.getTp().calcularPromedioAsignaturas(listaEvaluaciones)));
            }

        }
        System.out.println("NUMBER CONDICIONES "+listaCondiciones.size());
        if(listaCondiciones.size() > 0){
            System.out.println("LISTA CONDICIONES:");
            System.out.println(listaCondiciones);

            recyclerCond = view.findViewById(R.id.recyclerview_cond);
            condAdapter = new CondAdapter(getActivity().getApplicationContext(), listaCondiciones);
            recyclerCond.setAdapter(condAdapter);
            recyclerCond.setHasFixedSize(true);
            recyclerCond.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
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
                    agregarCondText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    agregarCondText.getBackground().setAlpha(180);
                    agregarEvalText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    agregarEvalText.getBackground().setAlpha(180);
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