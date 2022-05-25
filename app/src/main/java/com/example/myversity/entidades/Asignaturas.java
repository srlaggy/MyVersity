package com.example.myversity.entidades;

public class Asignaturas {
    private Integer id;
    private Integer id_configInicial;
    private Integer id_tipoPromedio;
    private String nombre;
    private String nota_final;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_configInicial() {
        return id_configInicial;
    }

    public void setId_configInicial(Integer id_configInicial) {
        this.id_configInicial = id_configInicial;
    }

    public Integer getId_tipoPromedio() {
        return id_tipoPromedio;
    }

    public void setId_tipoPromedio(Integer id_tipoPromedio) {
        this.id_tipoPromedio = id_tipoPromedio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNota_final() {
        return nota_final;
    }

    public void setNota_final(String nota_final) {
        this.nota_final = nota_final;
    }
}
