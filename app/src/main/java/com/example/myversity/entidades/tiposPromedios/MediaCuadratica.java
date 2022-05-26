package com.example.myversity.entidades.tiposPromedios;

import com.example.myversity.entidades.Evaluaciones;
import com.example.myversity.entidades.Notas;
import com.example.myversity.entidades.TipoPromedio;

import java.util.List;

public class MediaCuadratica extends TipoPromedio {
    public MediaCuadratica(){
        super("Media Cuadrática", false);
    }

    @Override
    public Float calcularPromedioAsignaturas(List<Evaluaciones> eval) {
        Float resultado = 0f;
        try{
            Integer cantidadEval = eval.size();
            Float suma = 0f;
            for(Evaluaciones ev : eval){
                suma += (float) Math.pow(Float.parseFloat(ev.getNota_evaluacion()), 2);
            }
            resultado = (float) Math.pow((suma / cantidadEval), 0.5);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return resultado;
    }

    @Override
    public Float calcularPromedioEvaluaciones(Evaluaciones eval, List<Notas> notas) {
        Float resultado = 0f;
        try{
            Integer cantidadNotas = eval.getCantidad();
            Float suma = 0f;
            for(Notas n : notas){
                suma += (float) Math.pow(Float.parseFloat(n.getNota()), 2);
            }
            resultado = (float) Math.pow((suma / cantidadNotas), 0.5);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return resultado;
    }
}
