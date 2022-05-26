package com.example.myversity.entidades.tiposPromedios;

import com.example.myversity.entidades.Evaluaciones;
import com.example.myversity.entidades.Notas;
import com.example.myversity.entidades.TipoPromedio;

import java.util.List;

public class MediaSoloSuma extends TipoPromedio {
    public MediaSoloSuma(){
        super("Suma", false);
    }

    @Override
    public Float calcularPromedioAsignaturas(List<Evaluaciones> eval) {
        Float resultado = 0f;
        try{
            for(Evaluaciones ev : eval){
                resultado += Float.parseFloat(ev.getNota_evaluacion());
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return resultado;
    }

    @Override
    public Float calcularPromedioEvaluaciones(Evaluaciones eval, List<Notas> notas) {
        Float resultado = 0f;
        try{
            for(Notas n : notas){
                resultado += Float.parseFloat(n.getNota());
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return resultado;
    }
}
