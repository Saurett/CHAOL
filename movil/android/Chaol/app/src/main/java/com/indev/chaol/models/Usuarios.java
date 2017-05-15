package com.indev.chaol.models;

import java.io.Serializable;

/**
 * Created by texiumuser on 26/04/2017.
 */

public class Usuarios implements Serializable {

    private String tipoDeUsuario;
    private String firebaseId;
    private String token;

    public Usuarios(String tipoDeUsuario, String firebaseId, String token) {
        this.tipoDeUsuario = tipoDeUsuario;
        this.firebaseId = firebaseId;
        this.token = token;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
