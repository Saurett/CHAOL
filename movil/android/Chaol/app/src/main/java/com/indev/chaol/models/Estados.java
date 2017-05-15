package com.indev.chaol.models;

/**
 * Created by jvier on 13/03/2017.
 */

public class Estados {

    private Integer id;
    private String estado;

    public Estados() {

    }

    public Estados(Integer id, String estado) {
        this.id = id;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
