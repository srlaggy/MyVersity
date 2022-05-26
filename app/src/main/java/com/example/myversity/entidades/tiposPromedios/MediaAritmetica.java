package com.example.myversity.entidades.tiposPromedios;

import com.example.myversity.entidades.Evaluaciones;
import com.example.myversity.entidades.Notas;
import com.example.myversity.entidades.TipoPromedio;

import java.util.List;

public class MediaAritmetica extends TipoPromedio {
    // CONSTRUCTOR SETEA VALORES DE NOMBRE Y USA_PESO
    public MediaAritmetica(){
        super("Media aritmética", false);
    }

    @Override
    public Float calcularPromedioAsignaturas(List<Evaluaciones> eval) {
        Float resultado = 0f;
        try{
            Integer cantidadEval = eval.size();
            Float suma = 0f;
            for(Evaluaciones ev : eval){
                suma += Float.parseFloat(ev.getNota_evaluacion());
            }
            resultado = suma / cantidadEval;
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
                suma += Float.parseFloat(n.getNota());
            }
            resultado = suma / cantidadNotas;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return resultado;
    }
}
