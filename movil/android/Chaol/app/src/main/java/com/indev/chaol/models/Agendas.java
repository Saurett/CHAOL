package com.indev.chaol.models;

import java.io.Serializable;

/**
 * Created by texiumuser on 28/02/2017.
 */

public class Agendas implements Serializable {

    private String estatus;
    private String nombre;
    private String nombreDelTransportista;
    private String firebaseID;

    public Agendas() {
    }

    public Agendas(String estatus, String nombre, String nombreDelTransportista, String firebaseID) {
        this.estatus = estatus;
        this.nombre = nombre;
        this.nombreDelTransportista = nombreDelTransportista;
        this.firebaseID = firebaseID;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreDelTransportista() {
        return nombreDelTransportista;
    }

    public void setNombreDelTransportista(String nombreDelTransportista) {
        this.nombreDelTransportista = nombreDelTransportista;
    }

    public String getFirebaseID() {
        return firebaseID;
    }

    public void setFirebaseID(String firebaseID) {
        this.firebaseID = firebaseID;
    }
}
