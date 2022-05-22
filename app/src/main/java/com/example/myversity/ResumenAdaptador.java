package com.example.myversity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ResumenAdaptador extends BaseAdapter {

    private static LayoutInflater inflater = null;

    Context contexto;
    String[][] datos;

    public ResumenAdaptador(Context contexto, String[][] datos){
        this.contexto=contexto;
        this.datos = datos;

        inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final View vista = inflater.inflate(R.layout.item_lista_resumen,null);

        TextView asignatura = (TextView) vista.findViewById(R.id.nombre_asignatura);
        TextView estado = (TextView) vista.findViewById(R.id.estado_asignatura);

        asignatura.setText(datos[i][0]);
        estado.setText(datos[i][1]);

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
