package classes;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;

@Entity
public class Demande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JsonbTransient
    private Equipe equipe;

    @OneToOne
    private Participant participant;

    public Demande(Equipe equipe, Participant participant) {
        this.equipe = equipe;
        this.participant = participant;
    }

    public Demande() {
    }
    // getters & setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Equipe getEquipe() {
        return equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }
}
