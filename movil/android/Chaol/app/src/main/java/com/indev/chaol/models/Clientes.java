package com.indev.chaol.models;

import java.io.Serializable;

/**
 * Created by texiumuser on 28/02/2017.
 */

public class Clientes implements Serializable {

    private int cve;
    private int id;

    private String nombre;
    private String rfc;
    private String estado;
    private String ciudad;
    private String colonia;
    private String codigoPostal;
    private String calle;
    private String numInterior;
    private String numExterior;
    private String metodoPago;
    private String telefono;
    private String celular;
    private String correoElectronico;
    private String contraseña;

    private String imagenUrl;
    private String tipoUsuario;

    private String firebaseID;
    private String estatus;
    private Double fechaDeCreacion;
    private Double fechaDeEdicion;


    public Clientes() {

    }

    public Clientes(String _nombre) {
        this.nombre = _nombre;
    }

    public Clientes(String nombre, String rfc, String estado, String ciudad, String colonia,
                    String codigoPostal, String calle, String numInterior, String numExterior,
                    String metodoPago, String telefono, String celular, String correoElectronico,
                    String contraseña, String imagenUrl, String tipoUsuario, String firebaseID,
                    String estatus, Double fechaDeCreacion, Double fechaDeEdicion) {
        this.nombre = nombre;
        this.rfc = rfc;
        this.estado = estado;
        this.ciudad = ciudad;
        this.colonia = colonia;
        this.codigoPostal = codigoPostal;
        this.calle = calle;
        this.numInterior = numInterior;
        this.numExterior = numExterior;
        this.metodoPago = metodoPago;
        this.telefono = telefono;
        this.celular = celular;
        this.correoElectronico = correoElectronico;
        this.contraseña = contraseña;
        this.imagenUrl = imagenUrl;
        this.tipoUsuario = tipoUsuario;
        this.firebaseID = firebaseID;
        this.estatus = estatus;
        this.fechaDeCreacion = fechaDeCreacion;
        this.fechaDeEdicion = fechaDeEdicion;
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

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
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

    public String getNumInterior() {
        return numInterior;
    }

    public void setNumInterior(String numInterior) {
        this.numInterior = numInterior;
    }

    public String getNumExterior() {
        return numExterior;
    }

    public void setNumExterior(String numExterior) {
        this.numExterior = numExterior;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
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

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getFirebaseID() {
        return firebaseID;
    }

    public void setFirebaseID(String firebaseID) {
        this.firebaseID = firebaseID;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public Double getFechaDeCreacion() {
        return fechaDeCreacion;
    }

    public void setFechaDeCreacion(Double fechaDeCreacion) {
        this.fechaDeCreacion = fechaDeCreacion;
    }

    public Double getFechaDeEdicion() {
        return fechaDeEdicion;
    }

    public void setFechaDeEdicion(Double fechaDeEdicion) {
        this.fechaDeEdicion = fechaDeEdicion;
    }
}
