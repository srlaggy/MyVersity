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
import java.util.List;

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

        boolean hayCondicionesNoCumplidas = Boolean.FALSE;
        boolean estaReprobado = Boolean.FALSE;
        boolean estaAprobado = Boolean.FALSE;


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

                textViewAsignatura.setText(asignatura.getNombre());

                //SE CREA  LISTA DE LAS NOTAS CALCULADAS DE LAS EVALUACIONES

                ArrayList<Evaluaciones> arrayListNotasFinalesOptimistas = new ArrayList<>();
                ArrayList<Evaluaciones> arrayListNotasFinalesRealistas = new ArrayList<>();
                ArrayList<Evaluaciones> arrayListNotasFinalesActuales = new ArrayList<>();

                //SE REVISAN LAS CONDICIONES DE LAS EVALUACIONES
                arrayListEvaluaciones = dbEvaluaciones.buscarEvaluacionesPorIdAsignatura(asignatura.getId());
                for(int counter = 0; counter < arrayListEvaluaciones.size();counter++){
                    Evaluaciones evaluacion = arrayListEvaluaciones.get(counter);

                    boolean isAlreadyApproved = true;

                    if(evaluacion.getNota_cond() != null && evaluacion.getNota_evaluacion() != null){
                        if(CompararNotas(Float.parseFloat(evaluacion.getNota_evaluacion()),Float.parseFloat(evaluacion.getNota_cond()),config.getOrientacionAsc()) == Boolean.FALSE){
                            estaReprobado = Boolean.TRUE;
                            if(textViewDescripcion.getText() == "") textViewDescripcion.setText(contexto.getResources().getString(R.string.resumen_condicion_incumplida) + "\n");
                            textViewDescripcion.append("-" + contexto.getResources().getString(R.string.resumen_condicion_incumplida_promedio) + " " + evaluacion.getTipo() + "\n");
                        }
                    }if(evaluacion.getNota_cond() != null && evaluacion.getNota_evaluacion() == null) hayCondicionesNoCumplidas = Boolean.TRUE;


                    //SE VE DENTRO DE CADA UNA LAS CONDICIONES DE LAS NOTAS
                    arrayListNotas = dbNotas.buscarNotasPorIdEvaluacion(evaluacion.getId());
                    ArrayList<Notas> arrayListNotasRealista = new ArrayList<>();
                    ArrayList<Notas> arrayListNotasOptimista = new ArrayList<>();
                    ArrayList<Notas> arrayListNotasNoNulas = new ArrayList<>();

                    for(int counterNotas = 0; counterNotas < arrayListNotas.size();counterNotas++){
                        Notas nota = arrayListNotas.get(counterNotas);
                        if(nota.getNota_cond() != null && nota.getNota() == null) isAlreadyApproved = false;
                        if(nota.getNota() == null){
                            Notas auxNota1 = new Notas();
                            auxNota1.setNota(config.getNotaAprobacion());
                            auxNota1.setPeso(nota.getPeso());
                            Notas auxNota2 = new Notas();
                            auxNota2.setNota(config.getMax());
                            auxNota2.setPeso(nota.getPeso());
                            Notas auxNota3 = new Notas();
                            auxNota3.setNota(config.getMin());
                            auxNota3.setPeso(nota.getPeso());
                            arrayListNotasRealista.add(auxNota1);
                            arrayListNotasOptimista.add(auxNota2);
                            arrayListNotasNoNulas.add(auxNota3);
                        }
                        else {
                            arrayListNotasRealista.add(nota);
                            arrayListNotasOptimista.add(nota);
                            arrayListNotasNoNulas.add(nota);
                        }
                        if(nota.getNota_cond() != null && nota.getNota() != null){
                            if(!CompararNotas(Float.parseFloat(nota.getNota()),Float.parseFloat(nota.getNota_cond()),config.getOrientacionAsc())){
                                estaReprobado = Boolean.TRUE;
                                if(textViewDescripcion.getText() == "") textViewDescripcion.setText(contexto.getResources().getString(R.string.resumen_condicion_incumplida) + "\n");
                                textViewDescripcion.append("-" + contexto.getResources().getString(R.string.resumen_condicion_incumplida_nota) + " " + evaluacion.getTipo() + "\n");
                            }
                        }
                        if(nota.getNota_cond() != null && nota.getNota() == null) hayCondicionesNoCumplidas = Boolean.TRUE;
                    }

                    float notaFinalRequerida = 0;
                    float notaFinalOptimista = evaluacion.getTp().calcularPromedioEvaluaciones(evaluacion,arrayListNotasOptimista);
                    float notaFinalRealista = evaluacion.getTp().calcularPromedioEvaluaciones(evaluacion,arrayListNotasRealista);
                    float notaFinalActual = evaluacion.getTp().calcularPromedioEvaluaciones(evaluacion,arrayListNotasNoNulas);

                    if(!config.getDecimal()) notaFinalActual = Math.round(notaFinalActual);

                    if(evaluacion.getCond() == Boolean.FALSE) notaFinalRequerida = Float.parseFloat(config.getNotaAprobacion());
                    else notaFinalRequerida = Float.parseFloat(evaluacion.getNota_cond());

                    textViewEvaluaciones.append("*" + evaluacion.getTipo() + ": " + String.valueOf(notaFinalActual));

                    //Indica si ya se aprobo una evaluacion que tenia condicion
                    if (CompararNotas(notaFinalActual,notaFinalRequerida,config.getOrientacionAsc())) {
                        if(evaluacion.getCond() != Boolean.FALSE && isAlreadyApproved) textViewEvaluaciones.append(contexto.getResources().getString(R.string.resumen_evaluacion_aprobada));
                    }
                    if (evaluacion.getCond() != Boolean.FALSE){
                        if (!CompararNotas(notaFinalOptimista,notaFinalRequerida,config.getOrientacionAsc())){
                            textViewEvaluaciones.append(contexto.getResources().getString(R.string.resumen_evaluacion_insuficiente));
                            estaReprobado = Boolean.TRUE;
                        }
                        else if (!CompararNotas(notaFinalRealista*0.5f + notaFinalOptimista*0.5f,notaFinalRequerida,config.getOrientacionAsc())) textViewEvaluaciones.append(contexto.getResources().getString(R.string.resumen_evaluacion_riesgo_reprobar));
                        else if (!CompararNotas(notaFinalRealista,notaFinalRequerida,config.getOrientacionAsc())) textViewEvaluaciones.append(contexto.getResources().getString(R.string.resumen_evaluacion_riesgo_reprobar_leve));
                    }

                    textViewEvaluaciones.append("\n");

                    Evaluaciones auxEvaluacionOptimista = new Evaluaciones();
                    Evaluaciones auxEvaluacionRealista = new Evaluaciones();
                    Evaluaciones auxEvaluacionActual = new Evaluaciones();
                    auxEvaluacionOptimista.setNota_evaluacion(String.valueOf(notaFinalOptimista));
                    auxEvaluacionOptimista.setPeso(evaluacion.getPeso());
                    auxEvaluacionRealista.setNota_evaluacion(String.valueOf(notaFinalRealista));
                    auxEvaluacionRealista.setPeso(evaluacion.getPeso());
                    auxEvaluacionActual.setNota_evaluacion(String.valueOf(notaFinalActual));
                    auxEvaluacionActual.setPeso(evaluacion.getPeso());
                    arrayListNotasFinalesOptimistas.add(auxEvaluacionOptimista);
                    arrayListNotasFinalesRealistas.add(auxEvaluacionRealista);
                    arrayListNotasFinalesActuales.add(auxEvaluacionActual);

                    Log.d("test",(evaluacion.getTipo()));
                    Log.d("test",String.valueOf(evaluacion.getTp().calcularPromedioEvaluaciones(evaluacion,arrayListNotasOptimista)));
                    Log.d("test",String.valueOf(evaluacion.getTp().calcularPromedioEvaluaciones(evaluacion,arrayListNotasRealista)));
                    Log.d("test",String.valueOf(evaluacion.getTp().calcularPromedioEvaluaciones(evaluacion,arrayListNotasNoNulas)));
                }

                float mediaFinalOptimista = asignatura.getTp().calcularPromedioAsignaturas(arrayListNotasFinalesOptimistas);
                float mediaFinalRealista = asignatura.getTp().calcularPromedioAsignaturas(arrayListNotasFinalesRealistas);
                float mediaFinalActual = asignatura.getTp().calcularPromedioAsignaturas(arrayListNotasFinalesActuales);

                List<CondAsignatura> condAsignaturaList = asignatura.getCa();
                int auxCount;
                for (auxCount = 0; auxCount < condAsignaturaList.size(); auxCount++){
                    CondAsignatura cond = condAsignaturaList.get(auxCount);
                    switch (cond.getId_tiposPenalizacion()){
                        case 2:
                            if(!cond.getChequeado()) mediaFinalActual *= (1f - (Float.parseFloat(cond.getValor()) * 0.01f));
                            break;
                        case 3:
                            if(!cond.getChequeado()) mediaFinalActual -= Float.parseFloat(cond.getValor());
                            break;
                        case 4:
                            if(cond.getChequeado()) mediaFinalActual += Float.parseFloat(cond.getValor());
                            break;
                        default:
                            break;
                    }
                }

                if(!config.getDecimal()) mediaFinalActual = Math.round(mediaFinalActual);

                if(estaReprobado){
                    textViewEstado.setText("Asignatura reprobada");
                }

                else if(!CompararNotas(mediaFinalOptimista,Float.parseFloat(config.getNotaAprobacion()),config.getOrientacionAsc())){
                    textViewEstado.setText(R.string.resumen_asignatura_imposible_aprobar);
                    estaReprobado = Boolean.TRUE;
                }

                else if (CompararNotas(mediaFinalActual, Float.parseFloat(config.getNotaAprobacion()),config.getOrientacionAsc())){
                    estaAprobado = Boolean.TRUE;
                    if(hayCondicionesNoCumplidas) textViewEstado.setText(R.string.resumen_asignatura_todavia_no_aprobada_condiciones);
                    textViewEstado.setText(R.string.resumen_asignatura_ya_aprobada);
                }
                else if(CompararNotas(mediaFinalRealista,Float.parseFloat(config.getNotaAprobacion()),config.getOrientacionAsc() )){
                    textViewEstado.setText(R.string.resumen_asignatura_todavia_no_aprobada);
                }
                else if(CompararNotas(mediaFinalRealista*0.5f + mediaFinalOptimista*0.5f,Float.parseFloat(config.getNotaAprobacion()),config.getOrientacionAsc() )){
                    textViewEstado.setText(R.string.resumen_asignatura_riesgo_reprobar);
                }
                else if(CompararNotas(mediaFinalRealista*0.2f + mediaFinalOptimista*0.8f,Float.parseFloat(config.getNotaAprobacion()),config.getOrientacionAsc() )){
                    textViewEstado.setText(R.string.resumen_asignatura_riesgo_reprobar_alto);
                }

                arrayListCondAsignatura = dbCondAsignatura.buscarCondAsignaturasPorIdAsignatura(asignatura.getId());

                for(int counter = 0; counter < arrayListCondAsignatura.size();counter++){
                    CondAsignatura condAsignatura = arrayListCondAsignatura.get(counter);
                    TiposPenalizacion tipoPenalizacion = dbTiposPenalizacion.buscarTipoPenalizacion(condAsignatura.getId_tiposPenalizacion());
                    if(!tipoPenalizacion.getRequiere_valor() && !condAsignatura.getChequeado()) {
                        hayCondicionesNoCumplidas = Boolean.TRUE;
                        if(textViewDescripcion.getText() == "") textViewDescripcion.setText(contexto.getResources().getString(R.string.resumen_condicion_incumplida) + "\n");
                        textViewDescripcion.append("-" + condAsignatura.getCondicion() + "\n");
                    }
                }

                if(estaReprobado){
                    //linearLayoutRectangulo.setBackgroundResource(R.color.red);
                    linearLayoutRectangulo.setBackground(contexto.getDrawable(R.drawable.roundstyle_error));
                }
                else if (hayCondicionesNoCumplidas){
                    linearLayoutRectangulo.setBackground(contexto.getDrawable(R.drawable.roundstyle_risk));
                }
                else if (estaAprobado){
                    linearLayoutRectangulo.setBackground(contexto.getDrawable(R.drawable.roundstyle_aprobado));
                }
                else linearLayoutRectangulo.setBackground(contexto.getDrawable(R.drawable.roundstyle_risk));

                textViewPromedioActual.append(String.valueOf(mediaFinalActual));

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
