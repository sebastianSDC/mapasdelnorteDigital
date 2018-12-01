package com.example.pilasnotebook.mapasdelnortedigital.model.POJO;


import java.io.Serializable;
import java.util.List;
import com.google.android.gms.maps.model.LatLng;

public class Cliente implements Serializable {

    private String id;
    private String categoria;
    private String nombreComercio;
    private String foto;
    private Zona zona;
    private String telefono;
    private LatLng latlang;
    private String mail;
    private String horarioDeAtencion, paginaWeb;
    private List<String> redes;

    private String direccion;


    public Cliente() {

    }

    public Cliente(String nombre, String categoria, String direccion) {
        this.nombreComercio = nombre;
        this.categoria = categoria;
        this.direccion = direccion;


    }

    public String getNombreComercio() {
        return nombreComercio;
    }

    public void setNombreComercio(String nombre) {
        this.nombreComercio = nombre;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getHorarioDeAtencion() {
        return horarioDeAtencion;
    }

    public void setHorarioDeAtencion(String horarioDeAtencion) {
        this.horarioDeAtencion = horarioDeAtencion;
    }

    public String getPaginaWeb() {
        return paginaWeb;
    }

    public void setPaginaWeb(String paginaWeb) {
        this.paginaWeb = paginaWeb;
    }

    public List<String> getRedes() {
        return redes;
    }

    public void setRedes(List<String> redes) {
        this.redes = redes;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}