package com.example.myversity.entidades.tiposPromedios;

import com.example.myversity.entidades.Evaluaciones;
import com.example.myversity.entidades.Notas;
import com.example.myversity.entidades.TipoPromedio;

import java.util.List;

public class MediaGeometrica extends TipoPromedio {
    public MediaGeometrica(){
        super("Media geométrica", false);
    }

    @Override
    public Float calcularPromedioAsignaturas(List<Evaluaciones> eval) {
        Float resultado = 0f;
        try{
            Integer cantidadEval = eval.size();
            Float enesimo = 1.0f / cantidadEval;
            Float suma = 1f;
            for(Evaluaciones ev : eval){
                suma *= Float.parseFloat(ev.getNota_evaluacion());
            }
            resultado = (float) Math.pow(suma, enesimo);
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
            Float enesimo = 1.0f / cantidadNotas;
            Float suma = 1f;
            for(Notas n : notas){
                suma *= Float.parseFloat(n.getNota());
            }
            resultado = (float) Math.pow(suma, enesimo);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return resultado;
    }
}
