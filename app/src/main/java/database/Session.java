package database;

import java.util.ArrayList;
import java.util.List;

public class Session {
    private String titulo;
    private String data;
    private List<String> jogadoresConfirmados, jogadoresCancelados, jogadoresConvidados;

    public Session(){

    }

    public Session(String titulo, String data, List<String> jogadoresConfirmados,
                   List<String> jogadoresCancelados, List<String> jogadoresConvidados) {
        this.titulo = titulo;
        this.data = data;
        if(jogadoresConfirmados != null) this.jogadoresConfirmados = jogadoresConfirmados;
        else this.jogadoresConfirmados = new ArrayList<>();
        if(jogadoresCancelados != null) this.jogadoresCancelados = jogadoresCancelados;
        else this.jogadoresCancelados = new ArrayList<>();
        if(jogadoresConvidados != null) this.jogadoresConvidados = jogadoresConvidados;
        else this.jogadoresConvidados = new ArrayList<>();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) { this.data = data; }

    public List<String> getJogadoresConfirmados() {
        if(this.jogadoresConfirmados == null) this.jogadoresConfirmados = new ArrayList<>();
        return jogadoresConfirmados;
    }

    public void setJogadoresConfirmados(List<String> jogadoresConfirmados) {
        this.jogadoresConfirmados = jogadoresConfirmados;
    }

    public List<String> getJogadoresCancelados() {
        if(this.jogadoresCancelados == null) this.jogadoresCancelados = new ArrayList<>();
        return jogadoresCancelados;
    }

    public void setJogadoresCancelados(List<String> jogadoresCancelados) {
        this.jogadoresCancelados = jogadoresCancelados;
    }

    public List<String> getJogadoresConvidados() {
        if(this.jogadoresConvidados == null) this.jogadoresConvidados = new ArrayList<>();
        return jogadoresConvidados;
    }

    public void setJogadoresConvidados(List<String> jogadoresConvidados) {
        this.jogadoresConvidados = jogadoresConvidados;
    }
}
