package gestionnaires;

import classes.Equipe;
import classes.Participant;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import java.util.ArrayList;

/**
 * Opérations faites en rapport avec les {@link Equipe} sur la BDD.
 * RequestScoped, on fait une opération lors d'une requête, on aura pas besoin de plus d'intéractions.
 */
@Transactional
@RequestScoped
public class EquipeGestion {

    @PersistenceContext
    EntityManager em;

    @Inject
    ParticipantGestion participantGestionnaire;

    @Inject
    CTFGestion ctfGestionnaire;

    /**
     * Recherche d'une {@link Equipe} avec son nom.
     */
    public Equipe rechercherEquipe(String nom) {
        try {
            Query emQuery = em.createQuery("SELECT e FROM Equipe e WHERE e.nom = :nom", Equipe.class);
            emQuery.setParameter("nom", nom);
            return (Equipe) emQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Recherche des {@link Participant} membres d'une {@link Equipe} donnée.
     */
    public ArrayList<Participant> rechercherMembresEquipe(String nom) {
        Query emQuery = em.createQuery("SELECT p FROM Participant p WHERE p.equipe.nom = :nom", Participant.class);
        emQuery.setParameter("nom", nom);
        return (ArrayList<Participant>) emQuery.getResultList();
    }

    /**
     * Retourne toutes les {@link Equipe}.
     */
    public ArrayList<Equipe> toutesEquipe() {
        Query emQuery = em.createQuery("SELECT e FROM Equipe e", Equipe.class);
        return (ArrayList<Equipe>) emQuery.getResultList();
    }

    /**
     * Création d'une {@link Equipe} par un {@link Participant} qui sera donc chef de celle-ci.
     */
    public void creerEquipe(String nom, String description, String pseudo) {
        Participant createur = participantGestionnaire.rechercherParticipant(pseudo);
        if (rechercherEquipeChef(pseudo) == null && createur.getEquipe() == null) {
            Equipe monEquipe = new Equipe(nom, description, createur);
            createur.setEquipe(monEquipe);
            em.persist(monEquipe);
        } else {
            throw new EntityExistsException("Le participant est déjà membre ou chef d'équipe.");
        }
    }

    /**
     * Retourne une {@link Equipe} en fonction du pseudonyme du {@link Participant} chef.
     */
    public Equipe rechercherEquipeChef(String pseudo) {
        try {
            Query emQuery = em.createQuery("SELECT e FROM Equipe e JOIN FETCH e.chef WHERE e.chef.pseudo = :pseudo", Equipe.class);
            emQuery.setParameter("pseudo", pseudo);
            return (Equipe) emQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Transfère du chef d'une {@link Equipe} à un autre membre de l'équipe.
     */
    public void transfererChef(String pseudoChef, String pseudoNouveauChef) {
        Equipe monEquipe = rechercherEquipeChef(pseudoChef);
        Participant nouveauChef = participantGestionnaire.rechercherParticipant(pseudoNouveauChef);

        if (nouveauChef != null && nouveauChef.getEquipe() == monEquipe) {
            monEquipe.setChef(nouveauChef);
        } else {
            throw new EntityNotFoundException("Le nouveau chef n'existe pas ou n'est pas membre de l'équipe.");
        }
    }

    /**
     * Inscription de tout les {@link Participant} membres d'une {@link Equipe} à un {@link classes.CTF}.
     */
    public void inscrireEquipe(String pseudoChef, String titre) {
        Equipe monEquipe = rechercherEquipeChef(pseudoChef);
        ArrayList<Participant> lesMembres = rechercherMembresEquipe(monEquipe.getNom());
        for (int i = 0; i < lesMembres.size(); i++) {
            ctfGestionnaire.ajoutParticipantCTF(lesMembres.get(i).getPseudo(), titre);
        }
    }


}
