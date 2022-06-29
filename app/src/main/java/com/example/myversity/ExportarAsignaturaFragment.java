package com.example.myversity;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myversity.adapters.RowExportar;
import com.example.myversity.adapters.RowExportarAdapter;
import com.example.myversity.db.DbAsignaturas;
import com.example.myversity.entidades.Asignaturas;
import com.example.myversity.entidades.CondAsignatura;
import com.example.myversity.entidades.Evaluaciones;
import com.example.myversity.entidades.Notas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ExportarAsignaturaFragment extends Fragment {
    private final String NOMBRE_ARCHIVO_EXPORT = "export.myv";
    private List<RowExportar> rows = new ArrayList<>();
    private ListView listViewExport;

    public ExportarAsignaturaFragment() {
    }

    public static ExportarAsignaturaFragment newInstance(String param1, String param2) {
        ExportarAsignaturaFragment fragment = new ExportarAsignaturaFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exportar_asignatura, container, false);
        listViewExport = (ListView) view.findViewById(R.id.lista_asignaturas_a_exportar);
        Button btnExport = (Button) view.findViewById(R.id.btn_exportar_asignatura);
        if(setearRows()){
            listViewExport.setAdapter(new RowExportarAdapter(getActivity().getApplicationContext(), rows));
        }

        if(rows.isEmpty()){
            return inflater.inflate(R.layout.sin_asignaturas_exportar, container, false);
        }

        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Asignaturas> asignaturas = asignaturasAExportar();
                if(asignaturas.isEmpty()){
                    Toast.makeText(getActivity().getApplicationContext(), "Debe seleccionar alguna asignatura", Toast.LENGTH_LONG).show();
                } else {
                    exportarAsignatura(asignaturas);
                }
            }
        });

        return view;
    }

    private Boolean setearRows(){
        List<Asignaturas> asig = null;

        try{
            DbAsignaturas dbAsignaturas = new DbAsignaturas(getActivity().getApplicationContext());
            asig = dbAsignaturas.buscarAsignaturas();
            dbAsignaturas.close();
        } catch (Exception e){
            System.out.println(e.toString());
        }

        if(asig == null || asig.isEmpty()){
            return false;
        }

        RowExportar row = null;
        for(Asignaturas a : asig){
            row = new RowExportar();
            row.setAsignatura(a);
            row.setTitulo(a.getNombre());
            row.setChecked(false);
            rows.add(row);
        }

        return true;
    }

    private List<Asignaturas> asignaturasAExportar(){
        List<Asignaturas> asig = new ArrayList<>();
        for(RowExportar r : rows){
            if(r.isChecked()){
                asig.add(r.getAsignatura());
            }
        }
        return asig;
    }

    private void exportarAsignatura(List<Asignaturas> asig){
        JSONArray listaAsignaturas = new JSONArray();
        try {
            for(Asignaturas a : asig){
                // ASIGNATURA
                JSONObject asignatura = new JSONObject();
                asignatura.put("id", a.getId());
                asignatura.put("nombre", a.getNombre());
//                asignatura.put("nota_final", a.getNota_final());

                // CONFIGURACION
                JSONObject configuracion = new JSONObject();
                configuracion.put("id", a.getConfig().getId());
                configuracion.put("nota_minima", a.getConfig().getMin());
                configuracion.put("nota_maxima", a.getConfig().getMax());
                configuracion.put("nota_aprobacion", a.getConfig().getNotaAprobacion());
                configuracion.put("decimal", a.getConfig().getDecimal());
                configuracion.put("orientacion_asc", a.getConfig().getOrientacionAsc());
                asignatura.put("configuracion", configuracion);

                // TIPO PROMEDIO
                JSONObject tipoPromedio = new JSONObject();
                tipoPromedio.put("id", a.getTp().getId());
                tipoPromedio.put("nombre", a.getTp().getNombre());
                tipoPromedio.put("usa_peso", a.getTp().getUsa_peso());
                asignatura.put("tipoPromedio", tipoPromedio);

                // CONDICION ASIGNATURAS
                JSONArray listaCondiciones = new JSONArray();
                for(CondAsignatura c : a.getCa()){
                    JSONObject condicion = new JSONObject();
                    condicion.put("id", c.getId());
                    condicion.put("condicion", c.getCondicion());
                    condicion.put("chequeado", c.getChequeado());
                    condicion.put("valor", c.getValor());

                    // TIPO PENALIZACION
                    JSONObject penalizacion = new JSONObject();
                    penalizacion.put("id", c.getTp().getId());
                    penalizacion.put("nombre", c.getTp().getNombre());
                    penalizacion.put("cuando_penaliza", c.getTp().getCuando_penaliza());
                    penalizacion.put("requiere_valor", c.getTp().getRequiere_valor());
                    condicion.put("tipoPenalizacion", penalizacion);

                    listaCondiciones.put(condicion);
                }
                asignatura.put("condicion", listaCondiciones);

                // EVALUACIONES
                JSONArray listaEvaluaciones = new JSONArray();
                for(Evaluaciones e : a.getEv()){
                    JSONObject evaluacion = new JSONObject();
                    evaluacion.put("id", e.getId());
                    evaluacion.put("tipo", e.getTipo());
                    evaluacion.put("cantidad", e.getCantidad());
//                    evaluacion.put("nota_evaluacion", e.getNota_evaluacion());
                    evaluacion.put("cond", e.getCond());
                    evaluacion.put("nota_cond", e.getNota_cond());
                    evaluacion.put("peso", e.getPeso());

                    // TIPO PROMEDIO
                    JSONObject promedio = new JSONObject();
                    promedio.put("id", e.getTp().getId());
                    promedio.put("nombre", e.getTp().getNombre());
                    promedio.put("usa_peso", e.getTp().getUsa_peso());
                    evaluacion.put("tipoPromedio", promedio);

                    // NOTAS
                    JSONArray listaNotas = new JSONArray();
                    for(Notas n : e.getNotas()) {
                        JSONObject notas = new JSONObject();
                        notas.put("id", n.getId());
//                        notas.put("nota", n.getNota());
                        notas.put("cond", n.getCond());
                        notas.put("nota_cond", n.getNota_cond());
                        notas.put("peso", n.getPeso());

                        listaNotas.put(notas);
                    }
                    evaluacion.put("notas", listaNotas);

                    listaEvaluaciones.put(evaluacion);
                }
                asignatura.put("evaluacion", listaEvaluaciones);

                listaAsignaturas.put(asignatura);
            }
        } catch (JSONException e){
            Log.e("MYAPP", "unexpected JSON exception", e);
        }

        // ESCRIBIMOS EL ARCHIVO
        try (FileWriter file = new FileWriter(getActivity().getFilesDir() + "/" + NOMBRE_ARCHIVO_EXPORT)) {
            file.write(listaAsignaturas.toString());
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File send = null;
        try {
            send = new File(getActivity().getFilesDir() + "/" + NOMBRE_ARCHIVO_EXPORT);

            // BORRABLE
            Scanner scanner = new Scanner(send);
            while (scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }
            scanner.close();
        } catch (Exception e) {
            Log.e("OPEN", "ERROR AL ABRIR EL ARCHIVO EXPORT");
        }

        if (send != null) {
            // PRUEBA DE EXPORTAR COSAS
            String authority = getActivity().getApplicationContext().getPackageName() + ".provider";
            Context contexto = getActivity().getApplicationContext();
            Uri exportUri = FileProvider.getUriForFile(contexto, authority, send);

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (exportUri != null) {
                sendIntent.putExtra(Intent.EXTRA_STREAM, exportUri);
                sendIntent.setType("text/*");
                getActivity().setResult(Activity.RESULT_OK, sendIntent);
            } else {
                sendIntent.setDataAndType(null, "");
                getActivity().setResult(Activity.RESULT_CANCELED, sendIntent);
            }
            startActivity(sendIntent);
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Problemas para exportar asignaturas", Toast.LENGTH_LONG).show();
        }
    }
}