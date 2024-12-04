package gestionnaires;

import classes.Demande;
import classes.Equipe;
import classes.Participant;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

import java.util.ArrayList;

/**
 * Opérations faites en rapport avec les {@link Demande} sur la BDD.
 * RequestScoped, on fait une opération lors d'une requête, on aura pas besoin de plus d'intéractions.
 */
@Transactional
@RequestScoped
public class DemandeGestion {

    @PersistenceContext
    EntityManager em;

    @Inject
    EquipeGestion eg;

    @Inject
    ParticipantGestion pg;

    /**
     * Ajout d'un {@link Demande} dans la BDD.
     */
    public void creerDemande(String nom, String pseudo) {
        Participant demandeur = pg.rechercherParticipant(pseudo);
        Equipe demandee = eg.rechercherEquipe(nom);
        if (demandeur.getEquipe() != null)
            throw new EntityExistsException("Le demandeur est déjà membre d'une équipe.");
        else
            em.persist(new Demande(demandee, demandeur));

    }



    /**
     * Acceptation d'une {@link Demande} d'un {@link classes.Participant} par un {@link classes.Participant} chef de l'{@link Equipe}
     * On ne se préoccupe pas de supprimer toutes les demandes existantes car un participant ne peut
     * formuler qu'une demande à la fois.
     */
    public void accepterDemande(String pseudoChef, String pseudoDemandeur) {
        Equipe monEquipe = eg.rechercherEquipeChef(pseudoChef);

        Query emQuery = em.createQuery("SELECT d FROM Demande d LEFT JOIN FETCH d.participant WHERE d.participant.pseudo = :pseudoDemandeur AND d.equipe.nom=:nomEquipe", Demande.class);
        emQuery.setParameter("pseudoDemandeur", pseudoDemandeur);
        emQuery.setParameter("nomEquipe", monEquipe.getNom());

        Demande maDemande = (Demande) emQuery.getSingleResult();

        maDemande.getParticipant().setEquipe(monEquipe);

        em.remove(maDemande);
    }

    /**
     * Refus d'une {@link Demande} d'un {@link classes.Participant} par un {@link classes.Participant} chef de l'{@link Equipe}
     */
    public void refuserDemande(String pseudoChef, String pseudoDemandeur) {
        Equipe monEquipe = eg.rechercherEquipeChef(pseudoChef);

        Query emQuery = em.createQuery("SELECT d FROM Demande d LEFT JOIN FETCH d.participant WHERE d.participant.pseudo = :pseudoDemandeur AND d.equipe.nom=:nomEquipe", Demande.class);
        emQuery.setParameter("pseudoDemandeur", pseudoDemandeur);
        emQuery.setParameter("nomEquipe", monEquipe.getNom());

        Demande maDemande = (Demande) emQuery.getSingleResult();

        em.remove(maDemande);
    }

    /**
     * On recupère toutes les {@link Demande} liées a une {@link Equipe}.
     * On utilise le chef d'équipe pour identifier cette dernière car on
     * utilisera l'utilisateur connecté. Celà permet d'éviter a quiconque
     * utilisant l'API de voir les demandes des autres équipes.
     */
    public ArrayList<Demande> demandesEquipe(String pseudoChef) {
        Query emQuery = em.createQuery("SELECT d FROM Demande d WHERE d.equipe.chef.pseudo=:pseudoChef");
        emQuery.setParameter("pseudoChef", pseudoChef);
        return (ArrayList<Demande>) emQuery.getResultList();
    }
}
