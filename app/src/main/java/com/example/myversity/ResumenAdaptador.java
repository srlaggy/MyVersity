package com.example.myversity;

import android.content.Context;
import android.util.Log;
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final View vista = inflater.inflate(R.layout.item_lista_resumen,null);

        TextView textViewAsignatura = (TextView) vista.findViewById(R.id.resumen_nombre_asignatura);
        TextView textViewEstado = (TextView) vista.findViewById(R.id.resumen_estado_asignatura);
        TextView textViewDescripcion = (TextView) vista.findViewById(R.id.resumen_descripcion_asignatura);
        TextView textViewPromedioActual = (TextView) vista.findViewById(R.id.resumen_promedio_actual);
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
                        textViewDescripcion.append(contexto.getResources().getString(R.string.resumen_condicion_incumplida) + " " + condAsignatura.getCondicion() + "\n");
                    }
                }

                //LUEGO LAS CONDICIONES DE LAS EVALUACIONES
                arrayListEvaluaciones = dbEvaluaciones.buscarEvaluacionesPorIdAsignatura(asignatura.getId());
                for(int counter = 0; counter < arrayListEvaluaciones.size();counter++){
                    Evaluaciones evaluacion = arrayListEvaluaciones.get(counter);
                    if(evaluacion.getNota_cond() != null && evaluacion.getNota_evaluacion() != null){
                        if((Float.parseFloat(evaluacion.getNota_cond()) > Float.parseFloat(evaluacion.getNota_evaluacion()) && config.getOrientacionAsc()) || (Float.parseFloat(evaluacion.getNota_cond()) < Float.parseFloat(evaluacion.getNota_evaluacion()) && !config.getOrientacionAsc())){
                            textViewEstado.setText(contexto.getResources().getString(R.string.resumen_estado_reprobado));
                            linearLayoutRectangulo.setBackgroundResource(R.color.red);
                            textViewDescripcion.append(contexto.getResources().getString(R.string.resumen_condicion_incumplida) + " " + contexto.getResources().getString(R.string.resumen_conducion_incumplida_promedio) + " " + evaluacion.getTipo() + "\n");
                        }
                    }

                    float sumaMediaOptimista = 0;
                    float sumaMediaRealista = 0;
                    //SE VE DENTRO DE CADA UNA LAS CONDICIONES DE LAS NOTAS
                    arrayListNotas = dbNotas.buscarNotasPorIdEvaluacion(evaluacion.getId());
                    for(int counterNotas = 0; counterNotas < arrayListNotas.size();counterNotas++){
                        Notas nota = arrayListNotas.get(counterNotas);
                        if(nota.getNota_cond() != null && nota.getNota() != null){
                            if ((Float.parseFloat(nota.getNota_cond()) > Float.parseFloat(nota.getNota()) && config.getOrientacionAsc()) || (Float.parseFloat(nota.getNota_cond()) < Float.parseFloat(nota.getNota()) && !config.getOrientacionAsc())){
                                textViewEstado.setText(contexto.getResources().getString(R.string.resumen_estado_reprobado));
                                linearLayoutRectangulo.setBackgroundResource(R.color.red);
                                textViewDescripcion.append(contexto.getResources().getString(R.string.resumen_condicion_incumplida) + " " + contexto.getResources().getString(R.string.resumen_conducion_incumplida_nota) + " " + evaluacion.getTipo() + "\n");
                            }
                        }

                    }

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
