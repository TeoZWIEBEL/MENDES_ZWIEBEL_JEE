package demarrage;

import authentification.Compte;
import classes.Defis;
import gestionnaires.*;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import io.quarkus.runtime.StartupEvent;


@Singleton
public class Startup {
    @Inject
    ParticipantGestion pg;

    @Inject
    OrganisateurGestion og;

    @Inject
    AdminGestion ag;

    @Inject
    EquipeGestion eg;

    @Inject
    CTFGestion cg;

    @Inject
    CommentaireGestion comg;

    @Inject
    DiscussionGestion dg;

    @Inject
    DefiGestion defig;

    @Inject
    DemandeGestion demandeg;

    @Transactional
    public void chargerBase(@Observes StartupEvent evt){
        Compte.deleteAll();
        /*
        Ajout dans la base de Participants, D'Organisateurs et d'un Administrateur
        */
        pg.inscrireParticipant("Anakin", "anakin@mail.com", "anakin");
        pg.inscrireParticipant("Padme", "padme@mail.com", "padme");
        pg.inscrireParticipant("Yoda", "yoda@mail.com", "yoda");
        pg.inscrireParticipant("Obiwan", "obiwan@mail.com", "obiwan");
        pg.inscrireParticipant("Mando", "mando@mail.com", "mando");
        pg.inscrireParticipant("Luke", "luke@mail.com", "luke");

        for (int j = 0; j < 10; j++) {
            og.inscrireOrganisateur("Organisateur" + j, "OrganisateurNumero" + j + "@organisation.com", "orgamdp" + j);
        }
        ag.inscrireAdministrateur("admin", "admin@admin.com", "admin");

        eg.creerEquipe("UneBonneEquipe", "Les meilleurs", "Anakin");
        eg.creerEquipe("UneMauvaiseEquipe", "On est moins bons", "Luke");

        demandeg.creerDemande("UneBonneEquipe", "Padme");
        demandeg.creerDemande("UneBonneEquipe", "Obiwan");
        demandeg.creerDemande("UneBonneEquipe", "Yoda");

        demandeg.accepterDemande("Anakin", "Padme");
        demandeg.accepterDemande("Anakin", "Obiwan");

        cg.creerCTF("MonSuperCTF", "Le Ctf à ne pas manquer !", "www.easyctf/superctf.org", "Organisateur0");
        cg.creerCTF("MonCTFPasMauvais", "Seul les meilleurs réussiront", "www.outofthebox.com", "Organisateur4");
        cg.creerCTF("LeCTF", "le seul et l'unique", "www.matrix.fr", "Organisateur7");

        cg.valideCTF("MonSuperCTF");

        comg.creerCommentaire("Des gens ont une expérience avec ce site ?", "MonSuperCTF", "Anakin");

        dg.initierDiscussion("Anakin", "admin");
        dg.ajouterMessage("admin", "Anakin", "Bonjour, des accusations de triche avec un organisateur ont été formulées, merci de me répondre au plus vite.");

        defig.creerDefi("EscapeTheBox", 10, Defis.Categorie.FUN);
        defig.creerDefi("HardMode", 2, Defis.Categorie.DIFFICILE);
        defig.creerDefi("TipsAndTricks", 5, Defis.Categorie.NORMAL);
        defig.creerDefi("LaunchTheRocket", 4, Defis.Categorie.FUN);
        defig.creerDefi("JediOuSith", 2, Defis.Categorie.FACILE);

    }
}


