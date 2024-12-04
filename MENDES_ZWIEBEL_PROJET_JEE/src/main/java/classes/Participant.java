package classes;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Participant extends Utilisateur {
    @Access(AccessType.PROPERTY)
    private int score;

    @JsonbTransient
    @OneToMany
    private List<ParticipantCTF> ctfs;

    @ManyToOne
    @JsonbTransient
    private Equipe equipe;

    public Participant(String pseudo, String mail) {
        super(pseudo, mail);
        this.score = 0;
        this.ctfs = new ArrayList<>();
    }

    public Participant() {
    }

    // getters et Setters

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public List<ParticipantCTF> getCtfs() {
        return this.ctfs;
    }

    public void setCtfs(List<ParticipantCTF> ctfs) {
        this.ctfs = ctfs;
    }

    public Equipe getEquipe() {
        return equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }
}
