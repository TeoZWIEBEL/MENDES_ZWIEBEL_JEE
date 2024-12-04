package gestionnaires;

import authentification.Compte;
import classes.Defis;
import classes.Discussion;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import java.util.ArrayList;

/**
 * Opérations faites en rapport avec les {@link Defis} sur la BDD.
 * RequestScoped, on fait une opération lors d'une requête, on aura pas besoin de plus d'intéractions.
 */
@Transactional
@RequestScoped
public class DiscussionGestion {

    @PersistenceContext
    EntityManager em;

    /**
     * Ajout d'une nouvelle {@link Discussion} entre deux {@link Compte}.
     * Idéalement, nous aurions voulu un serveur de discussion séparé, q
     * qui n'intéragi qu'avec la Table de discussion et le serveur d'authentification.
     */
    public void initierDiscussion(String pseudoUtilisateur1, String pseudoUtilisateur2) {
        Discussion discussionCheck = recupererUneDiscussion(pseudoUtilisateur1, pseudoUtilisateur2);

        if (discussionCheck == null) {
            EntityManager uem = Compte.getEntityManager();
            Query uemQuery1 = uem.createQuery("SELECT u FROM Compte u WHERE u.pseudo = :pseudoUtilisateur1", Compte.class);
            uemQuery1.setParameter("pseudoUtilisateur1", pseudoUtilisateur1);
            Compte utilisateur1 = (Compte) uemQuery1.getSingleResult();

            Query uemQuery2 = uem.createQuery("SELECT u FROM Compte u WHERE u.pseudo = :pseudoUtilisateur2", Compte.class);
            uemQuery2.setParameter("pseudoUtilisateur2", pseudoUtilisateur2);
            Compte utilisateur2 = (Compte) uemQuery2.getSingleResult();
            em.persist(new Discussion(utilisateur1, utilisateur2));

        } else {
            throw new EntityExistsException("Une discussion entre les deux utilisateurs existe déjà.");
        }

    }

    /**
     * Récupération d'une {@link Discussion} en l'identifiant par les deux {@link Compte}.
     */
    public Discussion recupererUneDiscussion(String pseudoUtilisateur, String pseudoInterlocuteur) {
        try {
            Query emQuery = em.createQuery("SELECT d FROM Discussion d WHERE (d.utilisateur1.pseudo = :pseudoUtilisateur OR d.utilisateur2.pseudo = :pseudoUtilisateur) AND (d.utilisateur1.pseudo = :pseudoInterlocuteur OR d.utilisateur2.pseudo = :pseudoInterlocuteur)");
            emQuery.setParameter("pseudoUtilisateur", pseudoUtilisateur);
            emQuery.setParameter("pseudoInterlocuteur", pseudoInterlocuteur);
            return (Discussion) emQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    /**
     * Récupération de toutes les {@link Discussion} d'un {@link Compte}.
     */
    public ArrayList<Discussion> mesDiscussions(String pseudoUtilisateur) {
        Query emQuery = em.createQuery("SELECT d FROM Discussion d WHERE d.utilisateur1.pseudo = :pseudo OR d.utilisateur2.pseudo = :pseudo");
        emQuery.setParameter("pseudo", pseudoUtilisateur);
        return (ArrayList<Discussion>) emQuery.getResultList();
    }

    /**
     * Ajout d'un {@link String} qui est un message, dans une {@link Discussion}.
     */
    public void ajouterMessage(String pseudoUtilisateur, String pseudoInterlocuteur, String message) {
        Discussion maDiscussion = recupererUneDiscussion(pseudoUtilisateur, pseudoInterlocuteur);
        maDiscussion.ajouterMessage(pseudoUtilisateur + " : " + message);
        em.persist(maDiscussion);
    }

}
