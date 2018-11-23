package com.example.pilasnotebook.mapasdelnortedigital.utils;

import android.app.AlertDialog;

import com.example.pilasnotebook.mapasdelnortedigital.view.fragment.DatosClienteFragment;

import java.util.Locale;


public final class Constantes {

    // constante universal
    public static final String TAG = "tag";

    public static final int COD_CAMARA =20;  //para abrir camara y tomar la foto
    public static final int COD_GALERIA =10; // para abrir galeria y seleccionar imagen.

    // constantes para ubicacion en mapa
    public static final String COORDENADAS = "latlang";
public static final Locale LOCALE_ARGENTINA = new Locale("es","ARG");

    //constantes admin cliente: datos
    public static final String NOMBRE = "nombre";
    public static final String CATEGORIA = "categoria";
    public static final String FOTO = "foto";
    public static final String DIRECCION = "direccion";
    public static final String LOCALIDAD = "localidad";
    public static final String CODIGO_POSTAL = "codigo postal";
    public static final String PROVINCIA = "provincia";
    public static final String PAIS = "pais";
    public static final String TELEFONO = "telefono";
    public static final String MAIL = "mail";


    //constantes admin cliente: cuponera
    public static final String TIPO = "tipo";
    public static final String FECHA = "fecha";
    public static final String TITULO = "titulo";
    public static final String DESCRIPCION = "descripcion";// comun a todas los fragment de admin





}
