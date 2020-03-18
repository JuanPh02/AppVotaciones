package com.example.votaciones;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.provider.CalendarContract.CalendarCache.URI;


public class ListViewAdapter extends BaseAdapter {

    Context context;
    ArrayList<Candidato> lstCandidatos;
    LayoutInflater inflater;

    public ListViewAdapter(Context context, ArrayList<Candidato> lstCandidatos) {
        this.context = context;
        this.lstCandidatos = lstCandidatos;
    }

    @Override
    public int getCount() {
        return lstCandidatos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView tvNombre, tvDni, tvPartido;
        ImageView imgCandidato;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.list_row, parent, false);

        imgCandidato = itemView.findViewById(R.id.imgCandidatolst);
        tvNombre = itemView.findViewById(R.id.tvNombre);
        tvDni = itemView.findViewById(R.id.tvDni);
        tvPartido = itemView.findViewById(R.id.tvPartido);

        if(lstCandidatos.get(position).getNombres().equals("Voto en Blanco")) {
            imgCandidato.setImageResource(R.drawable.ico_votob);
        } else {
            File imgFile = new File(lstCandidatos.get(position).getPath());
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imgCandidato.setImageBitmap(myBitmap);
            }
        }

        tvNombre.setText(lstCandidatos.get(position).getNombres() + " " + lstCandidatos.get(position).getApellidos());
        tvDni.setText(lstCandidatos.get(position).getDni());
        tvPartido.setText(lstCandidatos.get(position).getPartido());

        return itemView;
    }

}
