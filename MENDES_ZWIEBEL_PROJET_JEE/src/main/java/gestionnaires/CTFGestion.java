package gestionnaires;

import classes.*;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import java.util.*;

/**
 * Opérations faites en rapport avec les {@link CTF} sur la BDD.
 * RequestScoped, on fait une opération lors d'une requête, on aura pas besoin de plus d'intéractions.
 */

@Transactional
@RequestScoped
public class CTFGestion {

    @PersistenceContext
    EntityManager em;

    @Inject
    ParticipantGestion pg;

    /**
     * Recherche d'un {@link CTF} en base. Utilisé pour l'API, pour ajouter une vue au CTF consulté.
     */
    public CTF rechercherCTFAPI(String titre) {
        try {
            Query emQuery = em.createQuery("SELECT c FROM CTF c where c.titre = :titre");
            emQuery.setParameter("titre", titre);
            CTF monCTF = (CTF) emQuery.getSingleResult();
            monCTF.addVues();
            return monCTF;
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Tous les {@link CTF} de la base.
     */
    public ArrayList<CTF> tousCTF() {
        Query emQuery = em.createQuery("SELECT c FROM CTF c", CTF.class);
        return (ArrayList<CTF>) emQuery.getResultList();
    }

    // On remarque ici que plutôt que de faire comme pour les Catégories de Défis, on utilise directement la traduction en entier en base. Les deux sont valides.

    /**
     * Tous les {@link CTF} validés de la base.
     */
    public ArrayList<CTF> CTFvalides() {
        Query emQuery = em.createQuery("SELECT c FROM CTF c WHERE c.etat=1", CTF.class);
        return (ArrayList<CTF>) emQuery.getResultList();
    }

    /**
     * Tous les {@link CTF} en attente de la base.
     */
    public ArrayList<CTF> CTFattente() {
        Query emQuery = em.createQuery("SELECT c FROM CTF c WHERE c.etat=0", CTF.class);
        return (ArrayList<CTF>) emQuery.getResultList();
    }

    /**
     * Tous les {@link CTF} terminés de la base.
     */
    public ArrayList<CTF> CTFtermines() {
        Query emQuery = em.createQuery("SELECT c FROM CTF c WHERE c.etat=3", CTF.class);
        return (ArrayList<CTF>) emQuery.getResultList();
    }

    /**
     * Tous les {@link CTF} refusés de la base retournés dans une {@link java.util.ArrayList}
     */
    public ArrayList<CTF> CTFrefuses() {
        Query emQuery = em.createQuery("SELECT c FROM CTF c WHERE c.etat=2", CTF.class);
        return (ArrayList<CTF>) emQuery.getResultList();
    }

    /**
     * Recherche d'un {@link CTF} en base. Utilisé pour faciliter les opérations Backend.
     */
    public CTF rechercherCTF(String titre) {
        try {
            Query emQuery = em.createQuery("SELECT c FROM CTF c where c.titre = :titre");
            emQuery.setParameter("titre", titre);
            return (CTF) emQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Liste de tous les {@link CTF} crées par un {@link classes.Organisateur}.
     */
    public CTF rechercherCTFOrga(String titre, String pseudo) {
        try {
            Query emQuery = em.createQuery("SELECT c FROM CTF c where c.titre = :titre and c.contact.pseudo = :pseudo");
            emQuery.setParameter("titre", titre);
            emQuery.setParameter("pseudo", pseudo);
            return (CTF) emQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Liste de tous les {@link CTF} validés et crées par un {@link classes.Organisateur}.
     */
    public ArrayList<CTF> rechercherCTFOrgaValides(String pseudo) {
        try {
            Query emQuery = em.createQuery("SELECT c FROM CTF c where c.contact.pseudo = :pseudo and c.etat = 1");
            emQuery.setParameter("pseudo", pseudo);
            return (ArrayList<CTF>) emQuery.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Liste de tous les {@link CTF} en attente et crées par un {@link classes.Organisateur}.
     */
    public ArrayList<CTF> rechercherCTFOrgaAttentes(String pseudo) {
        Query emQuery = em.createQuery("SELECT c FROM CTF c where c.contact.pseudo = :pseudo and c.etat = 0");
        emQuery.setParameter("pseudo", pseudo);
        return (ArrayList<CTF>) emQuery.getResultList();

    }

    /**
     * Création d'un {@link CTF}, par un {@link Organisateur}.
     */
    public void creerCTF(String titre, String description, String lieu, String pseudo) {
        Query emQuery = em.createQuery("select o from Organisateur o WHERE pseudo = :pseudo");
        emQuery.setParameter("pseudo", pseudo);
        Organisateur organisateur = (Organisateur) emQuery.getSingleResult();

        em.persist(new CTF(titre, description, lieu, organisateur));
    }

    /**
     * Suppression d'un {@link CTF}, par un {@link Organisateur}.
     */
    public void supprimerCTF(String titre, String pseudo) throws EntityNotFoundException {
        CTF monCTF = rechercherCTFOrga(titre, pseudo);
        if (monCTF != null) {
            Query emQuery = em.createQuery("DELETE FROM Commentaire c WHERE c.ctf.titre = :titre");
            emQuery.setParameter("titre", titre);
            emQuery.executeUpdate();

            emQuery = em.createQuery("DELETE FROM ParticipantCTF pc WHERE pc.ctf.titre = :titre");
            emQuery.setParameter("titre", titre);
            emQuery.executeUpdate();

            emQuery = em.createQuery("DELETE FROM CTF c WHERE c.titre = :titre");
            emQuery.setParameter("titre", titre);
            emQuery.executeUpdate();

        } else {
            throw new EntityNotFoundException("Le ctf n'est pas celui de l'organisateur donné ou il n'existe pas.");
        }
    }

    /**
     * Modification d'un {@link CTF}, par un {@link Organisateur}.
     */
    public void modifierCTF(String titreCourant, String titre, String description, String lieu, String pseudo) throws EntityNotFoundException{
        CTF monCTF = rechercherCTFOrga(titreCourant, pseudo);
        if (monCTF != null) {
            monCTF.setTitre(titre);
            monCTF.setDescription(description);
            monCTF.setLieu(lieu);
            em.persist(monCTF);
        } else {
            throw new EntityNotFoundException("Le ctf n'est pas celui de l'organisateur donné où il n'éxiste pas.");
        }
    }

    /**
     * Inscription d'un {@link Participant} à un {@link CTF}.
     */
    public void ajoutParticipantCTF(String pseudo, String titre) throws EntityExistsException{
        Participant monParticipant = pg.rechercherParticipant(pseudo);
        CTF monCTF = rechercherCTF(titre);

        List<ParticipantCTF> mesParticipantCTF = monCTF.getParticipants();
        for (ParticipantCTF participantCTF : mesParticipantCTF) {
            if (Objects.equals(participantCTF.getParticipant().getPseudo(), monParticipant.getPseudo())) {
                throw new EntityExistsException("Le participant est déjà inscrit à ce CTF.");
            }
        }
        monCTF.addParticipant(new ParticipantCTF(monCTF, monParticipant, 0));
        em.persist(monCTF);

    }

    /**
     * Desinscription d'un {@link Participant} à un {@link CTF}.
     */
    public void suppressionParticipantCTF(String pseudo, String titre) {
        Query emQuery = em.createQuery("DELETE FROM ParticipantCTF pc WHERE pc.ctf.titre=:titre AND pc.participant.pseudo=:pseudo");
        emQuery.setParameter("titre", titre);
        emQuery.setParameter("pseudo", pseudo);
        emQuery.executeUpdate();
    }

    /**
     * Map de tous les pseudos des {@link Participant} et de leur score à un {@link CTF}.
     * On ne renvoie que les pseudos, on a pas besoin de plus ici.
     * Plus tard on privilegera cette approche avec des DTO.
     */
    public Map<String, Integer> participantsCTF(String titre) {
        Query emQuery = em.createQuery("SELECT pc FROM ParticipantCTF pc WHERE pc.ctf.titre = :titre", ParticipantCTF.class);
        emQuery.setParameter("titre", titre);
        ArrayList<ParticipantCTF> mesParticipantsCtf = (ArrayList<ParticipantCTF>) emQuery.getResultList();
        Map<String, Integer> maMap = new HashMap<>();
        for(int i = 0; i < mesParticipantsCtf.size(); i++){
            maMap.put(mesParticipantsCtf.get(i).getParticipant().getPseudo(), mesParticipantsCtf.get(i).getScore());
        }
        return maMap;
    }

    /**
     * Saisie des scores, mise à jour de la table {@link ParticipantCTF}.
     * {@link CTF} pour lequel les scores sont saisis passe en état terminé.
     */
    public void saisieScoreCTF(Map<String, Integer> participantsScores, String titre, String pseudoOrga){
        ArrayList<String> cles = new ArrayList<>(participantsScores.keySet());

        for(int i = 0; i<participantsScores.size(); i++){
            Query emQuery = em.createQuery("SELECT pc FROM ParticipantCTF pc WHERE pc.participant.pseudo = :pseudo AND pc.ctf.titre = :titre AND pc.ctf.contact.pseudo = :pseudoOrga", ParticipantCTF.class);
            emQuery.setParameter("pseudo", cles.get(i));
            emQuery.setParameter("titre", titre);
            emQuery.setParameter("pseudoOrga", pseudoOrga);
            ParticipantCTF monParticipantCTF = (ParticipantCTF) emQuery.getSingleResult();

            monParticipantCTF.setScore(participantsScores.get(cles.get(i)));
            monParticipantCTF.getParticipant().addScore(participantsScores.get(cles.get(i)));
            em.persist(monParticipantCTF);
        }

        CTF monCTF = rechercherCTFOrga(titre, pseudoOrga);
        monCTF.terminerCTF();
        em.persist(monCTF);
    }

    /**
     * Modification d'un {@link classes.CTF} en base, changement de l'état en VALIDE.
     * On pourra parfois privilegier cette approche, on ne passe pas par la recupération du CTF
     * pour ensuite le modifier et le persist.
     */
    public int valideCTF(String titre) {
        Query emQuery = em.createQuery("update CTF c set c.etat=1 where titre=:titre");
        emQuery.setParameter("titre", titre);
        return emQuery.executeUpdate();
    }
    /**
     * Modification d'un {@link classes.CTF} en base, changement de l'état en REFUSE.
     * On pourra parfois privilegier cette approche, on ne passe pas par la recupération du CTF
     * pour ensuite le modifier et le persist.
     */
    public int refuseCTF(String titre) {
        Query emQuery = em.createQuery("update CTF c set c.etat=2 where titre=:titre");
        emQuery.setParameter("titre", titre);
        return emQuery.executeUpdate();
    }

}
