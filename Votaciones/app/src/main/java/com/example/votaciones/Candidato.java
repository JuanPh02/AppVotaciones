package com.example.votaciones;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

public class Candidato {

    private String path;
    private String dni;
    private String nombres;
    private String apellidos;
    private String partido;

    public Candidato(String path, String dni, String nombres, String apellidos, String partido){
        this.path = path;
        //this.bmp = bmp;
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.partido = partido;
    }


    public String getDni() {
        return dni;
    }

    public String getPath() {
        return path;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getPartido() {
        return partido;
    }
}
