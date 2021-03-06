package com.example.myversity.entidades;

import java.util.List;

public class Evaluaciones {
    private Integer id;
    private Integer id_asignaturas;
    private Integer id_tipoPromedio;
    private String tipo;
    private Integer cantidad;
    private String nota_evaluacion;
    private Boolean cond;
    private String nota_cond;
    private String peso;
    // EXTRAS
    private List<Notas> notas;
    private TipoPromedio tp;

    public Evaluaciones(){
    }

    public Evaluaciones(Integer id_asignaturas, Integer id_tipoPromedio, String tipo, Integer cantidad, Boolean cond){
        this.id_asignaturas = id_asignaturas;
        this.id_tipoPromedio = id_tipoPromedio;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.cond = cond;
    }

    public Evaluaciones(Integer id_asignaturas, Integer id_tipoPromedio, String tipo, Integer cantidad, Boolean cond, String peso){
        this.id_asignaturas = id_asignaturas;
        this.id_tipoPromedio = id_tipoPromedio;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.cond = cond;
        this.peso = peso;
    }

    public Evaluaciones(Integer id_asignaturas, Integer id_tipoPromedio, String tipo, Integer cantidad, Boolean cond, String nota_cond, String peso){
        this.id_asignaturas = id_asignaturas;
        this.id_tipoPromedio = id_tipoPromedio;
        this.tipo = tipo;
        this.cantidad = cantidad;
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

    public Integer getId_asignaturas() {
        return id_asignaturas;
    }

    public void setId_asignaturas(Integer id_asignaturas) {
        this.id_asignaturas = id_asignaturas;
    }

    public Integer getId_tipoPromedio() {
        return id_tipoPromedio;
    }

    public void setId_tipoPromedio(Integer id_tipoPromedio) {
        this.id_tipoPromedio = id_tipoPromedio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getNota_evaluacion() {
        return nota_evaluacion;
    }

    public void setNota_evaluacion(String nota_evaluacion) {
        this.nota_evaluacion = nota_evaluacion;
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

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public List<Notas> getNotas() {
        return notas;
    }

    public void setNotas(List<Notas> notas) {
        this.notas = notas;
    }

    public TipoPromedio getTp() {
        return tp;
    }

    public void setTp(TipoPromedio tp) {
        this.tp = tp;
    }

    @Override
    public String toString() {
        String aux1 = "EVALUACION ID=" + id + "\n" +
                "-> Nombre: " + tipo + "\n" +
                "-> Nota: " + nota_evaluacion +
                "\n";
        String aux2 = "NOTAS:";
        for(Notas n: notas){
            aux2 += "\n" + "ID: " + n.getId() + " -- " + "NOTA: " + n.getNota();
        }
        return aux1+aux2;
    }
}
