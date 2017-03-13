package com.indev.chaol.models;

/**
 * Created by jvier on 13/03/2017.
 */

public class MetodosPagos {

    private Integer id;
    private String metodoPago;

    public MetodosPagos() {

    }

    public MetodosPagos(Integer _id, String _metodoPago) {
        this.id = _id;
        this.metodoPago = _metodoPago;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
}
