package com.indev.chaol.models;

import java.io.Serializable;

/**
 * Created by texiumuser on 28/02/2017.
 */

public class Tractores implements Serializable {

    private int cve;
    private int id;
    private String numEconomico;

    public Tractores() {

    }

    public  Tractores(String _numEconomico) {
        this.numEconomico = _numEconomico;
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

    public String getNumEconomico() {
        return numEconomico;
    }

    public void setNumEconomico(String numEconomico) {
        this.numEconomico = numEconomico;
    }
}
