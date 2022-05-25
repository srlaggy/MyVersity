package com.example.myversity.entidades;

public class CondAsignatura {
    private Integer id;
    private Integer id_asignaturas;
    private Integer id_tiposPenalizacion;
    private String condicion;
    private Boolean chequeado;
    private String valor;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_asignaturas() {
        return id_asignaturas;
    }

    public void setId_asignaturas(Integer id_asignaturas) {
        this.id_asignaturas = id_asignaturas;
    }

    public Integer getId_tiposPenalizacion() {
        return id_tiposPenalizacion;
    }

    public void setId_tiposPenalizacion(Integer id_tiposPenalizacion) {
        this.id_tiposPenalizacion = id_tiposPenalizacion;
    }

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public Boolean getChequeado() {
        return chequeado;
    }

    public void setChequeado(Boolean chequeado) {
        this.chequeado = chequeado;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
