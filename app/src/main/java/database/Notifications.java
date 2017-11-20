package database;

public class Notifications {

    private int usuarioDestinatario, usuarioRemetente;
    private String aventuraNome, dateAgenda, dateEnvio, dateRecebimento;

    public Notifications() {
    }

    public Notifications(int usuarioDestinatario, int usuarioRemetente, String aventuraNome,
                         String dateAgenda, String dateEnvio, String dateRecebimento) {
        this.usuarioDestinatario = usuarioDestinatario;
        this.usuarioRemetente = usuarioRemetente;
        this.aventuraNome = aventuraNome;
        this.dateAgenda = dateAgenda;
        this.dateEnvio = dateEnvio;
        this.dateRecebimento = dateRecebimento;
    }

    public int getUsuarioDestinatario() {
        return usuarioDestinatario;
    }

    public void setUsuarioDestinatario(int usuarioDestinatario) {
        this.usuarioDestinatario = usuarioDestinatario;
    }

    public int getUsuarioRemetente() {
        return usuarioRemetente;
    }

    public void setUsuarioRemetente(int usuarioRemetente) {
        this.usuarioRemetente = usuarioRemetente;
    }

    public String getAventuraNome() {
        return aventuraNome;
    }

    public void setAventuraNome(String aventuraNome) {
        this.aventuraNome = aventuraNome;
    }

    public String getDateAgenda() {
        return dateAgenda;
    }

    public void setDateAgenda(String dateAgenda) {
        this.dateAgenda = dateAgenda;
    }

    public String getDateEnvio() {
        return dateEnvio;
    }

    public void setDateEnvio(String dateEnvio) {
        this.dateEnvio = dateEnvio;
    }

    public String getDateRecebimento() {
        return dateRecebimento;
    }

    public void setDateRecebimento(String dateRecebimento) {
        this.dateRecebimento = dateRecebimento;
    }

}
