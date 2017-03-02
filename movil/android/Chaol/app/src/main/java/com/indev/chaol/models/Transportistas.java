package com.indev.chaol.models;

import java.io.Serializable;

/**
 * Created by texiumuser on 28/02/2017.
 */

public class Transportistas implements Serializable {

    private int cve;
    private int id;
    private String nombre;

    public Transportistas() {

    }

    public Transportistas(String _nombre) {
        this.nombre = _nombre;
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
}
