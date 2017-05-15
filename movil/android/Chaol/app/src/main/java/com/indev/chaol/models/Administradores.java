package com.indev.chaol.models;

import java.io.Serializable;

/**
 * Created by texiumuser on 28/02/2017.
 */

public class Administradores implements Serializable {

    private String nombre;
    private String correoElectronico;
    private String contraseña;

    private String tipoDeUsuario;
    private String firebaseId;
    private String estatus;
    private Long fechaDeCreacion;
    private Long fechaDeEdicion;

    public Administradores() {
    }

    public Administradores(String firebaseId, String nombre) {
        this.firebaseId = firebaseId;
        this.nombre = nombre;
    }

    public Administradores(String nombre) {
        this.nombre = nombre;
    }

    public Administradores(String nombre, String correoElectronico, String contraseña, String tipoDeUsuario, String firebaseId, String estatus, Long fechaDeCreacion, Long fechaDeEdicion) {
        this.nombre = nombre;
        this.correoElectronico = correoElectronico;
        this.contraseña = contraseña;
        this.tipoDeUsuario = tipoDeUsuario;
        this.firebaseId = firebaseId;
        this.estatus = estatus;
        this.fechaDeCreacion = fechaDeCreacion;
        this.fechaDeEdicion = fechaDeEdicion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getTipoDeUsuario() {
        return tipoDeUsuario;
    }

    public void setTipoDeUsuario(String tipoDeUsuario) {
        this.tipoDeUsuario = tipoDeUsuario;
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
