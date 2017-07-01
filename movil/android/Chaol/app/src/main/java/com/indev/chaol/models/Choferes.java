package com.indev.chaol.models;

import java.io.Serializable;

/**
 * Created by texiumuser on 28/02/2017.
 */

public class Choferes implements Serializable {

    private String nombre;
    private String numeroDeLicencia;
    private String numeroDeSeguroSocial;
    private String CURP;
    private String estado;
    private String ciudad;
    private String colonia;
    private String codigoPostal;
    private String calle;
    private String numeroInterior;
    private String numeroExterior;
    private String metodoPago;
    private String telefono;
    private String celular1;
    private String celular2;
    private String correoElectronico;
    private String contraseña;

    private String imagenURL;
    private String tipoDeUsuario;

    private String firebaseId;
    private String firebaseIdDelTransportista;

    private String estatus;
    private Long fechaDeCreacion;
    private Long fechaDeEdicion;

    public Choferes() {

    }

    public Choferes(String _nombre) {
        this.nombre = _nombre;
    }

    public Choferes(String nombre, String numeroDeLicencia, String numeroDeSeguroSocial, String CURP, String estado, String ciudad, String colonia, String codigoPostal, String calle, String numeroInterior, String numeroExterior, String metodoPago, String telefono, String celular1, String celular2, String correoElectronico, String contraseña, String imagenURL, String tipoUsuario, String firebaseId, String firebaseIdDelTransportista, String estatus, Long fechaDeCreacion, Long fechaDeEdicion) {
        this.nombre = nombre;
        this.numeroDeLicencia = numeroDeLicencia;
        this.numeroDeSeguroSocial = numeroDeSeguroSocial;
        this.CURP = CURP;
        this.estado = estado;
        this.ciudad = ciudad;
        this.colonia = colonia;
        this.codigoPostal = codigoPostal;
        this.calle = calle;
        this.numeroInterior = numeroInterior;
        this.numeroExterior = numeroExterior;
        this.metodoPago = metodoPago;
        this.telefono = telefono;
        this.celular1 = celular1;
        this.celular2 = celular2;
        this.correoElectronico = correoElectronico;
        this.contraseña = contraseña;
        this.imagenURL = imagenURL;
        this.tipoDeUsuario = tipoUsuario;
        this.firebaseId = firebaseId;
        this.firebaseIdDelTransportista = firebaseIdDelTransportista;
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

    public String getNumeroDeLicencia() {
        return numeroDeLicencia;
    }

    public void setNumeroDeLicencia(String numeroDeLicencia) {
        this.numeroDeLicencia = numeroDeLicencia;
    }

    public String getNumeroDeSeguroSocial() {
        return numeroDeSeguroSocial;
    }

    public void setNumeroDeSeguroSocial(String numeroDeSeguroSocial) {
        this.numeroDeSeguroSocial = numeroDeSeguroSocial;
    }

    public String getCURP() {
        return CURP;
    }

    public void setCURP(String CURP) {
        this.CURP = CURP;
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

    public String getCelular1() {
        return celular1;
    }

    public void setCelular1(String celular1) {
        this.celular1 = celular1;
    }

    public String getCelular2() {
        return celular2;
    }

    public void setCelular2(String celular2) {
        this.celular2 = celular2;
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

    public String getFirebaseIdDelTransportista() {        return firebaseIdDelTransportista;
    }

    public void setFirebaseIdDelTransportista(String firebaseIdDelTransportista) {
        this.firebaseIdDelTransportista = firebaseIdDelTransportista;
    }
}
