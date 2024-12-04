package classes;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;

@Entity
public class Commentaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Access(AccessType.PROPERTY)
    private String commentaire;
    @ManyToOne(cascade = CascadeType.ALL)
    @JsonbTransient
    private CTF ctf;
    @ManyToOne(cascade = CascadeType.ALL)
    private Participant participant;

    public Commentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Commentaire() {
    }
    // getters & setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public CTF getCtf() {
        return ctf;
    }

    public void setCtf(CTF ctf) {
        this.ctf = ctf;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
}
