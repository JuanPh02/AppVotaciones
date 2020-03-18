package com.example.votaciones;


import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.example.votaciones.R.drawable.img_base;


/**
 * A simple {@link Fragment} subclass.
 */
public class Candidatos extends Fragment {


    public Candidatos() {
        // Required empty public constructor
    }

    ImageView imgPerfil;
    EditText etDni, etNombres, etApellidos, etPartido;
    Button btnImage, btnAddCandidato;
    View view;
    File imgFile;
    String pathImage;
    ArrayList<Candidato> lstCandidatos;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_candidatos, container, false);

        imgPerfil = view.findViewById(R.id.imgPerfil);
        btnImage = view.findViewById(R.id.btnImage);
        etDni = view.findViewById(R.id.etDni);
        etNombres = view.findViewById(R.id.etNombres);
        etApellidos = view.findViewById(R.id.etApellidos);
        etPartido = view.findViewById(R.id.etPartido);
        btnAddCandidato = view.findViewById(R.id.btnAddCandidato);
        final ManejarPlanos manejarPlanos = new ManejarPlanos(getContext());
        final String archCandidatos = "Candidatos.txt";


        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
               intent.setType("image/");
               startActivityForResult(intent.createChooser(intent, "Seleccione la aplicación"),10);
            }
        });

        btnAddCandidato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dni, nombres, apellidos, partido;
                dni = etDni.getText().toString();
                nombres = etNombres.getText().toString();
                apellidos = etApellidos.getText().toString();
                partido = etPartido.getText().toString();
                lstCandidatos = manejarPlanos.leerCandidatos(archCandidatos);
                if(dni.isEmpty() || nombres.isEmpty() || apellidos.isEmpty() ||
                        partido.isEmpty() || btnImage.getText().equals("seleccionar imagen del candidato")) {
                    Toast.makeText(getContext(),"Ingrese todos los datos requeridos",Toast.LENGTH_SHORT).show();
                } else {
                    if(!existeCandidato(dni)){
                        manejarPlanos.addCandidato(archCandidatos,pathImage,dni,nombres,apellidos,partido);
                        resetCampos();
                    } else {
                        Toast.makeText(getContext(),"Ya existe un candidato con ese DNI",Toast.LENGTH_SHORT).show();
                        etDni.setText("");
                    }
                }
            }
        });
        return view;
    }

    public void resetCampos() {
        imgPerfil.setImageResource(R.drawable.img_start);
        btnImage.setText("seleccionar imagen del candidato");
        etDni.setText("");
        etNombres.setText("");
        etApellidos.setText("");
        etPartido.setText("");
    }

    public boolean existeCandidato(String dni) {
        for (Candidato c: lstCandidatos) {
            if(c.getDni().equals(dni)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            Uri uri = data.getData();
            pathImage = getPath(uri);
            //Toast.makeText(getContext(), "Path : " + pathImage, Toast.LENGTH_SHORT).show();
            imgFile = new File(pathImage);
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imgPerfil.setImageBitmap(myBitmap);
                btnImage.setText("");
            }
        }
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContext().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
}
