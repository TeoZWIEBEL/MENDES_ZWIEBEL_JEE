package gestionnaires;

import authentification.Compte;
import classes.*;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

import java.util.ArrayList;

/**
 * Opérations faites en rapport avec les {@link Participant} sur la BDD.
 * RequestScoped, on fait une opération lors d'une requête, on aura pas besoin de plus d'intéractions.
 */
@Transactional
@RequestScoped
public class ParticipantGestion {

    @PersistenceContext
    EntityManager em;

    @Inject
    EquipeGestion equipeGestionnaire;

    /**
     * Recherche d'un {@link Participant} avec son pseudo.
     */
    public Participant rechercherParticipant(String pseudo) {
        try {
            Query emQuery = em.createQuery("SELECT p FROM Participant p WHERE p.pseudo = :pseudo", Participant.class);
            emQuery.setParameter("pseudo", pseudo);
            return (Participant) emQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    /**
     * Retourne tous les {@link Participant}.
     */
    public ArrayList<Participant> tousParticipants() {
        Query emQuery = em.createQuery("SELECT p FROM Participant p ORDER BY p.score DESC", Participant.class);
        return (ArrayList<Participant>) emQuery.getResultList();
    }

    /**
     * Inscription d'un {@link Participant} dans la BDD du serveur d'Authentification et également dans la table des {@link Participant}.
     */
    public void inscrireParticipant(String pseudo, String mail, String mdp) {
        em.persist(new Participant(pseudo, mail));
        Compte.add(pseudo, mdp, "participant");
    }


    /**
     * Un {@link Participant} quitte son {@link Equipe}.
     * On modifie bien un participant ici, en base l'id de l'équipe est dans la
     * table participant.
     */
    public void quitterEquipe(String pseudo) {
        Participant monParticipant = rechercherParticipant(pseudo);
        monParticipant.setEquipe(null);
        em.persist(monParticipant);
    }

    /**
     * Verification, est-ce que le {@link Participant} est un chef d'{@link Equipe} ?
     */
    public boolean checkChef(String pseudoChef){
        return equipeGestionnaire.rechercherEquipeChef(pseudoChef) != null;
    }
}
