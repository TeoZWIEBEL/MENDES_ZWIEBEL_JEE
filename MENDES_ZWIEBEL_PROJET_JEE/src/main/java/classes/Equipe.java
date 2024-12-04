package classes;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "nom"))
public class Equipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Access(AccessType.PROPERTY)
    private String nom;
    @OneToMany
    @JsonbTransient
    private List<Participant> membres;

    @ManyToOne
    private Participant chef;

    @Access(AccessType.PROPERTY)
    private String description;

    public Equipe(String nom, String description, Participant chef) {
        this.nom = nom;
        this.membres = new ArrayList<>();
        this.chef = chef;
        this.description = description;
    }

    public Equipe() {
    }

    // getters & setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Participant> getMembres() {
        return membres;
    }

    public void setMembres(List<Participant> membres) {
        this.membres = membres;
    }

    public void ajouterMembre(Participant membre) {
        membres.add(membre);
    }

    public void supprimerMembre(Participant membre) {
        membres.remove(membre);
    }

    public Participant getChef() {
        return chef;
    }

    public void setChef(Participant chef) {
        this.chef = chef;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
