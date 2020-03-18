package com.example.votaciones;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.jjoe64.graphview.GraphView;

import java.util.ArrayList;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class Cierre extends Fragment {


    public Cierre() {
        // Required empty public constructor
    }

    ArrayList<Candidato> lstCandidatos;
    ArrayList<Votante> lstVotantes;
    ArrayList<Voto> lstVotos;
    String[] candidatos;
    int[] nVotosXCandidato;
    float[] porcentajes;
    int[] colors;
    int totalVotos = 0;
    Button btnCerrar;
    BarChart grafico;
    TextView tvI;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cierre, container, false);

        grafico = view.findViewById(R.id.grafBarras);
        tvI = view.findViewById(R.id.tvI);
        btnCerrar = view.findViewById(R.id.btnCerrarVot);

        final ManejarPlanos manejarPlanos = new ManejarPlanos(getContext());
        final String archCandidatos = "Candidatos.txt";
        final String archVotos = "Votos.txt";
        lstCandidatos = manejarPlanos.leerCandidatos(archCandidatos);
        lstVotos = manejarPlanos.leerVotos(archVotos);
        candidatos = arrayCandidatos();
        nVotosXCandidato = conteoVotos();
        colors = arrayColors();

        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Confirmacion");
                builder.setMessage("Desea cerrar y finalizar las votaciones ?");
                builder.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        lstCandidatos = manejarPlanos.leerCandidatos(archCandidatos);
                        lstVotos = manejarPlanos.leerVotos(archVotos);
                        candidatos = arrayCandidatos();
                        nVotosXCandidato = conteoVotos();
                        porcentajes = getPorcentajes();
                        datos();
                        crearGrafico();
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

    public float[] getPorcentajes() {
        float[] porcentajes = new float[nVotosXCandidato.length];
        totalVotos = 0;
        for (int i = 0; i < nVotosXCandidato.length; i++){
            totalVotos += nVotosXCandidato[i];
        }
        for (int i = 0; i < nVotosXCandidato.length; i++){
            porcentajes[i] = nVotosXCandidato[i]*100/totalVotos;
        }
        return porcentajes;
    }

    public void datos() {
        String lect = "";
        for (int i = 0; i < lstCandidatos.size(); i++){
            lect += candidatos[i] + "-> " + porcentajes[i] + " % de votos" + '\n';
        }
        lect += '\n' + "Total de votos: " + totalVotos;
        tvI.setText(lect);
    }

    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    private Chart getSameChart(Chart chart, String description, int textColor, int background, int animateY) {
        chart.getDescription().setText(description);
        chart.getDescription().setTextSize(15);
        chart.getDescription().setTextColor(textColor);
        chart.setBackgroundColor(background);
        chart.animateY(animateY);
        legend(chart);
        return chart;
    }

    private void legend(Chart chart) {
        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        ArrayList<LegendEntry> entries = new ArrayList<>();
        for(int i = 0; i < candidatos.length;i++) {
            LegendEntry entry = new LegendEntry();
            entry.formColor = colors[i];
            entry.label = candidatos[i];
            entries.add(entry);
        }
        legend.setCustom(entries);
    }

    private ArrayList<BarEntry> getBarEntries() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for(int i = 0; i < candidatos.length;i++) {
            entries.add(new BarEntry(i, nVotosXCandidato[i]));
        }
        return  entries;
    }

    private void axisX(XAxis axis){
        axis.setGranularityEnabled(true);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setValueFormatter(new IndexAxisValueFormatter(candidatos));
        axis.setEnabled(false);
    }

    private void axisLeft(YAxis axis) {
        axis.setSpaceTop(40);
        axis.setAxisMinimum(0);
        axis.setGranularity(1);
    }

    private void axisRight(YAxis axis) {
        axis.setEnabled(false);
    }

    private DataSet getData(DataSet dataSet) {
        dataSet.setColors(colors);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(10);
        return dataSet;
    }

    private BarData getBarData() {
        BarDataSet barDataSet = (BarDataSet) getData(new BarDataSet(getBarEntries(), ""));
        barDataSet.setBarShadowColor(Color.TRANSPARENT);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.45f);
        return barData;
    }

    public void crearGrafico() {
        grafico = (BarChart) getSameChart(grafico,"Votos",Color.RED, Color.TRANSPARENT,3500);
        grafico.setDrawGridBackground(true);
        grafico.setDrawBarShadow(true);
        grafico.setData(getBarData());
        grafico.invalidate();
        axisX(grafico.getXAxis());
        axisLeft(grafico.getAxisLeft());
        axisRight(grafico.getAxisRight());
        //grafico.getLegend().setEnabled(false);
    }

    public int[] arrayColors() {
        int[] colors = new int[lstCandidatos.size()];
        for (int i = 0; i < lstCandidatos.size(); i++){
            colors[i] = getRandomColor();
        }
        return colors;
    }

    public String[] arrayCandidatos() {
        String [] candidatos = new String[lstCandidatos.size()];
        for (int i = 0; i < lstCandidatos.size(); i++){
            candidatos[i] = lstCandidatos.get(i).getNombres() + " " + lstCandidatos.get(i).getApellidos();
        }
        return candidatos;
    }

    public int[] conteoVotos() {
        int[] nVotosXCandidato = new int[lstCandidatos.size()];
        for (int i = 0; i < lstVotos.size(); i++){
            for(int j = 0; j < lstCandidatos.size(); j++) {
                if(lstVotos.get(i).getVoto() == j){
                    nVotosXCandidato[j] ++;
                }
            }
        }
        return nVotosXCandidato;
    }
}
