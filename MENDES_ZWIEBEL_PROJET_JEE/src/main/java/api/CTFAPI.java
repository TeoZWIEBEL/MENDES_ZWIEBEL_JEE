package api;

import classes.CTF;
import gestionnaires.CTFGestion;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.ArrayList;
import java.util.Map;

/**
 * API en lien avec les {@link CTF}, utilise {@link CTFGestion}.
 */
@Path("/ctf")
public class CTFAPI {
    @Inject
    CTFGestion ctfGestionnaire;

    @Path("/valides")
    @GET
    @PermitAll
    public Response validesCTF() {
        ArrayList<CTF> mesCTFs = ctfGestionnaire.CTFvalides();
        return Response.ok(mesCTFs, MediaType.APPLICATION_JSON).build();

    }

    @Path("/organisateur/valides")
    @GET
    @RolesAllowed("organisateur")
    public Response mesCtfsvalides(@Context SecurityContext securityContext) {
        ArrayList<CTF> mesCTFs = ctfGestionnaire.rechercherCTFOrgaValides(securityContext.getUserPrincipal().getName());
        return Response.ok(mesCTFs, MediaType.APPLICATION_JSON).build();

    }

    @Path("/organisateur/attentes")
    @GET
    @RolesAllowed("organisateur")
    public Response mesCTFsEnAttente(@Context SecurityContext securityContext) {
        ArrayList<CTF> mesCTFs = ctfGestionnaire.rechercherCTFOrgaAttentes(securityContext.getUserPrincipal().getName());
        return Response.ok(mesCTFs, MediaType.APPLICATION_JSON).build();
    }

    @Path("/attentes")
    @GET
    @RolesAllowed("admin")
    public Response attentesCTF() {
        ArrayList<CTF> mesCTFs = ctfGestionnaire.CTFattente();
        return Response.ok(mesCTFs, MediaType.APPLICATION_JSON).build();
    }

    @Path("/refuses")
    @GET
    @RolesAllowed("admin")
    public Response refusesCTF() {
        ArrayList<CTF> mesCTFs = ctfGestionnaire.CTFrefuses();
        return Response.ok(mesCTFs, MediaType.APPLICATION_JSON).build();
    }

    @Path("/termines")
    @GET
    @PermitAll
    public Response terminesCTF() {
        ArrayList<CTF> mesCTFs = ctfGestionnaire.CTFtermines();
        return Response.ok(mesCTFs, MediaType.APPLICATION_JSON).build();
    }


    @Path("/{titre}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response consulterCTF(@PathParam("titre") String titre) {
        return Response.ok(ctfGestionnaire.rechercherCTFAPI(titre), MediaType.APPLICATION_JSON).build();
    }

    @Path("/{titre}/participants")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response consulterParticipantsCTF(@PathParam("titre") String titre) {
        return Response.ok(ctfGestionnaire.participantsCTF(titre), MediaType.APPLICATION_JSON).build();
    }

    @Path("/creer")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @RolesAllowed("organisateur")
    public Response creerCTF(@FormParam("titre") String titre, @FormParam("description") String description, @FormParam("lieu") String lieu, @Context SecurityContext securityContext) {
        if (titre != "" && titre != null) {
            ctfGestionnaire.creerCTF(titre, description, lieu, securityContext.getUserPrincipal().getName());
            return Response.ok("CTF crée avec succès, validation par un administrateur en attente.").build();
        } else {
            return Response.status(Response.Status.CONFLICT).entity("Le titre ne peut pas être nul.").build();
        }
    }

    @Path("/{titre}/supprimer")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("organisateur")
    public Response supprimerCTF(@PathParam("titre") String titre, @Context SecurityContext securityContext) {
        try {
            ctfGestionnaire.supprimerCTF(titre, securityContext.getUserPrincipal().getName());
            return Response.ok("CTF supprimé avec succès").build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.CONFLICT).entity("Le CTF n'existe pas ou n'est pas de l'organisateur connecté.").build();
        }
    }

    @Path("/{titre}/modifierCTF")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @RolesAllowed("organisateur")
    public Response modifierCTF(@PathParam("titre") String titreCourant, @FormParam("titre") String titre, @FormParam("description") String description, @FormParam("lieu") String lieu, @Context SecurityContext securityContext) {
        if (titre != "" && titre != null) {
            try {
                ctfGestionnaire.modifierCTF(titreCourant, titre, description, lieu, securityContext.getUserPrincipal().getName());
                return Response.ok("CTF modifié avec succès").build();
            } catch (EntityNotFoundException e) {
                return Response.status(Response.Status.CONFLICT).entity("Le CTF n'existe pas ou n'est pas de l'organisateur connecté.").build();
            }
        } else {
            return Response.status(Response.Status.CONFLICT).entity("Le titre ne peut pas être nul.").build();

        }

    }

    @Path("/inscrire/{titre}")
    @POST
    @RolesAllowed("participant")
    public Response inscrireCTF(@PathParam("titre") String titre, @Context SecurityContext securityContext) {
        try {
            ctfGestionnaire.ajoutParticipantCTF(securityContext.getUserPrincipal().getName(), titre);
            return Response.ok("Inscription réalisée avec succès").build();
        } catch (EntityExistsException e) {
            // Le participant est déjà inscrit, renvoyer une réponse conflict
            return Response.status(Response.Status.CONFLICT).entity("Participant déjà inscrit à ce CTF.").build();
        }
    }

    @Path("/desinscrire/{titre}")
    @POST
    @RolesAllowed("participant")
    public Response desinscrireCTF(@Context SecurityContext securityContext, @PathParam("titre") String titre) {
        ctfGestionnaire.suppressionParticipantCTF(securityContext.getUserPrincipal().getName(), titre);
        return Response.ok("Desinscription realisée avec succès.").build();
    }

    @Path("/{titre}/saisirScore")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("organisateur")
    public Response saisieScores(Map<String, Integer> participantsScores, @PathParam("titre") String titre, @Context SecurityContext securityContext) {
        ctfGestionnaire.saisieScoreCTF(participantsScores, titre, securityContext.getUserPrincipal().getName());
        return Response.ok("Saisie réalisée avec succès").build();
    }

    @Path("/valider/{titre}")
    @POST
    @RolesAllowed("admin")
    public Response validerCTF(@PathParam("titre") String titre) {
        int valide = ctfGestionnaire.valideCTF(titre);
        if (valide != 0)
            return Response.ok("CTF validé avec succès").build();
        else
            return Response.status(Response.Status.CONFLICT).entity("CTF déjà validé ou inexistant.").build();

    }

    @Path("/refuser/{titre}")
    @POST
    @RolesAllowed("admin")
    public Response refuserCTF(@PathParam("titre") String titre) {
        int refuse = ctfGestionnaire.refuseCTF(titre);
        if (refuse != 0)
            return Response.ok("CTF refusé avec succès").build();
        else
            return Response.status(Response.Status.CONFLICT).entity("CTF déjà refusé ou inexistant.").build();

    }


}
