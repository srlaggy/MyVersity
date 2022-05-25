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

import com.example.myversity.entidades.ConfiguracionInicial;

import java.util.ArrayList;

public class ResumenAdaptador extends BaseAdapter {

    private static LayoutInflater inflater = null;

    Context contexto;
    String[][] datos;
    ConfiguracionInicial config;

    public ResumenAdaptador(Context contexto, String[][] datos, ConfiguracionInicial config){
        this.contexto=contexto;
        this.datos = datos;
        this.config = config;

        inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final View vista = inflater.inflate(R.layout.item_lista_resumen,null);

        TextView asignatura = (TextView) vista.findViewById(R.id.resumen_nombre_asignatura);
        TextView estado = (TextView) vista.findViewById(R.id.resumen_estado_asignatura);
        LinearLayout rectangulo = (LinearLayout) vista.findViewById(R.id.resumen_rectangulo);

        if(config != null) {

            float notaAprobacion = Float.parseFloat(config.getNotaAprobacion());

            asignatura.setText(datos[i][0]);
            if (Integer.parseInt(datos[i][1]) > notaAprobacion) {
                estado.setText("Has aprobado");
                rectangulo.setBackgroundResource(R.color.teal_200);
            } else {
                estado.setText("Todavía no apruebas");
                rectangulo.setBackgroundResource(R.color.reply_orange);
            }
        }

        else{
            asignatura.setText(datos[i][0]);
            estado.setText("No se ha podido acceder a la configuración de esta asignatura");
            rectangulo.setBackgroundResource(R.color.reply_blue_600);
        }

        return vista;
    }

    @Override
    public int getCount() {
        return datos.length;
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