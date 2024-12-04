package api;

import authentification.Compte;
import gestionnaires.AdminGestion;
import gestionnaires.OrganisateurGestion;
import gestionnaires.ParticipantGestion;
import io.quarkus.security.identity.CurrentIdentityAssociation;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * API de gestion de comptes en lien avec les {@link Compte} et les classes {@link classes.Utilisateur}, utilise {@link ParticipantGestion}, {@link OrganisateurGestion} et {@link AdminGestion}.
 */
@Path("/compte")
public class CompteAPI {
    @Inject
    CurrentIdentityAssociation identity;
    @Inject
    ParticipantGestion participantGestionnaire;
    @Inject
    OrganisateurGestion organisateurGestionnaire;
    @Inject
    AdminGestion administrateurGestionnaire;

    @Path("/ban/{pseudo}")
    @POST
    @RolesAllowed("admin")
    public Response bannirUtilisateur(@PathParam("pseudo") String pseudo) {
        int banni = administrateurGestionnaire.banUtilisateur(pseudo);
        // Le gestionnaire retourne ce que retourne son executeUpdate(), cette fonction retourne le nombre de lignes modifiées.
        // Si on a modifié une ligne, alors l'utilisateur à bien été banni.
        // Sinon on renvoie une réponse erreur.
        if (banni != 0)
            return Response.ok("Utilisateur banni avec succès").build();
        else
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Utilisateur déjà banni ou inexistant").build();
    }

    @Path("/creerparticipant")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response creerCompte(@FormParam("pseudo") String pseudo, @FormParam("mdp") String mdp, @FormParam("mail") String mail) {
        if (!identity.getIdentity().isAnonymous()) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Vous êtes déjà connecté").build();
        }
        participantGestionnaire.inscrireParticipant(pseudo, mail, mdp);
        return Response.ok("Inscription réussie").build();
    }

    @Path("/creerorganisateur")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response inscription(@FormParam("pseudo") String pseudo, @FormParam("mdp") String mdp, @FormParam("mail") String mail, @FormParam("type") String type) {
        if (!identity.getIdentity().isAnonymous()) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Vous êtes déjà connecté").build();
        }
        organisateurGestionnaire.inscrireOrganisateur(pseudo, mail, mdp);
        return Response.ok("Inscription réussie").build();
    }
}
