package com.indev.chaol.models;

/**
 * Created by jvier on 13/03/2017.
 */

public class TiposRemolques {

    private Integer id;
    private String tipoRemolque;

    public TiposRemolques() {

    }

    public TiposRemolques(Integer _id, String _tipoRemolque) {
        this.id = _id;
        this.tipoRemolque = _tipoRemolque;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipoRemolque() {
        return tipoRemolque;
    }

    public void setTipoRemolque(String tipoRemolque) {
        this.tipoRemolque = tipoRemolque;
    }
}
