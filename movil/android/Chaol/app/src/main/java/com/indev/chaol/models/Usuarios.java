package com.indev.chaol.models;

import java.io.Serializable;

/**
 * Created by texiumuser on 26/04/2017.
 */

public class Usuarios implements Serializable {

    private String tipoUsuario;
    private String firebaseId;
    private String token;

    public Usuarios(String tipoUsuario, String firebaseId, String token) {
        this.tipoUsuario = tipoUsuario;
        this.firebaseId = firebaseId;
        this.token = token;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
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
