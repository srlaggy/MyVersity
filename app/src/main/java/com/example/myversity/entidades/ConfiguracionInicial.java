package com.example.myversity.entidades;

import java.util.Objects;

public class ConfiguracionInicial {
    private int id;
    private String min;
    private String max;
    private String notaAprobacion;
    private Boolean decimal;
    private Boolean orientacionAsc;

    public ConfiguracionInicial(){
    }

    public ConfiguracionInicial(String min, String max, String notaAprobacion, Boolean decimal, Boolean orientacionAsc){
        this.min = min;
        this.max = max;
        this.notaAprobacion = notaAprobacion;
        this.decimal = decimal;
        this.orientacionAsc = orientacionAsc;
    }

    public ConfiguracionInicial(int id, String min, String max, String notaAprobacion, Boolean decimal, Boolean orientacionAsc){
        this.id = id;
        this.min = min;
        this.max = max;
        this.notaAprobacion = notaAprobacion;
        this.decimal = decimal;
        this.orientacionAsc = orientacionAsc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getNotaAprobacion() {
        return notaAprobacion;
    }

    public void setNotaAprobacion(String notaAprobacion) {
        this.notaAprobacion = notaAprobacion;
    }

    public Boolean getDecimal() {
        return decimal;
    }

    public void setDecimal(Boolean decimal) {
        this.decimal = decimal;
    }

    public Boolean getOrientacionAsc() {
        return orientacionAsc;
    }

    public void setOrientacionAsc(Boolean orientacionAsc) {
        this.orientacionAsc = orientacionAsc;
    }

    @Override
    public String toString() {
        String dec = (decimal) ? "SI" : "NO";
        String ori = (orientacionAsc) ? "ASCENDENTE" : "DESCENDENTE";
        return "REGISTRO " + id + ":\n" +
                "RANGO DE NOTAS: " + min + " - " + max + "\n" +
                "NOTA DE APROBACION: " + notaAprobacion + "\n" +
                "DECIMAL: " + dec + "\n" +
                "ORIENTACION: " + ori + "\n";
    }

    @Override
    public boolean equals(Object o) {
        ConfiguracionInicial that = (ConfiguracionInicial) o;
        return Objects.equals(min, that.min) && Objects.equals(max, that.max) && Objects.equals(notaAprobacion, that.notaAprobacion) && Objects.equals(decimal, that.decimal) && Objects.equals(orientacionAsc, that.orientacionAsc);
    }
}
