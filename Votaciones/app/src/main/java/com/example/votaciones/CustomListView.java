package com.example.votaciones;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class CustomListView extends ArrayAdapter<String> {

    private ArrayList<Candidato> lstCandidatos;
    public CustomListView(@NonNull Context context, int resource) {
        super(context, resource);
    }
}
