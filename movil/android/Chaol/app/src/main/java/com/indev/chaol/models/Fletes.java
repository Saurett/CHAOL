package com.indev.chaol.models;

import android.content.Intent;

import java.io.Serializable;

/**
 * Created by texiumuser on 28/02/2017.
 */

public class Fletes implements Serializable {

    private String bodegaDeCarga;
    private String bodegaDeDescarga;
    private String carga;
    private String cliente;
    private String destinatario;
    private Long fechaDeAsignacionDeTransportista;
    private Long fechaDeSalida;
    private String firebaseID;
    private Integer horaDeSalida;
    private String idFlete;
    private String numeroDeEmbarque;
    private String precio;
    private String tipoDeRemolque;

    private String estatus;
    private Long fechaDeCreacion;
    private Long fechaDeEdicion;

    public Fletes() {
    }

    public Fletes(String bodegaDeCarga, String bodegaDeDescarga, String carga, String cliente, String destinatario, Long fechaDeAsignacionDeTransportista, Long fechaDeSalida, String firebaseID, Integer horaDeSalida, String idFlete, String numeroDeEmbarque, String precio, String tipoDeRemolque, String estatus, Long fechaDeCreacion, Long fechaDeEdicion) {
        this.bodegaDeCarga = bodegaDeCarga;
        this.bodegaDeDescarga = bodegaDeDescarga;
        this.carga = carga;
        this.cliente = cliente;
        this.destinatario = destinatario;
        this.fechaDeAsignacionDeTransportista = fechaDeAsignacionDeTransportista;
        this.fechaDeSalida = fechaDeSalida;
        this.firebaseID = firebaseID;
        this.horaDeSalida = horaDeSalida;
        this.idFlete = idFlete;
        this.numeroDeEmbarque = numeroDeEmbarque;
        this.precio = precio;
        this.tipoDeRemolque = tipoDeRemolque;
        this.estatus = estatus;
        this.fechaDeCreacion = fechaDeCreacion;
        this.fechaDeEdicion = fechaDeEdicion;
    }

    public String getBodegaDeCarga() {
        return bodegaDeCarga;
    }

    public void setBodegaDeCarga(String bodegaDeCarga) {
        this.bodegaDeCarga = bodegaDeCarga;
    }

    public String getBodegaDeDescarga() {
        return bodegaDeDescarga;
    }

    public void setBodegaDeDescarga(String bodegaDeDescarga) {
        this.bodegaDeDescarga = bodegaDeDescarga;
    }

    public String getCarga() {
        return carga;
    }

    public void setCarga(String carga) {
        this.carga = carga;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public Long getFechaDeAsignacionDeTransportista() {
        return fechaDeAsignacionDeTransportista;
    }

    public void setFechaDeAsignacionDeTransportista(Long fechaDeAsignacionDeTransportista) {
        this.fechaDeAsignacionDeTransportista = fechaDeAsignacionDeTransportista;
    }

    public Long getFechaDeSalida() {
        return fechaDeSalida;
    }

    public void setFechaDeSalida(Long fechaDeSalida) {
        this.fechaDeSalida = fechaDeSalida;
    }

    public String getFirebaseID() {
        return firebaseID;
    }

    public void setFirebaseID(String firebaseID) {
        this.firebaseID = firebaseID;
    }

    public Integer getHoraDeSalida() {
        return horaDeSalida;
    }

    public void setHoraDeSalida(Integer horaDeSalida) {
        this.horaDeSalida = horaDeSalida;
    }

    public String getIdFlete() {
        return idFlete;
    }

    public void setIdFlete(String idFlete) {
        this.idFlete = idFlete;
    }

    public String getNumeroDeEmbarque() {
        return numeroDeEmbarque;
    }

    public void setNumeroDeEmbarque(String numeroDeEmbarque) {
        this.numeroDeEmbarque = numeroDeEmbarque;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getTipoDeRemolque() {
        return tipoDeRemolque;
    }

    public void setTipoDeRemolque(String tipoDeRemolque) {
        this.tipoDeRemolque = tipoDeRemolque;
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
