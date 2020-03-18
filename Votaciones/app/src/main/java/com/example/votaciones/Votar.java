package com.example.votaciones;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Votar extends Fragment {


    public Votar() {
        // Required empty public constructor
    }

    EditText etDni;
    Button btnIrAVotacion;
    TextView tvInfo, tvNombre;
    ArrayList<Candidato> lstCandidatos;
    ArrayList<Votante> lstVotantes;
    ArrayList<Voto> lstVotos;
    ListView lvCandidatos;
    ListViewAdapter adapter;
    View view;
    Context ctx;
    Votante votanteActual;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_votar, container, false);
        etDni = view.findViewById(R.id.etDniVot);
        btnIrAVotacion = view.findViewById(R.id.btnIrAVotacion);
        tvInfo = view.findViewById(R.id.tvInfoVotacion);
        tvNombre = view.findViewById(R.id.tvNombre);
        lvCandidatos = view.findViewById(R.id.lvCandidatos);
        final ManejarPlanos manejarPlanos = new ManejarPlanos(getContext());
        final String archCandidatos = "Candidatos.txt";
        final String archVotantes = "Votantes.txt";
        final String archVotos = "Votos.txt";
        lstCandidatos = manejarPlanos.leerCandidatos(archCandidatos);
        lstVotantes = manejarPlanos.leerVotantes(archVotantes);
        lstVotos = manejarPlanos.leerVotos(archVotos);
        lvCandidatos = view.findViewById(R.id.lvCandidatos);
        ctx = this.getContext();

        btnIrAVotacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dni = etDni.getText().toString().trim();
                lstCandidatos = manejarPlanos.leerCandidatos(archCandidatos);
                lstVotantes = manejarPlanos.leerVotantes(archVotantes);
                lstVotos = manejarPlanos.leerVotos(archVotos);
                adapter = new ListViewAdapter(ctx, lstCandidatos);
                lvCandidatos.setAdapter(adapter);
                if(!dni.isEmpty()) {
                    if(existeVotante(dni)){
                        if(!yaVoto(dni)) {
                            etDni.setVisibility(View.INVISIBLE);
                            btnIrAVotacion.setVisibility(View.INVISIBLE);
                            lvCandidatos.setVisibility(View.VISIBLE);
                            tvNombre.setText("Bienvenido(a), " + votanteActual.getNombres() + " " + votanteActual.getApellidos());
                            tvInfo.setText("Haga click sobre un candidato para votar");
                        } else {
                            Toast.makeText(getContext(),"El señor(a), " + votanteActual.getNombres() + " " + votanteActual.getApellidos() + " ya realizó su votacion", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getContext(),"No existe votante registrado con ese DNI", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(),"Ingrese un DNI por favor", Toast.LENGTH_LONG).show();
                }
                etDni.setText("");
            }
        });

        lvCandidatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Confirmacion");
                builder.setMessage("Desea realizar su voto por " + lstCandidatos.get(position).getNombres() + " " + lstCandidatos.get(position).getApellidos());
                builder.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        manejarPlanos.newVoto(archVotos,votanteActual.getDni(), position);
                        resetCampos();
                    }
                });

                builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return view;
    }

    public void resetCampos(){
        etDni.setText("");
        etDni.setVisibility(View.VISIBLE);
        btnIrAVotacion.setVisibility(View.VISIBLE);
        lvCandidatos.setVisibility(View.INVISIBLE);
        tvInfo.setText("INGRESO VOTANTE");
        tvNombre.setText("");
    }

    public boolean yaVoto(String dni) {
        for (Voto voto: lstVotos) {
            if(voto.getDniVotante().equals(dni)) {
                return true;
            }
        }
        return false;
    }

    public boolean existeVotante(String dni) {
        for (Votante v: lstVotantes) {
            if(v.getDni().equals(dni)) {
                votanteActual = v;
                return true;
            }
        }
        votanteActual = null;
        return false;
    }

}
