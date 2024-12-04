package gestionnaires;

import classes.Defis;
import classes.Participant;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import java.util.ArrayList;

/**
 * Opérations faites en rapport avec les {@link Defis} sur la BDD.
 * RequestScoped, on fait une opération lors d'une requête, on aura pas besoin de plus d'intéractions.
 */
@Transactional
@RequestScoped
public class DefiGestion {

    @PersistenceContext
    EntityManager em;

    @Inject
    ParticipantGestion pg;

    /**
     * Ajout d'un {@link Defis} dans la BDD.
     */
    public void creerDefi(String titre, int points, Defis.Categorie categorie) {
        em.persist(new Defis(titre, points, categorie));
    }

    /**
     * Tous les {@link Defis} de la base.
     */
    public ArrayList<Defis> tousDefis() {
        Query emQuery = em.createQuery("SELECT d FROM Defis d");
        return (ArrayList<Defis>) emQuery.getResultList();
    }

    /**
     * Récuperation d'un {@link Defis} en fonction de son titre.
     */
    public Defis recupererDefi(String titre) {
        try {
            Query emQuery = em.createQuery("SELECT d FROM Defis d WHERE d.titre=:titre");
            emQuery.setParameter("titre", titre);
            return (Defis) emQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Tous les {@link Defis} de la {@link Defis.Categorie} précisée de la base.
     */
    public ArrayList<Defis> recupererDefisCate(String categorie) {
        Query emQuery = em.createQuery("SELECT d FROM Defis d WHERE d.categorie=:categorie");
        switch (categorie) {
            // On remarque ici que plutôt que de faire comme pour l'Etat du CTF, on utilise directement l'enum. Les deux sont valides.
            case "FUN":
                emQuery.setParameter("categorie", Defis.Categorie.FUN);
                break;
            case "DIFFICILE":
                emQuery.setParameter("categorie", Defis.Categorie.DIFFICILE);
                break;
            case "NORMAL":
                emQuery.setParameter("categorie", Defis.Categorie.NORMAL);
                break;
            case "FACILE":
                emQuery.setParameter("categorie", Defis.Categorie.FACILE);
                break;
        }
        return (ArrayList<Defis>) emQuery.getResultList();
    }

    /**
     * Complétion d'un {@link Defis} par un {@link Participant}
     */
    public void participantCompleteDefi(String titre, String pseudo) throws EntityExistsException {
        Defis defi = recupererDefi(titre);
        Participant participant = pg.rechercherParticipant(pseudo);

        if (defi.getParticipants().contains(participant)) {
            throw new EntityExistsException("Un participant ne peut pas compléter deux fois un même défi");
        } else {
            participant.addScore(defi.getPoints());
            defi.addParticipant(participant);
            em.persist(participant);
            em.persist(defi);
        }
    }


}
