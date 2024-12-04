package gestionnaires;

import authentification.Compte;
import classes.Equipe;
import classes.Organisateur;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

import java.util.ArrayList;

/**
 * Opérations faites en rapport avec les {@link Equipe} sur la BDD.
 * RequestScoped, on fait une opération lors d'une requête, on aura pas besoin de plus d'intéractions.
 */
@Transactional
@RequestScoped
public class OrganisateurGestion {
    @PersistenceContext
    EntityManager em;

    /**
     * Recherche d'un {@link Organisateur} avec son pseudo.
     */
    public Organisateur rechercherOrganisateur(String pseudo) {
        try {
            Query emQuery = em.createQuery("SELECT o FROM Organisateur o WHERE o.pseudo = :pseudo", Organisateur.class);
            emQuery.setParameter("pseudo", pseudo);
            return (Organisateur) emQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Retourne tous les {@link Organisateur}.
     */
    public ArrayList<Organisateur> tousOrganisateurs() {
        Query emQuery = em.createQuery("SELECT o FROM Organisateur o", Organisateur.class);
        return (ArrayList<Organisateur>) emQuery.getResultList();
    }
    /**
     * Inscription d'un {@link Organisateur} dans la BDD du serveur d'Authentification et également dans la table des {@link Organisateur}.
     */
    public void inscrireOrganisateur(String pseudo, String mail, String mdp) {
        em.persist(new Organisateur(pseudo, mail));
        Compte.add(pseudo, mdp, "organisateur");
    }

}
