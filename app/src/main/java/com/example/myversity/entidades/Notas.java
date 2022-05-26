package com.example.myversity.entidades;

public class Notas {
    private Integer id;
    private Integer id_evaluaciones;
    private String nota;
    private Boolean cond;
    private String nota_cond;
    private Float peso;

    public Notas(){
    }

    public Notas(Integer id, Integer id_evaluaciones, String nota, Boolean cond, String nota_cond, Float peso){
        this.id = id;
        this.id_evaluaciones = id_evaluaciones;
        this.nota = nota;
        this.cond = cond;
        this.nota_cond = nota_cond;
        this.peso = peso;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_evaluaciones() {
        return id_evaluaciones;
    }

    public void setId_evaluaciones(Integer id_evaluaciones) {
        this.id_evaluaciones = id_evaluaciones;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public Boolean getCond() {
        return cond;
    }

    public void setCond(Boolean cond) {
        this.cond = cond;
    }

    public String getNota_cond() {
        return nota_cond;
    }

    public void setNota_cond(String nota_cond) {
        this.nota_cond = nota_cond;
    }

    public Float getPeso() {
        return peso;
    }

    public void setPeso(Float peso) {
        this.peso = peso;
    }
}
