package com.example.myversity;

import android.content.Context;
import android.util.Log;
import android.util.FloatMath;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myversity.db.DbCondAsignatura;
import com.example.myversity.db.DbConfigInicial;
import com.example.myversity.db.DbEvaluaciones;
import com.example.myversity.db.DbNotas;
import com.example.myversity.db.DbTiposPenalizacion;
import com.example.myversity.entidades.Asignaturas;
import com.example.myversity.entidades.CondAsignatura;
import com.example.myversity.entidades.ConfiguracionInicial;
import com.example.myversity.entidades.Evaluaciones;
import com.example.myversity.entidades.Notas;
import com.example.myversity.entidades.TiposPenalizacion;

import java.util.ArrayList;

public class ResumenAdaptador extends BaseAdapter {

    private static LayoutInflater inflater = null;

    Context contexto;

    ArrayList<Asignaturas> arrayListAsignaturas;
    DbConfigInicial dbConfigInicial;
    DbEvaluaciones dbEvaluaciones;
    DbNotas dbNotas;
    DbCondAsignatura dbCondAsignatura;
    DbTiposPenalizacion dbTiposPenalizacion;

    public ResumenAdaptador(Context contexto, ArrayList<Asignaturas> arrayListAsignaturas, DbConfigInicial dbConfigInicial, DbEvaluaciones dbEvaluaciones, DbNotas dbNotas, DbCondAsignatura dbCondAsignatura, DbTiposPenalizacion dbTiposPenalizacion){
        this.contexto=contexto;
        this.arrayListAsignaturas = arrayListAsignaturas;
        this.dbConfigInicial = dbConfigInicial;
        this.dbEvaluaciones = dbEvaluaciones;
        this.dbNotas = dbNotas;
        this.dbCondAsignatura = dbCondAsignatura;
        this.dbTiposPenalizacion = dbTiposPenalizacion;

        inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    private boolean CompararNotas(float notaObtenida, float notaObjetivo, boolean orientacion){
        if (orientacion){
            if(notaObtenida >= notaObjetivo){
                return Boolean.TRUE;
            }
            else{
                return Boolean.FALSE;
            }
        }
        else{
            if(notaObtenida <= notaObjetivo){
                return Boolean.TRUE;
            }
            else{
                return Boolean.FALSE;
            }
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final View vista = inflater.inflate(R.layout.item_lista_resumen,null);

        TextView textViewAsignatura = (TextView) vista.findViewById(R.id.resumen_nombre_asignatura);
        TextView textViewEstado = (TextView) vista.findViewById(R.id.resumen_estado_asignatura);
        TextView textViewDescripcion = (TextView) vista.findViewById(R.id.resumen_descripcion_asignatura);
        TextView textViewPromedioActual = (TextView) vista.findViewById(R.id.resumen_promedio_actual);
        TextView textViewEvaluaciones = (TextView) vista.findViewById(R.id.resumen_estado_evaluaciones);
        LinearLayout linearLayoutRectangulo = (LinearLayout) vista.findViewById(R.id.resumen_rectangulo);

        ConfiguracionInicial config;
        ArrayList<CondAsignatura> arrayListCondAsignatura;
        ArrayList<Evaluaciones> arrayListEvaluaciones;
        ArrayList<Notas> arrayListNotas;

        //SI NO HAY NINGUNA ASIGNATURA, NO APARECE NADA EN RESUMEN
        if(arrayListAsignaturas != null) {
            Asignaturas asignatura = arrayListAsignaturas.get(i);
            //SI NOY HAY NINGUNA CONFIGURACION, ESTO SE INDICARA EN LOS RESUMENES DE TODAS LAS ASIGNATURAS
            if(dbConfigInicial.buscarPrimeraConfiguracion() != null){
                //SI NO SE ENCUENTRA UNA CONFIGURACION PARA LA ASIGNATURA, SE USA LA POR DEFECTO
                if (dbConfigInicial.buscarRegistro(asignatura.getId_configInicial()) != null ){
                    config = dbConfigInicial.buscarRegistro(asignatura.getId_configInicial());
                }
                else{
                    config = dbConfigInicial.buscarPrimeraConfiguracion();
                }

                //SI LA ASIGNATURA NO TIENE NOTA FINAL, ESTO SE INDICA EN SU RESUMEN
                if(asignatura.getNota_final() != null){
                    float notaFinal = Float.parseFloat(asignatura.getNota_final());
                    float notaAprobacion = Float.parseFloat(config.getNotaAprobacion());
                    textViewAsignatura.setText(asignatura.getNombre());
                    textViewPromedioActual.append(asignatura.getNota_final());
                    if ((notaFinal >= notaAprobacion && config.getOrientacionAsc()) || (notaFinal <= notaAprobacion && !config.getOrientacionAsc())) {
                        textViewEstado.setText(contexto.getResources().getString(R.string.resumen_estado_aprobado));
                        linearLayoutRectangulo.setBackgroundResource(R.color.green);
                    }
                    else {
                        textViewEstado.setText(contexto.getResources().getString(R.string.resumen_estado_no_aprobado));
                        linearLayoutRectangulo.setBackgroundResource(R.color.reply_orange);
                    }
                }

                else{
                    textViewAsignatura.setText(asignatura.getNombre());
                    textViewEstado.setText(contexto.getResources().getString(R.string.e_resumen_no_nota_final));
                    linearLayoutRectangulo.setBackgroundResource(R.color.reply_blue_600);
                }

                //SE REVISAN LAS CONDICIONES DE REPROBACION SIN CUMPLIR

                //PRIMERO LAS CONDICIONES DE LA ASIGNATURA
                arrayListCondAsignatura = dbCondAsignatura.buscarCondAsignaturasPorIdAsignatura(asignatura.getId());;
                for(int counter = 0; counter < arrayListCondAsignatura.size();counter++){
                    CondAsignatura condAsignatura = arrayListCondAsignatura.get(counter);
                    TiposPenalizacion tipoPenalizacion = dbTiposPenalizacion.buscarTipoPenalizacion(condAsignatura.getId_tiposPenalizacion());
                    if(!tipoPenalizacion.getRequiere_valor() && !condAsignatura.getChequeado()) {
                        textViewEstado.setText(contexto.getResources().getString(R.string.resumen_estado_no_aprobado));
                        linearLayoutRectangulo.setBackgroundResource(R.color.reply_orange);
                        if(textViewDescripcion.getText() == "") textViewDescripcion.setText(contexto.getResources().getString(R.string.resumen_condicion_incumplida) + "\n");
                        textViewDescripcion.append("-" + condAsignatura.getCondicion() + "\n");
                    }
                }

                //LUEGO LAS CONDICIONES DE LAS EVALUACIONES
                arrayListEvaluaciones = dbEvaluaciones.buscarEvaluacionesPorIdAsignatura(asignatura.getId());
                for(int counter = 0; counter < arrayListEvaluaciones.size();counter++){
                    Evaluaciones evaluacion = arrayListEvaluaciones.get(counter);
                    boolean isAlreadyApproved = true;
                    if(evaluacion.getNota_cond() != null && evaluacion.getNota_evaluacion() != null){
                        if((Float.parseFloat(evaluacion.getNota_cond()) > Float.parseFloat(evaluacion.getNota_evaluacion()) && config.getOrientacionAsc()) || (Float.parseFloat(evaluacion.getNota_cond()) < Float.parseFloat(evaluacion.getNota_evaluacion()) && !config.getOrientacionAsc())){
                            textViewEstado.setText(contexto.getResources().getString(R.string.resumen_estado_reprobado));
                            linearLayoutRectangulo.setBackgroundResource(R.color.red);
                            if(textViewDescripcion.getText() == "") textViewDescripcion.setText(contexto.getResources().getString(R.string.resumen_condicion_incumplida) + "\n");
                            textViewDescripcion.append("-" + contexto.getResources().getString(R.string.resumen_condicion_incumplida_promedio) + " " + evaluacion.getTipo() + "\n");
                        }
                    }

                    float sumaMediaActual = 0;
                    float sumaMediaOptimista = 0;
                    float sumaMediaRealista = 0;
                    //SE VE DENTRO DE CADA UNA LAS CONDICIONES DE LAS NOTAS
                    arrayListNotas = dbNotas.buscarNotasPorIdEvaluacion(evaluacion.getId());
                    for(int counterNotas = 0; counterNotas < arrayListNotas.size();counterNotas++){
                        Notas nota = arrayListNotas.get(counterNotas);
                        if(nota.getNota_cond() != null && nota.getNota() == null) isAlreadyApproved = false;
                        if(nota.getNota_cond() != null && nota.getNota() != null){
                            if ((Float.parseFloat(nota.getNota_cond()) > Float.parseFloat(nota.getNota()) && config.getOrientacionAsc()) || (Float.parseFloat(nota.getNota_cond()) < Float.parseFloat(nota.getNota()) && !config.getOrientacionAsc())){
                                textViewEstado.setText(contexto.getResources().getString(R.string.resumen_estado_reprobado));
                                linearLayoutRectangulo.setBackgroundResource(R.color.red);
                                if(textViewDescripcion.getText() == "") textViewDescripcion.setText(contexto.getResources().getString(R.string.resumen_condicion_incumplida) + "\n");
                                textViewDescripcion.append("-" + contexto.getResources().getString(R.string.resumen_condicion_incumplida_nota) + " " + evaluacion.getTipo() + "\n");
                            }
                        }

                        //SE CALCULA SU NOTA FINAL SI EN LAS NOTAS FALTANTES OBTUVIERA: EL MINIMO NECESARIO PARA APROBAR, Y LA NOTA MÁXIMA
                        switch (evaluacion.getId_tipoPromedio()){
                            case 1: case 5: //MEDIA ARIMÉTICA O SUMA
                                if(nota.getNota() != null) {
                                    sumaMediaOptimista += Float.parseFloat(nota.getNota());
                                    sumaMediaRealista += Float.parseFloat(nota.getNota());
                                    sumaMediaActual += Float.parseFloat(nota.getNota());
                                }
                                else{
                                    sumaMediaOptimista += Float.parseFloat(config.getMax());
                                    if(nota.getNota_cond() != null) sumaMediaRealista += Float.parseFloat(nota.getNota_cond());
                                    else if (evaluacion.getNota_cond() != null)sumaMediaRealista += Float.parseFloat(evaluacion.getNota_evaluacion());
                                    else sumaMediaRealista += Float.parseFloat(config.getNotaAprobacion());
                                }
                                break;

                            case 2: //MEDIA PONDERADA
                                if(nota.getNota() != null) {
                                    sumaMediaOptimista += Float.parseFloat(nota.getNota()) * Float.parseFloat(nota.getPeso());
                                    sumaMediaRealista += Float.parseFloat(nota.getNota()) * Float.parseFloat(nota.getPeso());
                                    sumaMediaActual += Float.parseFloat(nota.getNota()) * Float.parseFloat(nota.getPeso());
                                }
                                else{
                                    sumaMediaOptimista += Float.parseFloat(config.getMax()) * Float.parseFloat(nota.getPeso());
                                    if(nota.getNota_cond() != null) sumaMediaRealista *= Float.parseFloat(nota.getNota_cond()) * Float.parseFloat(nota.getPeso());
                                    else if (evaluacion.getNota_cond() != null)sumaMediaRealista *= Float.parseFloat(evaluacion.getNota_evaluacion()) * Float.parseFloat(nota.getPeso());
                                    else sumaMediaRealista *= Float.parseFloat(config.getNotaAprobacion()) * Float.parseFloat(nota.getPeso());
                                }
                                break;

                            case 3: //MEDIA GEOMÉTRICA
                                if(nota.getNota() != null) {
                                    sumaMediaOptimista *= Float.parseFloat(nota.getNota());
                                    sumaMediaRealista *= Float.parseFloat(nota.getNota());
                                    sumaMediaActual *= Float.parseFloat(nota.getNota());
                                }
                                else{
                                    sumaMediaOptimista *= Float.parseFloat(config.getMax());
                                    if(nota.getNota_cond() != null) sumaMediaRealista *= Float.parseFloat(nota.getNota_cond());
                                    else if (evaluacion.getNota_cond() != null)sumaMediaRealista *= Float.parseFloat(evaluacion.getNota_evaluacion());
                                    else sumaMediaRealista *= Float.parseFloat(config.getNotaAprobacion());
                                }
                                break;

                            case 4: //MEDIA CUADRÁTICA
                                if(nota.getNota() != null) {
                                    sumaMediaOptimista += Float.parseFloat(nota.getNota()) * Float.parseFloat(nota.getNota());
                                    sumaMediaRealista += Float.parseFloat(nota.getNota()) * Float.parseFloat(nota.getNota());
                                    sumaMediaActual += Float.parseFloat(nota.getNota()) * Float.parseFloat(nota.getNota());
                                }
                                else{
                                    sumaMediaOptimista *= Float.parseFloat(config.getMax());
                                    if(nota.getNota_cond() != null) sumaMediaRealista += Float.parseFloat(nota.getNota_cond()) * Float.parseFloat(nota.getNota_cond()) ;
                                    else if (evaluacion.getNota_cond() != null)sumaMediaRealista += Float.parseFloat(evaluacion.getNota_evaluacion()) * Float.parseFloat(evaluacion.getNota_evaluacion());
                                    else sumaMediaRealista += Float.parseFloat(config.getNotaAprobacion()) * Float.parseFloat(config.getNotaAprobacion());
                                }
                                break;

                            default:
                                Log.e("switch","default");
                                break;
                        }
                    }

                    float notaFinalRealista;
                    float notaFinalOptimista;
                    float notaFinalActual;
                    switch (evaluacion.getId_tipoPromedio()){

                        case 1:
                            notaFinalRealista = sumaMediaRealista / arrayListNotas.size();
                            notaFinalOptimista = sumaMediaOptimista / arrayListNotas.size();
                            notaFinalActual = sumaMediaActual / arrayListNotas.size();
                            break;

                        case 2: case 5:
                            notaFinalRealista = sumaMediaRealista;
                            notaFinalOptimista = sumaMediaOptimista;
                            notaFinalActual = sumaMediaActual;
                            break;

                        case 3:
                            notaFinalRealista = (float) Math.pow(sumaMediaRealista, (1/arrayListNotas.size()));
                            notaFinalOptimista = (float) Math.pow(sumaMediaOptimista, (1/arrayListNotas.size()));
                            notaFinalActual = (float) Math.pow(sumaMediaActual, (1/arrayListNotas.size()));
                            break;

                        case 4:
                            notaFinalRealista = (float) Math.sqrt(sumaMediaRealista / arrayListNotas.size());
                            notaFinalOptimista = (float) Math.sqrt(sumaMediaOptimista / arrayListNotas.size());
                            notaFinalActual = (float) Math.sqrt(sumaMediaActual / arrayListNotas.size());
                            break;

                        default:
                            notaFinalRealista = -1f;
                            notaFinalOptimista = -1f;
                            notaFinalActual = -1f;
                            break;
                    }

                    float notaFinalRequerida;

                    if(evaluacion.getCond() == Boolean.FALSE) notaFinalRequerida = Float.parseFloat(config.getNotaAprobacion());
                    else notaFinalRequerida = Float.parseFloat(evaluacion.getNota_cond());

                    if ( CompararNotas(notaFinalRealista,notaFinalRequerida,config.getOrientacionAsc())){
                        if(isAlreadyApproved) textViewEvaluaciones.append("*" + evaluacion.getTipo() + ": " + "Ya posees una nota final por sobre el mínimo" + "\n");
                        else textViewEvaluaciones.append("*" + evaluacion.getTipo() + ": " + "Solo necesitas obtener la nota mínima en las evaluaciones restantes" + "\n");
                    }

                    else if(CompararNotas((notaFinalOptimista*0.7f + notaFinalRealista*0.3f),notaFinalRequerida,config.getOrientacionAsc())){
                        if(isAlreadyApproved) textViewEvaluaciones.append("*" + evaluacion.getTipo() + ": " + "Ya posees una nota final por sobre el mínimo" + "\n");
                        else textViewEvaluaciones.append("*" + evaluacion.getTipo() + ": " + "Solo necesitas obtener la nota mínima en las evaluaciones restantes" + "\n");
                    }

                    else if(CompararNotas((notaFinalOptimista*0.3f + notaFinalRealista*0.7f),notaFinalRequerida,config.getOrientacionAsc())){
                        if(isAlreadyApproved) textViewEvaluaciones.append("*" + evaluacion.getTipo() + ": " + "Ya posees una nota final por sobre el mínimo" + "\n");
                        else textViewEvaluaciones.append("*" + evaluacion.getTipo() + ": " + "Solo necesitas obtener la nota mínima en las evaluaciones restantes" + "\n");
                    }

                    else if ( CompararNotas(notaFinalOptimista,notaFinalRequerida,config.getOrientacionAsc())){
                        if(evaluacion.getCond() == Boolean.FALSE) textViewEvaluaciones.append("*" + evaluacion.getTipo() + ": " + "No te será posible obtener una nota por encima del mínimo (Pero no es causal de reprobación)" + "\n");
                        else{
                            textViewEvaluaciones.append("*" + evaluacion.getTipo() + ": " + "No te será posible obtener una nota por encima del mínimo (Causal de reprobación)" + "\n");
                            linearLayoutRectangulo.setBackgroundResource(R.color.red);
                        }
                    }

                    else{
                        textViewEvaluaciones.append("*" + evaluacion.getTipo() + ": " + "No te será posible obtener una nota por encima del mínimo (Causal de reprobación)" + "\n");
                        linearLayoutRectangulo.setBackgroundResource(R.color.red);
                    }

                    textViewEvaluaciones.append(Float.toString(notaFinalActual) + "\n");

                }

            }

            else{
                textViewAsignatura.setText(asignatura.getNombre());
                textViewEstado.setText(contexto.getResources().getString(R.string.e_resumen_no_configuracion));
                linearLayoutRectangulo.setBackgroundResource(R.color.reply_blue_600);
            }

        }

        else{
            textViewAsignatura.setText(contexto.getResources().getString(R.string.e_error));
            textViewEstado.setText(contexto.getResources().getString(R.string.e_resumen_no_asignaturas));
            linearLayoutRectangulo.setBackgroundResource(R.color.reply_blue_600);
        }

        return vista;
    }

    @Override
    public int getCount() {
        return arrayListAsignaturas.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


}
