package database;

import java.util.ArrayList;

/**
 * Created by renne on 09/11/2017.
 */

public class Adventure {
    private String nome;
    private String descricao;
    private String mestre;
    private ArrayList<String> jogadores;
    private ArrayList<Session> sessoes;

    public Adventure() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Adventure(String nome, String descricao, String mestre,  ArrayList<String> jogadores) {
        this.nome = nome;
        this.descricao = descricao;
        this.mestre = mestre;
        this.jogadores = jogadores;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMestre() {
        return mestre;
    }

    public void setMestre(String mestre) {
        this.mestre = mestre;
    }

    public ArrayList<String> getJogadores() {
        return jogadores;
    }

    public void setJogadores( ArrayList<String> jogadores) {
        this.jogadores = jogadores;
    }

    public ArrayList<Session> getSessoes() { return sessoes; }

    public void setSessoes( ArrayList<Session> sessoes) {this.sessoes = sessoes; }
}
