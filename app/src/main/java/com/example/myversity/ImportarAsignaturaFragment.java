package com.example.myversity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myversity.db.DbAsignaturas;
import com.example.myversity.db.DbCondAsignatura;
import com.example.myversity.db.DbConfigInicial;
import com.example.myversity.db.DbEvaluaciones;
import com.example.myversity.db.DbNotas;
import com.example.myversity.entidades.Asignaturas;
import com.example.myversity.entidades.CondAsignatura;
import com.example.myversity.entidades.ConfiguracionInicial;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ImportarAsignaturaFragment extends Fragment {
    public ImportarAsignaturaFragment() {
    }

    public static ImportarAsignaturaFragment newInstance() {
        ImportarAsignaturaFragment fragment = new ImportarAsignaturaFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_importar_asignatura, container, false);
        return view;
    }

    public static Boolean importarAsignaturas(String ruta, Activity activity){
        JSONParser jsonParser = new JSONParser();
        Context context = activity.getApplicationContext();
        try (FileReader reader = new FileReader(ruta)) {
            Object obj = jsonParser.parse(reader);
            JSONArray listaAsignaturas = (JSONArray) obj;

            if(listaAsignaturas.isEmpty()){
                Toast.makeText(activity.getApplicationContext(), "No existen asignaturas", Toast.LENGTH_LONG).show();
                return false;
            } else {
                // CASO DONDE EL ARCHIVO ESTA CORRECTO
                for(int i=0; i<listaAsignaturas.size(); i++) {
                    Object asig = listaAsignaturas.get(i);
                    JSONObject asigObj = (JSONObject) asig;

                    // DATOS JSON -> ASIGNATURA
                    Long id = (Long) asigObj.get("id");
                    String nombre = (String) asigObj.get("nombre");
//                    System.out.println("-----------------------------------------");
//                    System.out.println("ASIGNATURA: " + nombre + " | ID: " + id.toString());

                    // DATOS JSON -> CONFIGURACION
                    JSONObject configObj = (JSONObject) asigObj.get("configuracion");
                    Long idConfig = (Long) configObj.get("id");
                    String min = (String) configObj.get("nota_minima");
                    String max = (String) configObj.get("nota_maxima");
                    String notaAprobacion = (String) configObj.get("nota_aprobacion");
                    Boolean decimal = (Boolean) configObj.get("decimal");
                    Boolean orientacion = (Boolean) configObj.get("orientacion_asc");
//                    System.out.println("CONFIGURACION: ID = " + idConfig.toString() + " | Rango = " + min + " - " + max + " | Nota aprobacion = " + notaAprobacion);
//                    System.out.println("               Decimal: " + decimal.toString() + " | Orientacion ascendente: " + orientacion.toString());

                    // INSERTAR BD -> CONFIGURACION
                    DbConfigInicial dbConfigInicial = new DbConfigInicial(activity.getApplicationContext());
                    ArrayList<ConfiguracionInicial> configAux = dbConfigInicial.buscarConfiguraciones();
                    if(configAux.isEmpty()){
                        Long auxNoUsar = dbConfigInicial.insertarConfigInicial(min, max, notaAprobacion, decimal, orientacion);
                    }
                    idConfig = dbConfigInicial.insertarConfigInicial(min, max, notaAprobacion, decimal, orientacion);
                    dbConfigInicial.close();
                    if(idConfig == 0){
                        Toast.makeText(activity.getApplicationContext(), "Problema al importar una asignatura", Toast.LENGTH_LONG).show();
                        return false;
                    }

                    // DATOS JSON -> TIPO PROMEDIO (NO INSERTAR)
                    JSONObject promedioObj = (JSONObject) asigObj.get("tipoPromedio");
                    Long idProm = (Long) promedioObj.get("id");
                    String nombreProm = (String) promedioObj.get("nombre");
                    Boolean usa_peso = (Boolean) promedioObj.get("usa_peso");
//                    System.out.println("TIPO PROMEDIO: " + nombreProm + " | ID = " + idProm.toString() + " | Usa peso? " + usa_peso.toString());

                    // INSERTAR BD -> ASIGNATURA
                    DbAsignaturas dbAsignaturas = new DbAsignaturas(context);
                    ArrayList<Asignaturas> asignaturas = dbAsignaturas.buscarAsignaturas();
                    Boolean repetida = false;
                    for(Asignaturas as : asignaturas){
                        if(Objects.equals(as.getNombre(), nombre)){
                            repetida = true;
                            break;
                        }
                    }
                    if(repetida){
                        dbAsignaturas.close();
                        continue;
                    }
                    id = dbAsignaturas.crearAsignatura(idConfig.intValue(), idProm.intValue(), nombre);
                    dbAsignaturas.close();
                    if(id == 0){
                        Toast.makeText(activity.getApplicationContext(), "Problema al importar una asignatura", Toast.LENGTH_LONG).show();
                        return false;
                    }

                    JSONArray listaCondiciones = (JSONArray) asigObj.get("condicion");
                    if (!listaCondiciones.isEmpty()) {
//                        System.out.println("CONDICIONES:");

                        for(int j=0; j<listaCondiciones.size(); j++){
                            Object cond = listaCondiciones.get(j);

                            // DATOS JSON -> CONDICIONES
                            JSONObject condObj = (JSONObject) cond;
                            Long idCond = (Long) condObj.get("id");
                            String condicion = (String) condObj.get("condicion");
                            Boolean chequeado = (Boolean) condObj.get("chequeado");
                            String valor = (String) condObj.get("valor");

                            // DATOS JSON -> TIPO PENALIZACION (NO INSERTAR)
                            JSONObject penalizacionObj = (JSONObject) condObj.get("tipoPenalizacion");
                            Long idPenal = (Long) penalizacionObj.get("id");
                            String nombrePenal = (String) penalizacionObj.get("nombre");
                            Boolean cuando_penaliza = (Boolean) penalizacionObj.get("cuando_penaliza");
                            Boolean requiere_valor = (Boolean) penalizacionObj.get("requiere_valor");

//                            System.out.println("    -> NOMBRE: " + condicion + " | PENALIZACION: " + nombrePenal);

                            // INSERTAR BD -> CONDICIONES
                            DbCondAsignatura dbCondAsignatura = new DbCondAsignatura(context);
                            idCond = dbCondAsignatura.insertarCondAsignatura(new CondAsignatura(id.intValue(), idPenal.intValue(), condicion, chequeado, valor));
                            dbCondAsignatura.close();
                            if(idCond == 0){
                                Toast.makeText(activity.getApplicationContext(), "Problema al importar una asignatura", Toast.LENGTH_LONG).show();
                                DbAsignaturas dbAsignaturas1 = new DbAsignaturas(context);
                                dbAsignaturas1.borrarAsignatura(id.intValue(), idConfig.intValue());
                                dbAsignaturas1.close();
                                return false;
                            }
                        }

                    }

                    JSONArray listaEvaluaciones = (JSONArray) asigObj.get("evaluacion");
                    if (!listaEvaluaciones.isEmpty()) {
//                        System.out.println("EVALUACIONES:");

                        for(int k=0; k<listaEvaluaciones.size(); k++){
                            Object eval = listaEvaluaciones.get(k);

                            // DATOS JSON -> EVALUACIONES
                            JSONObject evalObj = (JSONObject) eval;
                            Long idEval = (Long) evalObj.get("id");
                            String tipo = (String) evalObj.get("tipo");
                            Long cantidad = (Long) evalObj.get("cantidad");
                            Boolean cond = (Boolean) evalObj.get("cond");
                            String nota_cond = (String) evalObj.get("nota_cond");
                            String peso = (String) evalObj.get("peso");
                            //                                        System.out.println("    -> NOMBRE: " + tipo + " | CANTIDAD: " + cantidad.toString() + " | peso? " + ((peso!=0) ? peso.toString() : "NO"));
//                            System.out.println("    -> NOMBRE: " + tipo + " | CANTIDAD: " + cantidad.toString() + " | peso? " + peso);

                            // DATOS JSON -> TIPO PROMEDIO (NO INSERTAR)
                            JSONObject promedioObj2 = (JSONObject) evalObj.get("tipoPromedio");
                            Long idProm2 = (Long) promedioObj2.get("id");
                            String nombreProm2 = (String) promedioObj2.get("nombre");
                            Boolean usa_peso2 = (Boolean) promedioObj2.get("usa_peso");
//                            System.out.println("         -> TIPO PROMEDIO: " + nombreProm2 + " | ID = " + idProm2.toString() + " | Usa peso? " + usa_peso2.toString());

                            // INSERTAR BD -> EVALUACIONES
                            DbEvaluaciones dbEvaluaciones = new DbEvaluaciones(context);
                            idEval = dbEvaluaciones.insertarEvaluacionesFull(id.intValue(), idProm2.intValue(), tipo, cantidad.intValue(), cond, nota_cond, peso);
                            dbEvaluaciones.close();
                            if(idEval == 0){
                                Toast.makeText(activity.getApplicationContext(), "Problema al importar una asignatura", Toast.LENGTH_LONG).show();
                                DbAsignaturas dbAsignaturas1 = new DbAsignaturas(context);
                                dbAsignaturas1.borrarAsignatura(id.intValue(), idConfig.intValue());
                                dbAsignaturas1.close();
                                return false;
                            }

                            JSONArray listaNotas = (JSONArray) evalObj.get("notas");
                            if (!listaNotas.isEmpty()) {
//                                System.out.println("         -> NOTAS:");

                                for(int l=0; l<listaNotas.size(); l++){
                                    Object n = listaNotas.get(l);

                                    // DATOS JSON -> NOTAS
                                    JSONObject notaObj = (JSONObject) n;
                                    Long idNota = (Long) notaObj.get("id");
                                    Boolean cond2 = (Boolean) notaObj.get("cond");
                                    String nota_cond2 = (String) notaObj.get("nota_cond");
                                    String peso2 = (String) notaObj.get("peso");
                                    //                                                System.out.println("           -> NOTA " + idNota.toString() + " | peso? " +  ((peso2!=0) ? peso2.toString() : "NO"));
//                                    System.out.println("           -> NOTA " + idNota.toString() + " | peso? " + peso2);

                                    // INSERTAR BD -> NOTAS
                                    DbNotas dbNotas = new DbNotas(context);
                                    idNota = dbNotas.insertarNotaFull(idEval.intValue(), cond2, nota_cond2, peso2);
                                    dbNotas.close();
                                    if(idNota == 0){
                                        Toast.makeText(activity.getApplicationContext(), "Problema al importar una asignatura", Toast.LENGTH_LONG).show();
                                        DbAsignaturas dbAsignaturas1 = new DbAsignaturas(context);
                                        dbAsignaturas1.borrarAsignatura(id.intValue(), idConfig.intValue());
                                        dbAsignaturas1.close();
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                    // FIN ASIGNATURA
                }

            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
            Toast.makeText(activity.getApplicationContext(), "Problema al importar asignatura", Toast.LENGTH_LONG).show();
            return false;
        }
        Toast.makeText(activity.getApplicationContext(), "Importación exitosa!", Toast.LENGTH_LONG).show();
        return true;
    }
}