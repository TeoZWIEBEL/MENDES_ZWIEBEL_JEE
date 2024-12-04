package classes;

import jakarta.persistence.*;

/**
 * Classe Utilisateur permettant de faire de l'héritage.
 * On fait l'héritage en tant que table par classe.
 * Cela nous permettra de garder une bonne logique en BDD.
 * On évite également d'avoir trop de redondance de code.
 * Les classes filles sont {@link Administrateur}, {@link Organisateur}, {@link Participant}.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    protected int id;

    @Access(AccessType.PROPERTY)
    protected String pseudo;
    @Access(AccessType.PROPERTY)
    protected String mail;

    public Utilisateur(String pseudo, String mail) {
        this.pseudo = pseudo;
        this.mail = mail;
    }

    public Utilisateur() {
    }

    // getters & setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}


