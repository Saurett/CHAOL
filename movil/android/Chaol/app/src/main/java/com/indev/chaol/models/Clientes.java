package com.indev.chaol.models;

import java.io.Serializable;

/**
 * Created by jvier on 10/05/2017.
 */

public class Clientes implements Serializable {

    private String nombre;
    private String RFC;
    private String estado;
    private String ciudad;
    private String colonia;
    private String codigoPostal;
    private String calle;
    private String numeroInterior;
    private String numeroExterior;
    private String metodoDePago;
    private String telefono;
    private String celular;
    private String correoElectronico;
    private String password;
    private String imagenURL;
    private String tipoDeUsuario;

    private String firebaseId;
    private String estatus;
    private Long fechaDeCreacion;
    private Long fechaDeEdicion;

    public Clientes() {
    }

    public Clientes(String nombre, String RFC, String estado, String ciudad, String colonia, String codigoPostal, String calle, String numeroInterior, String numeroExterior, String metodoDePago, String telefono, String celular, String correoElectronico, String password, String imagenURL, String tipoDeUsuario, String firebaseId, String estatus, Long fechaDeCreacion, Long fechaDeEdicion) {
        this.nombre = nombre;
        this.RFC = RFC;
        this.estado = estado;
        this.ciudad = ciudad;
        this.colonia = colonia;
        this.codigoPostal = codigoPostal;
        this.calle = calle;
        this.numeroInterior = numeroInterior;
        this.numeroExterior = numeroExterior;
        this.metodoDePago = metodoDePago;
        this.telefono = telefono;
        this.celular = celular;
        this.correoElectronico = correoElectronico;
        this.password = password;
        this.imagenURL = imagenURL;
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

    public String getRFC() {
        return RFC;
    }

    public void setRFC(String RFC) {
        this.RFC = RFC;
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

    public String getMetodoDePago() {
        return metodoDePago;
    }

    public void setMetodoDePago(String metodoDePago) {
        this.metodoDePago = metodoDePago;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImagenURL() {
        return imagenURL;
    }

    public void setImagenURL(String imagenURL) {
        this.imagenURL = imagenURL;
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
