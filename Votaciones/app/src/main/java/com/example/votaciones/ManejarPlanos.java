package com.example.votaciones;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class ManejarPlanos {

    Context ctx;
    FileOutputStream fos;
    FileInputStream fis;

    public ManejarPlanos(Context ctx) {
        this.ctx = ctx;
    }

    public void addCandidato(String archivo, String path, String dni, String nombre, String apellidos, String partido) {
        try {
            fos = ctx.openFileOutput(archivo, Context.MODE_APPEND);
            String newTextLine = path + '\n' + dni + '\n' + nombre + '\n' + apellidos + '\n' + partido ;
            fos.write(newTextLine.getBytes());
            fos.write('\n');
            fos.close();
            Toast.makeText(ctx,"Se ha añadido correctamente el candidato ",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("Error-> ", e.getMessage());
        }
    }

    public void addVotante(String archivo, String dni, String nombre, String apellidos, String telefono) {
        try {
            fos = ctx.openFileOutput(archivo, Context.MODE_APPEND);
            String newTextLine = dni + '\n' + nombre + '\n' + apellidos + '\n' + telefono;
            fos.write(newTextLine.getBytes());
            fos.write('\n');
            fos.close();
            Toast.makeText(ctx,"Se ha añadido correctamente el votante ",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("Error-> ", e.getMessage());
        }
    }

    public void newVoto(String archivo, String dniVotante, int voto) {
        try {
            fos = ctx.openFileOutput(archivo, Context.MODE_APPEND);
            String newTextLine = dniVotante + '\n' + voto;
            fos.write(newTextLine.getBytes());
            fos.write('\n');
            fos.close();
            Toast.makeText(ctx,"Su voto ha sido exitoso", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e("Error-> ", e.getMessage());
        }
    }

    public ArrayList<Candidato> leerCandidatos(String archivo){
        ArrayList<Candidato> lstCandidatos = new ArrayList<>();
        int i = 1;
        int cont = 0;
        Bitmap bmp = null;
        String  path = "", dni="", nombres = "", apellidos = "", partido = "", lectura = "";
        char caracter;
        try {
            fis = ctx.openFileInput(archivo);
            while (i > 0) {
                i = fis.read();
                caracter = (char) i;
                lectura += caracter;
                if (i == '\n') {
                    switch (cont) {
                        case 0:
                            path = lectura.trim();
                            //bmp = StringToBitMap(lectura.trim());
                            break;
                        case 1:
                            dni = lectura.trim();
                            break;
                        case 2:
                            nombres = lectura.trim();
                            break;
                        case 3:
                            apellidos = lectura.trim();
                            break;
                        case 4:
                            partido = lectura.trim();
                            cont = -1;
                            lstCandidatos.add(new Candidato(path, dni, nombres, apellidos, partido));
                            break;
                    }
                    lectura = "";
                    cont ++;
                }
            }
        } catch (Exception e) {
            Log.e("", e.getMessage());
        }
        return lstCandidatos;
    }

    public ArrayList<Votante> leerVotantes(String archivo){
        ArrayList<Votante> lstVotantes = new ArrayList<>();
        int i = 1;
        int cont = 0;
        String lectura= "", dni = "", nombres = "", apellidos = "", telefono = "";
        char caracter;
        try {
            fis = ctx.openFileInput(archivo);
            while (i > 0) {
                i = fis.read();
                caracter = (char) i;
                lectura += caracter;
                if (i == '\n') {
                    switch (cont) {
                        case 0:
                            dni = lectura.trim();
                            break;
                        case 1:
                            nombres = lectura.trim();
                            break;
                        case 2:
                            apellidos = lectura.trim();
                            break;
                        case 3:
                            telefono = lectura.trim();
                            cont = -1;
                            lstVotantes.add(new Votante(dni, nombres, apellidos, telefono));
                            break;
                    }
                    lectura = "";
                    cont ++;
                }
            }
        } catch (Exception e) {
            Log.e("", e.getMessage());
        }
        return lstVotantes;
    }

    public ArrayList<Voto> leerVotos(String archivo){
        ArrayList<Voto> lstVotos = new ArrayList<>();
        int i = 1;
        int cont = 0;
        String lectura= "", dniVotante = "";
        int voto;
        char caracter;
        try {
            fis = ctx.openFileInput(archivo);
            while (i > 0) {
                i = fis.read();
                caracter = (char) i;
                lectura += caracter;
                if (i == '\n') {
                    switch (cont) {
                        case 0:
                            dniVotante = lectura.trim();
                            break;
                        case 1:
                            voto = Integer.parseInt(lectura.trim());
                            cont = -1;
                            lstVotos.add(new Voto(dniVotante,voto));
                            break;
                    }
                    lectura = "";
                    cont ++;
                }
            }
        } catch (Exception e) {
            Log.e("", e.getMessage());
        }
        return lstVotos;
    }

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }
}
