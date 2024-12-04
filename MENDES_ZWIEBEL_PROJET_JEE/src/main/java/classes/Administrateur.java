package classes;

import jakarta.persistence.Entity;

@Entity
public class Administrateur extends Utilisateur {

    public Administrateur(String pseudo, String mail) {
        super(pseudo, mail);
    }

    public Administrateur() {
    }

}
