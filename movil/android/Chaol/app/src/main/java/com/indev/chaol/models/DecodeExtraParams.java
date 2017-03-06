package com.indev.chaol.models;


import java.io.Serializable;

/**
 * Created by texiumuser on 06/03/2017.
 */

public class DecodeExtraParams implements Serializable {

    private String tituloActividad;
    private String tituloFormulario;
    private int accionFragmento;
    private String fragmentTag;

    public DecodeExtraParams() {

    }

    public DecodeExtraParams(String _tituloActividad, String _tituloFormulario, int _accionFragmento, String _fragmentTag) {
        this.tituloActividad = _tituloActividad;
        this.tituloFormulario = _tituloFormulario;
        this.accionFragmento = _accionFragmento;
        this.fragmentTag = _fragmentTag;
    }

    public String getTituloActividad() {
        return tituloActividad;
    }


    public void setTituloActividad(String tituloActividad) {
        this.tituloActividad = tituloActividad;
    }

    public String getTituloFormulario() {
        return tituloFormulario;
    }

    public void setTituloFormulario(String tituloFormulario) {
        this.tituloFormulario = tituloFormulario;
    }

    public int getAccionFragmento() {
        return accionFragmento;
    }

    public void setAccionFragmento(int accionFragmento) {
        this.accionFragmento = accionFragmento;
    }

    public String getFragmentTag() {
        return fragmentTag;
    }

    public void setFragmentTag(String fragmentTag) {
        this.fragmentTag = fragmentTag;
    }
}
