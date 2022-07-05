package com.example.myversity.adapters;
import com.example.myversity.entidades.Notas;

public class RowNotasEvaluacion {
    private Notas nota;
    private String titulo;
    private String valor;

    public Notas getNota() {
        return nota;
    }

    public void setNota(Notas nota) {
        this.nota = nota;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getValor() {return valor;}

    public void setValor(String valor) {
        this.valor = valor;
    }
}
