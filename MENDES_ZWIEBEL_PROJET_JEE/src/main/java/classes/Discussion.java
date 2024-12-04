package classes;

import authentification.Compte;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Discussion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private Compte utilisateur1;
    @ManyToOne
    private Compte utilisateur2;
    @Access(AccessType.PROPERTY)
    private List<String> messages;

    public Discussion(Compte utilisateur1, Compte utilisateur2) {
        this.utilisateur1 = utilisateur1;
        this.utilisateur2 = utilisateur2;
        this.messages = new ArrayList<>();
    }

    public Discussion() {
    }

    // getters & setters

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Compte getUtilisateur1() {
        return this.utilisateur1;
    }

    public Compte getUtilisateur2() {
        return this.utilisateur2;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void ajouterMessage(String message) {
        messages.add(message);
    }


}
