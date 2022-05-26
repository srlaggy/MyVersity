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

import com.example.myversity.db.DbConfigInicial;
import com.example.myversity.entidades.Asignaturas;
import com.example.myversity.entidades.ConfiguracionInicial;

import java.util.ArrayList;

public class ResumenAdaptador extends BaseAdapter {

    private static LayoutInflater inflater = null;

    Context contexto;

    ArrayList<Asignaturas> arrayListAsignaturas;
    DbConfigInicial dbConfigInicial = new DbConfigInicial(contexto);

    public ResumenAdaptador(Context contexto, ArrayList<Asignaturas> arrayListAsignaturas){
        this.contexto=contexto;
        this.arrayListAsignaturas = arrayListAsignaturas;

        inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final View vista = inflater.inflate(R.layout.item_lista_resumen,null);

        TextView textViewAsignatura = (TextView) vista.findViewById(R.id.resumen_nombre_asignatura);
        TextView textViewEstado = (TextView) vista.findViewById(R.id.resumen_estado_asignatura);
        LinearLayout linearLayoutRectangulo = (LinearLayout) vista.findViewById(R.id.resumen_rectangulo);
        ConfiguracionInicial config;

        //SI NO HAY NINGUNA ASIGNATURA, NO APARECE NADA EN RESUMEN
        if(arrayListAsignaturas != null) {
            Asignaturas asignatura = arrayListAsignaturas.get(i);
            //SI NOY HAY NINGUNA CONFIGURACION, ESTO SE INDICARA EN LOS RESUMENES DE TODAS LAS ASIGNATURAS
            if(dbConfigInicial != null){
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
                    if (notaFinal > notaAprobacion) {
                        textViewEstado.setText(contexto.getResources().getString(R.string.resumen_estado_aprobado));
                        linearLayoutRectangulo.setBackgroundResource(R.color.teal_200);
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
