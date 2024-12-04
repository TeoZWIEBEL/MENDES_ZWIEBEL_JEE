package classes;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Defis {

    public enum Categorie {
        FUN,
        DIFFICILE,
        NORMAL,
        FACILE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Access(AccessType.PROPERTY)
    private String titre;
    @Access(AccessType.PROPERTY)
    private int points;
    @Access(AccessType.PROPERTY)
    private Categorie categorie;
    @ManyToMany
    @JsonbTransient
    private List<Participant> participants;
    public Defis(String titre, int points, Categorie categorie) {
        this.titre = titre;
        this.points = points;
        this.categorie = categorie;
        this.participants = new ArrayList<>();
    }
    public Defis() {
    }

    // getters & setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participant) {
        this.participants = participant;
    }

    public void addParticipant(Participant participant){
        this.participants.add(participant);
    }
}

