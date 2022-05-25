package com.example.myversity.entidades;

public class TiposPenalizacion {
    private Integer id;
    private String nombre;
    private Boolean cuando_penaliza;
    private Boolean requiere_valor;

    public TiposPenalizacion(){
    }

    public TiposPenalizacion(String nombre, Boolean cuando_penaliza, Boolean requiere_valor){
        this.nombre = nombre;
        this.cuando_penaliza = cuando_penaliza;
        this.requiere_valor = requiere_valor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getCuando_penaliza() {
        return cuando_penaliza;
    }

    public void setCuando_penaliza(Boolean cuando_penaliza) {
        this.cuando_penaliza = cuando_penaliza;
    }

    public Boolean getRequiere_valor() {
        return requiere_valor;
    }

    public void setRequiere_valor(Boolean requiere_valor) {
        this.requiere_valor = requiere_valor;
    }
}
