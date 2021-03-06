package com.indev.chaol.models;

import java.io.Serializable;

/**
 * Created by texiumuser on 28/02/2017.
 */

public class Bodegas implements Serializable {

    private String nombreDeLaBodega;
    private String estado;
    private String ciudad;
    private String colonia;
    private String codigoPostal;
    private String calle;
    private String numeroInterior;
    private String numeroExterior;

    private String nombreDelCliente;

    private String firebaseIdBodega;
    private String firebaseIdDelCliente;
    private String estatus;
    private Long fechaDeCreacion;
    private Long fechaDeEdicion;


    public Bodegas() {

    }

    public Bodegas(String nombreDeLaBodega, String estado, String ciudad, String colonia, String codigoPostal, String calle, String numeroInterior, String numeroExterior, String nombreDelCliente, String firebaseIdBodega, String firebaseIdDelCliente, String estatus, Long fechaDeCreacion, Long fechaDeEdicion) {
        this.nombreDeLaBodega = nombreDeLaBodega;
        this.estado = estado;
        this.ciudad = ciudad;
        this.colonia = colonia;
        this.codigoPostal = codigoPostal;
        this.calle = calle;
        this.numeroInterior = numeroInterior;
        this.numeroExterior = numeroExterior;
        this.nombreDelCliente = nombreDelCliente;
        this.firebaseIdBodega = firebaseIdBodega;
        this.firebaseIdDelCliente = firebaseIdDelCliente;
        this.estatus = estatus;
        this.fechaDeCreacion = fechaDeCreacion;
        this.fechaDeEdicion = fechaDeEdicion;
    }

    public String getNombreDeLaBodega() {
        return nombreDeLaBodega;
    }

    public void setNombreDeLaBodega(String nombreDeLaBodega) {
        this.nombreDeLaBodega = nombreDeLaBodega;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumeroInterior() {
        return numeroInterior;
    }

    public void setNumeroInterior(String numeroInterior) {
        this.numeroInterior = numeroInterior;
    }

    public String getNumeroExterior() {
        return numeroExterior;
    }

    public void setNumeroExterior(String numeroExterior) {
        this.numeroExterior = numeroExterior;
    }

    public String getFirebaseIdBodega() {
        return firebaseIdBodega;
    }

    public void setFirebaseIdBodega(String firebaseIdBodega) {
        this.firebaseIdBodega = firebaseIdBodega;
    }

    public String getFirebaseIdDelCliente() {
        return firebaseIdDelCliente;
    }

    public void setFirebaseIdDelCliente(String firebaseIdDelCliente) {
        this.firebaseIdDelCliente = firebaseIdDelCliente;
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

    public String getNombreDelCliente() {
        return nombreDelCliente;
    }

    public void setNombreDelCliente(String nombreDelCliente) {
        this.nombreDelCliente = nombreDelCliente;
    }
}
