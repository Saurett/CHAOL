package com.indev.chaol.models;

import java.io.Serializable;

/**
 * Created by texiumuser on 28/02/2017.
 */

public class Fletes implements Serializable {

    private int cve;
    private int id;
    private String nombre;
    private String firebaseId;

    public Fletes() {

    }

    public Fletes(int cve, int id, String nombre, String firebaseId) {
        this.cve = cve;
        this.id = id;
        this.nombre = nombre;
        this.firebaseId = firebaseId;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }
}
