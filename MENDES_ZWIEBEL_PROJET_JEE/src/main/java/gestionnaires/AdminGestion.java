package gestionnaires;

import authentification.Compte;
import classes.Administrateur;
import classes.Utilisateur;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
/**
 * Opérations faites en rapport avec les {@link Administrateur} sur la BDD.
 * RequestScoped, on fait une opération lors d'une requête, on aura pas besoin de plus d'intéractions.
 */
@Transactional
@RequestScoped
public class AdminGestion {

    @PersistenceContext
    EntityManager em;

    /**
     * Inscription d'un {@link Administrateur} dans la BDD du serveur d'Authentification et également dans la table des {@link Administrateur}.
     */
    public void inscrireAdministrateur(String pseudo, String mail, String mdp) {
        em.persist(new Administrateur(pseudo, mail));
        Compte.add(pseudo, mdp, "admin");
    }

    /**
     * Action sur la BDD du serveur d'Authentification, l'{@link Utilisateur} aura le rôle "banni".
     * Il n'aura accès qu'aux routes de l'api publiques.
     * On ne supprime pas le compte pour empecher l'utilisateur de recréer plusieurs fois son compte.
     */
    public int banUtilisateur(String pseudo) {
        EntityManager uem = Compte.getEntityManager();
        Query emQuery = uem.createQuery("UPDATE Compte set role=:banni where pseudo=:pseudo");
        emQuery.setParameter("banni", "banni");
        emQuery.setParameter("pseudo", pseudo);
        return emQuery.executeUpdate();
    }

}
