package classes;

import jakarta.persistence.*;

/**
 * Table permettant de faire la relation ManyToMany pour les {@link Participant}
 * à des {@link CTF}. On n'utilise pas simplement l'annotation afin de rajouter
 * un score pour le ctf.
 */
@Entity
public class ParticipantCTF {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private CTF ctf;

    @ManyToOne
    private Participant participant;

    @Access(AccessType.PROPERTY)
    private int score;

    public ParticipantCTF(CTF ctf, Participant participant, int score) {
        this.ctf = ctf;
        this.participant = participant;
        this.score = score;
    }

    public ParticipantCTF() {
    }

    // getters & setters

    public CTF getCtf() {
        return ctf;
    }

    public void setCtf(CTF ctf) {
        this.ctf = ctf;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
