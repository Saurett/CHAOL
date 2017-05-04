package com.indev.chaol.models;

import java.io.Serializable;

/**
 * Created by texiumuser on 28/02/2017.
 */

public class Remolques implements Serializable {

    private String numeroEconomico;
    private String marca;
    private String modelo;
    private String numeroDeSerie;
    private String placa;
    private String IdGPS;
    private String tipoRemolque;

    private String firebaseId;
    private String estatus;
    private Long fechaDeCreacion;
    private Long fechaDeEdicion;

    public Remolques() {

    }

    public Remolques(String numeroEconomico, String marca, String modelo, String numeroDeSerie, String placa, String idGPS, String tipoRemolque, String firebaseId, String estatus, Long fechaDeCreacion, Long fechaDeEdicion) {
        this.numeroEconomico = numeroEconomico;
        this.marca = marca;
        this.modelo = modelo;
        this.numeroDeSerie = numeroDeSerie;
        this.placa = placa;
        IdGPS = idGPS;
        this.tipoRemolque = tipoRemolque;
        this.firebaseId = firebaseId;
        this.estatus = estatus;
        this.fechaDeCreacion = fechaDeCreacion;
        this.fechaDeEdicion = fechaDeEdicion;
    }

    public String getNumeroEconomico() {
        return numeroEconomico;
    }

    public void setNumeroEconomico(String numeroEconomico) {
        this.numeroEconomico = numeroEconomico;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNumeroDeSerie() {
        return numeroDeSerie;
    }

    public void setNumeroDeSerie(String numeroDeSerie) {
        this.numeroDeSerie = numeroDeSerie;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getIdGPS() {
        return IdGPS;
    }

    public void setIdGPS(String idGPS) {
        IdGPS = idGPS;
    }

    public String getTipoRemolque() {
        return tipoRemolque;
    }

    public void setTipoRemolque(String tipoRemolque) {
        this.tipoRemolque = tipoRemolque;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public Long getFechaDeCreacion() {
        return fechaDeCreacion;
    }

    public void setFechaDeCreacion(Long fechaDeCreacion) {
        this.fechaDeCreacion = fechaDeCreacion;
    }

    public Long getFechaDeEdicion() {
        return fechaDeEdicion;
    }

    public void setFechaDeEdicion(Long fechaDeEdicion) {
        this.fechaDeEdicion = fechaDeEdicion;
    }
}
