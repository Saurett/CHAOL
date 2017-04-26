package com.indev.chaol.models;

import java.io.Serializable;

/**
 * Created by texiumuser on 26/04/2017.
 */

public class Usuarios implements Serializable {

    private String tipoUsuario;
    private String firebaseID;

    public Usuarios() {
    }

    public Usuarios(String tipoUsuario, String firebaseID) {
        this.tipoUsuario = tipoUsuario;
        this.firebaseID = firebaseID;
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
}
