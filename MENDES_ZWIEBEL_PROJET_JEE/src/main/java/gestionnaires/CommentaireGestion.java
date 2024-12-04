package gestionnaires;

import classes.Administrateur;
import classes.CTF;
import classes.Commentaire;
import classes.Participant;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

import java.util.ArrayList;

/**
 * Opérations faites en rapport avec les {@link Commentaire} sur la BDD.
 * RequestScoped, on fait une opération lors d'une requête, on aura pas besoin de plus d'intéractions.
 */
@Transactional
@RequestScoped
public class CommentaireGestion {

    @PersistenceContext
    EntityManager em;

    @Inject
    CTFGestion ctfGestionnaire;

    @Inject
    ParticipantGestion participantGestionnaire;

    /**
     * Ajout d'un {@link Commentaire} rédigé par un {@link Participant} au sujet d'un {@link CTF}.
     */
    public void creerCommentaire(String commentaire, String titre, String pseudo) {
        Commentaire monCom = new Commentaire(commentaire);

        CTF monCTF = ctfGestionnaire.rechercherCTF(titre);
        Participant monParticipant = participantGestionnaire.rechercherParticipant(pseudo);
        monCom.setParticipant(monParticipant);
        monCom.setCtf(monCTF);

        em.persist(monCom);
    }

    /**
     * Liste de tous les {@link Commentaire} d'un {@link CTF}.
     */
    public ArrayList<Commentaire> consulterCommentairesCTF(String titre) {
        Query emQuery = em.createQuery("SELECT c FROM Commentaire c JOIN c.ctf WHERE c.ctf.titre = :titre", Commentaire.class);
        emQuery.setParameter("titre", titre);
        return (ArrayList<Commentaire>) emQuery.getResultList();
    }

}
