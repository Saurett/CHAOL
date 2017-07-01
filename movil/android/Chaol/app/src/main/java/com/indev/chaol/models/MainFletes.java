package com.indev.chaol.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jvier on 22/06/2017.
 */

public class MainFletes {

    private Fletes flete;
    private Bodegas bodegaDeCarga;
    private Bodegas bodedaDeDescarga;
    private Choferes choferSeleccionado;
    private Remolques remolqueSeleccionado;
    private Tractores tractorSeleccionado;
    private Transportistas transportistaSeleccionado;
    private List<Transportistas> transportistasInteresados;

    public MainFletes() {
        transportistasInteresados = new ArrayList<>();
    }

    public MainFletes(Fletes flete, Bodegas bodegaDeCarga, Bodegas bodedaDeDescarga, Choferes choferSeleccionado, Remolques remolqueSeleccionado, Tractores tractorSeleccionado, Transportistas transportistaSeleccionado, List<Transportistas> transportistasInteresados) {
        this.flete = flete;
        this.bodegaDeCarga = bodegaDeCarga;
        this.bodedaDeDescarga = bodedaDeDescarga;
        this.choferSeleccionado = choferSeleccionado;
        this.remolqueSeleccionado = remolqueSeleccionado;
        this.tractorSeleccionado = tractorSeleccionado;
        this.transportistaSeleccionado = transportistaSeleccionado;
        this.transportistasInteresados = transportistasInteresados;
    }

    public Fletes getFlete() {
        return flete;
    }

    public void setFlete(Fletes flete) {
        this.flete = flete;
    }

    public Bodegas getBodegaDeCarga() {
        return bodegaDeCarga;
    }

    public void setBodegaDeCarga(Bodegas bodegaDeCarga) {
        this.bodegaDeCarga = bodegaDeCarga;
    }

    public Bodegas getBodedaDeDescarga() {
        return bodedaDeDescarga;
    }

    public void setBodedaDeDescarga(Bodegas bodedaDeDescarga) {
        this.bodedaDeDescarga = bodedaDeDescarga;
    }

    public Choferes getChoferSeleccionado() {
        return choferSeleccionado;
    }

    public void setChoferSeleccionado(Choferes choferSeleccionado) {
        this.choferSeleccionado = choferSeleccionado;
    }

    public Remolques getRemolqueSeleccionado() {
        return remolqueSeleccionado;
    }

    public void setRemolqueSeleccionado(Remolques remolqueSeleccionado) {
        this.remolqueSeleccionado = remolqueSeleccionado;
    }

    public Tractores getTractorSeleccionado() {
        return tractorSeleccionado;
    }

    public void setTractorSeleccionado(Tractores tractorSeleccionado) {
        this.tractorSeleccionado = tractorSeleccionado;
    }

    public Transportistas getTransportistaSeleccionado() {
        return transportistaSeleccionado;
    }

    public void setTransportistaSeleccionado(Transportistas transportistaSeleccionado) {
        this.transportistaSeleccionado = transportistaSeleccionado;
    }

    public List<Transportistas> getTransportistasInteresados() {
        return transportistasInteresados;
    }

    public void setTransportistasInteresados(List<Transportistas> transportistasInteresados) {
        this.transportistasInteresados = transportistasInteresados;
    }
}
