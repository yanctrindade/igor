package database;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String email;
    private String username;
    private String nascimento;
    private String sexo;
    private List<String> aventuras;
    private List<Notifications> notifications;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String username, String nascimento, String sexo) {
        this.email = email;
        this.username = username;
        this.nascimento = nascimento;
        this.sexo = sexo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNascimento() {
        return nascimento;
    }

    public void setNascimento(String nascimento) {
        this.nascimento = nascimento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public List<String> getAventuras() {
        return aventuras;
    }

    public void setAventuras(List<String> aventuras) {
        this.aventuras = aventuras;
    }

    public List<Notifications> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notifications> notifications) {
        this.notifications = notifications;
    }

    public void addNotification(Notifications notification){
        if(this.notifications == null) notifications = new ArrayList<>();
        this.notifications.add(notification);
    }
}