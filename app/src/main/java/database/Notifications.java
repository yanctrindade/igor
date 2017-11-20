package database;

public class Notifications {

    private String usuarioDestinatario, usuarioRemetente;
    private String aventuraNome, sessaoNome, dataAgenda, dataEnvio, dataRecebimento;

    public Notifications() {
    }

    public Notifications(String usuarioDestinatario, String usuarioRemetente, String aventuraNome,
                         String sessaoNome, String dataAgenda, String dataEnvio, String dataRecebimento) {
        this.usuarioDestinatario = usuarioDestinatario;
        this.usuarioRemetente = usuarioRemetente;
        this.aventuraNome = aventuraNome;
        this.sessaoNome = sessaoNome;
        this.dataAgenda = dataAgenda;
        this.dataEnvio = dataEnvio;
        this.dataRecebimento = dataRecebimento;
    }

    public String getUsuarioDestinatario() {
        return usuarioDestinatario;
    }

    public void setUsuarioDestinatario(String usuarioDestinatario) {
        this.usuarioDestinatario = usuarioDestinatario;
    }

    public String getUsuarioRemetente() {
        return usuarioRemetente;
    }

    public void setUsuarioRemetente(String usuarioRemetente) {
        this.usuarioRemetente = usuarioRemetente;
    }

    public String getAventuraNome() {
        return aventuraNome;
    }

    public void setAventuraNome(String aventuraNome) {
        this.aventuraNome = aventuraNome;
    }

    public String getSessaoNome() {
        return sessaoNome;
    }

    public void setSessaoNome(String sessaoNome) {
        this.sessaoNome = sessaoNome;
    }

    public String getDataAgenda() {
        return dataAgenda;
    }

    public void setDataAgenda(String dataAgenda) {
        this.dataAgenda = dataAgenda;
    }

    public String getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(String dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public String getDataRecebimento() {
        return dataRecebimento;
    }

    public void setDataRecebimento(String dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
    }

}
