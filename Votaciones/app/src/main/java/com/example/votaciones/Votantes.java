package com.example.votaciones;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Votantes extends Fragment {


    public Votantes() {
        // Required empty public constructor
    }

    EditText etDni, etNombres, etApellidos, etTelefono;
    Button btnAddVotante;
    ArrayList<Votante> lstVotantes;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_votantes, container, false);

        etDni = view.findViewById(R.id.etDni);
        etNombres = view.findViewById(R.id.etNombres);
        etApellidos = view.findViewById(R.id.etApellidos);
        etTelefono = view.findViewById(R.id.etTelefono);
        btnAddVotante = view.findViewById(R.id.btnAddVotante);
        final ManejarPlanos manejarPlanos = new ManejarPlanos(getContext());
        final String archVotantes = "Votantes.txt";

        btnAddVotante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dni, nombres, apellidos, telefono;
                dni = etDni.getText().toString();
                nombres = etNombres.getText().toString();
                apellidos = etApellidos.getText().toString();
                telefono = etTelefono.getText().toString();
                lstVotantes = manejarPlanos.leerVotantes(archVotantes);
                if(dni.isEmpty() || nombres.isEmpty() || apellidos.isEmpty() || telefono.isEmpty()){
                    Toast.makeText(getContext(),"Ingrese todos los datos requeridos",Toast.LENGTH_SHORT).show();
                } else {
                    if(!existeVotante(dni)){
                        manejarPlanos.addVotante(archVotantes,dni,nombres,apellidos,telefono);
                        resetCampos();
                    } else {
                        Toast.makeText(getContext(),"Ya existe un votante con ese DNI",Toast.LENGTH_SHORT).show();
                        etDni.setText("");
                    }
                }
            }
        });

        return view;
    }

    public void resetCampos() {
        etDni.setText("");
        etNombres.setText("");
        etApellidos.setText("");
        etTelefono.setText("");
    }

    public boolean existeVotante(String dni) {
        for (Votante v: lstVotantes) {
            if(v.getDni().equals(dni)) {
                return true;
            }
        }
        return false;
    }

}
