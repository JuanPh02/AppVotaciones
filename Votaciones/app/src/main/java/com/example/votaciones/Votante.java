package com.example.votaciones;

public class Votante {

    private String dni;
    private String nombres;
    private String apellidos;
    String telefono;

    public Votante( String dni, String nombres, String apellidos, String telefono){
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getDni() {
        return dni;
    }

}
