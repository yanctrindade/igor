package database;

import java.util.ArrayList;
import java.util.List;

public class Adventure {
    private String nome;
    private String descricao;
    private String mestre;
    private String data;
    private List<String> jogadores = new ArrayList<>();
    private List<Session> sessoes = new ArrayList<>();
    private int background;

    public Adventure() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Adventure(String nome, String descricao, String mestre,  List<String> jogadores, List<Session> sessoes, int background, String data) {
        this.nome = nome;
        this.descricao = descricao;
        this.mestre = mestre;
        this.jogadores = jogadores;
        this.sessoes = sessoes;
        this.background = background;
        this.data = data;
    }

    public String getData(){ return  data; };

    public void setData(String data){ this.data = data; }

    public int getBackground() { return background; }

    public void setBackground(int background) { this.background = background; }

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

    public List<String> getJogadores() {
        return jogadores;
    }

    public void setJogadores( ArrayList<String> jogadores) {
        this.jogadores = jogadores;
    }

    public List<Session> getSessoes() {
        if(sessoes == null) sessoes = new ArrayList<>();
        return sessoes;
    }

    public void addSessao(Session session){
        sessoes.add(session);
    }

    public void setSessoes( List<Session> sessoes) {this.sessoes = sessoes; }
}
