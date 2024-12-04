package classes;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "titre"))
public class CTF {

    private enum Etat {
        ATTENTE,
        VALIDE,
        REFUSE,
        TERMINE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Access(AccessType.PROPERTY)
    private String titre;
    @Access(AccessType.PROPERTY)
    private String description;
    @Access(AccessType.PROPERTY)
    private String lieu;
    @ManyToOne(targetEntity = Organisateur.class)
    private Organisateur contact;
    @Access(AccessType.PROPERTY)
    private int vues;
    @OneToMany(targetEntity = Commentaire.class, mappedBy = "ctf")
    @JsonbTransient
    private List<String> commentaires;
    @Access(AccessType.PROPERTY)
    private Etat etat;

    @OneToMany(mappedBy = "ctf", cascade = CascadeType.ALL)
    @JsonbTransient
    private List<ParticipantCTF> participants;

    public CTF(String titre, String description, String lieu, Organisateur contact) {
        this.titre = titre;
        this.description = description;
        this.lieu = lieu;
        this.contact = contact;
        this.vues = 0;
        this.commentaires = new ArrayList<>();
        this.etat = Etat.ATTENTE;
        this.participants = new ArrayList<>();
    }

    public CTF() {
    }

    //getter & setters

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public Organisateur getContact() {
        return contact;
    }

    public void setContact(Organisateur contact) {
        this.contact = contact;
    }

    public int getVues() {
        return vues;
    }

    public void addVues() {
        this.vues++;
    }

    public List<String> getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(ArrayList<String> commentaires) {
        this.commentaires = commentaires;
    }

    public void ajouterCommentaire(String commentaire) {
        commentaires.add(commentaire);
    }

    public void supprimerCommentaire(String commentaire) {
        commentaires.remove(commentaire);
    }

    public Etat getEtat() {
        return etat;
    }

    public List<ParticipantCTF> getParticipants() {
        return this.participants;
    }

    public void setParticipants(List<ParticipantCTF> participants) {
        this.participants = participants;
    }

    public void addParticipant(ParticipantCTF pc) {
        participants.add(pc);
    }

    public void removeParticipant(ParticipantCTF pc) {
        participants.remove(pc);
    }

    public void validerCTF() {
        this.etat = Etat.VALIDE;
    }

    public void terminerCTF() {
        this.etat = Etat.TERMINE;
    }

    public void refuserCTF() {
        this.etat = Etat.REFUSE;
    }
}
