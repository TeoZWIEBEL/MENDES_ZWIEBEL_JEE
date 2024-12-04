package classes;

import jakarta.persistence.Entity;

@Entity
public class Organisateur extends Utilisateur {

    public Organisateur(String pseudo, String mail) {
        super(pseudo, mail);
    }

    public Organisateur() {
    }

}
