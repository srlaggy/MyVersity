package com.example.myversity.entidades.tiposPromedios;

import com.example.myversity.entidades.Evaluaciones;
import com.example.myversity.entidades.Notas;
import com.example.myversity.entidades.TipoPromedio;

import java.util.List;

public class MediaPonderada extends TipoPromedio {
    public MediaPonderada(){
        super("Media ponderada", true);
    }

    @Override
    public Float calcularPromedioAsignaturas(List<Evaluaciones> eval) {
        Float resultado = 0f;
        try{
            Float nota_y_peso = 0f;
            Float suma_pesos = 0f;
            for(int i=0; i<eval.size(); i++){
                Evaluaciones ev = eval.get(i);
                nota_y_peso += (Float.parseFloat(ev.getPeso()) * Float.parseFloat(ev.getNota_evaluacion()));
                suma_pesos += Float.parseFloat(ev.getPeso());
            }
            resultado = nota_y_peso / suma_pesos;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return resultado;
    }

    @Override
    public Float calcularPromedioEvaluaciones(Evaluaciones eval, List<Notas> notas) {
        Float resultado = 0f;
        try{
            Float nota_y_peso = 0f;
            Float suma_pesos = 0f;
            for(int i=0; i<eval.getCantidad(); i++){
                Notas n = notas.get(i);
                nota_y_peso += (Float.parseFloat(n.getPeso()) * Float.parseFloat(n.getNota()));
                suma_pesos += Float.parseFloat(n.getPeso());
            }
            resultado = nota_y_peso / suma_pesos;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return resultado;
    }


}
