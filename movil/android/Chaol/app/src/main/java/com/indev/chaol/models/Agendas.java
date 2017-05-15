package com.indev.chaol.models;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by texiumuser on 28/02/2017.
 */

public class Agendas implements Serializable {

    private int cve;
    private int id;
    private String estado;
    private String nombreCliente;
    private String nombreTransportista;
    private int status;

    public Agendas() {

    }

    public Agendas(int cve, int id, String estado, String nombreCliente, String nombreTransportista, int status) {
        this.cve = cve;
        this.id = id;
        this.estado = estado;
        this.nombreCliente = nombreCliente;
        this.nombreTransportista = nombreTransportista;
        this.status = status;
    }

    public int getCve() {
        return cve;
    }

    public void setCve(int cve) {
        this.cve = cve;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getNombreTransportista() {
        return nombreTransportista;
    }

    public void setNombreTransportista(String nombreTransportista) {
        this.nombreTransportista = nombreTransportista;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
