package com.example.myversity.entidades;

public class TipoPromedio {
    private Integer id;
    private String nombre;
    private Boolean usa_peso;

    public TipoPromedio(){
    }

    public  TipoPromedio(String nombre, Boolean usa_peso){
        this.nombre = nombre;
        this.usa_peso = usa_peso;
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

    public Boolean getUsa_peso() {
        return usa_peso;
    }

    public void setUsa_peso(Boolean usa_peso) {
        this.usa_peso = usa_peso;
    }
}
