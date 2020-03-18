package com.example.votaciones;

public class Voto {

    private String dniVotante;
    private int voto;

    public Voto(String dniVotante, int voto) {
        this.dniVotante = dniVotante;
        this.voto = voto;
    }

    public String getDniVotante() {
        return dniVotante;
    }

    public int getVoto() {
        return voto;
    }

}
