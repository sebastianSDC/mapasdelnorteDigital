package com.example.pilasnotebook.mapasdelnortedigital.Model.POJO;



public class Cliente {



    private String nombre, direccion, telefono, mail;

    public Cliente() {
    }

    public Cliente(String nombre, String direccion, String telefono, String mail) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.mail = mail;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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


    /*@Override
    public String toString() {
        return ("nombre:" + getNombre() + "direccion:" + getDireccion() + "telefono:" + getTelefono() + "mail:" + getMail());
    }*/
}
