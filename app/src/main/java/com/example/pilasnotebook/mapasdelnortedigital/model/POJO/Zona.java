package com.example.pilasnotebook.mapasdelnortedigital.model.POJO;

import com.google.android.gms.maps.model.LatLng;

public class Zona {

    private String direccion;
    private String localidad;
    private String provincia;
    private String pais;
    private Integer codigoPostal;
    private LatLng latlang;




    public Zona() {

    }
    public Zona(String direccion) {
        this.direccion = direccion;


    }

    public Zona(String direccion, String localidad, String provincia, String pais, Integer codigoPostal) {
        this.direccion = direccion;
        this.localidad = localidad;
        this.provincia = provincia;
        this.pais = pais;
        this.codigoPostal = codigoPostal;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getProvincia() {
        return provincia;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public Integer getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(Integer codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public LatLng getLatlang() {
        return latlang;
    }

    public void setLatlang(LatLng latlang) {
        this.latlang = latlang;
    }
}
