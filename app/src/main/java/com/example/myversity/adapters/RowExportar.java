package com.example.myversity.adapters;

import com.example.myversity.entidades.Asignaturas;

public class RowExportar {
    private Asignaturas asignatura;
    private String titulo;
    private Boolean checked;

    public Asignaturas getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(Asignaturas asignatura) {
        this.asignatura = asignatura;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Boolean isChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
