package com.example.myversity.entidades;

import java.util.List;

public class Asignaturas {
    private Integer id;
    private Integer id_configInicial;
    private Integer id_tipoPromedio;
    private String nombre;
    private String nota_final;
    // EXTRAS
    private ConfiguracionInicial config;
    private TipoPromedio tp;
    private List<CondAsignatura> ca;
    private List<Evaluaciones> ev;

    // GETTERS AND SETTERS
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

    public ConfiguracionInicial getConfig() {
        return config;
    }

    public void setConfig(ConfiguracionInicial config) {
        this.config = config;
    }

    public TipoPromedio getTp() {
        return tp;
    }

    public void setTp(TipoPromedio tp) {
        this.tp = tp;
    }

    public List<CondAsignatura> getCa() {
        return ca;
    }

    public void setCa(List<CondAsignatura> ca) {
        this.ca = ca;
    }

    public List<Evaluaciones> getEv() {
        return ev;
    }

    public void setEv(List<Evaluaciones> ev) {
        this.ev = ev;
    }

    @Override
    public String toString() {
        return "ASIGNATURA ID=" + id + "\n" +
                "-> Nombre: " + nombre + "\n" +
                "-> Nota: " + nota_final;
    }
}
