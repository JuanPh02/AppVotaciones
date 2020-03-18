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

import java.io.FileNotFoundException;
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
    Uri path;
    Bitmap bmp;
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
                if(!existeCandidato(dni)){
                    manejarPlanos.addCandidato(archCandidatos,bmp,dni,nombres,apellidos,partido);
                    resetCampos();
                } else {
                    Toast.makeText(getContext(),"Ya existe un candidato con ese DNI",Toast.LENGTH_SHORT).show();
                    etDni.setText("");
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
        if(resultCode == RESULT_OK){
            Uri path = data.getData();

            InputStream pictureInputStream;
            try{
                pictureInputStream = getContext().getContentResolver().openInputStream(path);
                bmp = BitmapFactory.decodeStream(pictureInputStream);
                imgPerfil.setImageBitmap(bmp);
                btnImage.setText("");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            //imgPerfil.setImageURI(path);
        }
    }

    public static String getRealPath(final Context context, final Uri uri) {

        if (uri.getScheme().equals("file")) {
            return uri.toString();

        } else if (uri.getScheme().equals("content")) {
            // DocumentProvider
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (DocumentsContract.isDocumentUri(context, uri)) {

                    // ExternalStorageProvider
                    if (isExternalStorageDocument(uri)) {
                        final String docId = DocumentsContract.getDocumentId(uri);
                        final String[] split = docId.split(":");
                        final String type = split[0];

                        if ("primary".equalsIgnoreCase(type)) {
                            return Environment.getExternalStorageDirectory() + "/" + split[1];
                        }

                        // TODO handle non-primary volumes
                    }
                    // DownloadsProvider
                    else if (isDownloadsDocument(uri)) {

                        final String id = DocumentsContract.getDocumentId(uri);
                        final Uri contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                        return getDataColumn(context, contentUri, null, null);
                    }
                    // MediaProvider
                    else if (isMediaDocument(uri)) {
                        final String docId = DocumentsContract.getDocumentId(uri);
                        final String[] split = docId.split(":");
                        final String type = split[0];

                        Uri contentUri = null;
                        if ("image".equals(type)) {
                            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        } else if ("video".equals(type)) {
                            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        } else if ("audio".equals(type)) {
                            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        }

                        final String selection = "_id=?";
                        final String[] selectionArgs = new String[]{
                                split[1]
                        };

                        return getDataColumn(context, contentUri, selection, selectionArgs);
                    }
                }
            }
    /*
    // MediaStore (and general)
    else if ("content".equalsIgnoreCase(uri.getScheme())) {
        return getDataColumn(context, uri, null, null);
    }
    // File
    else if ("file".equalsIgnoreCase(uri.getScheme())) {
        return uri.getPath();
    }*/
        }

        return null;
    }


    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
