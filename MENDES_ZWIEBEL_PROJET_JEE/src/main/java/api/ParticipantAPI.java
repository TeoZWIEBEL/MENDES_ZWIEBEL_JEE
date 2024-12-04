package api;

import classes.Participant;
import gestionnaires.DemandeGestion;
import gestionnaires.ParticipantGestion;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.ArrayList;

/**
 * API en lien avec les {@link Participant}, utilise {@link ParticipantGestion} et {@link DemandeGestion}.
 */
@Path("/participant")
public class ParticipantAPI {
    @Inject
    ParticipantGestion participantGestionnaire;
    @Inject
    DemandeGestion demandeGestionnaire;

    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response tousParticipant() {
        ArrayList<Participant> participants = participantGestionnaire.tousParticipants();
        return Response.ok(participants, MediaType.APPLICATION_JSON).build();
    }

    @Path("/{pseudo}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response consulterParticipant(@PathParam("pseudo") String pseudo) {
        return Response.ok(participantGestionnaire.rechercherParticipant(pseudo), MediaType.APPLICATION_JSON).build();
    }


    @Path("/demande/{nom}")
    @POST
    @RolesAllowed("participant")
    public Response demandeRejoindre(@PathParam("nom") String equipe, @Context SecurityContext securityContext) {
        demandeGestionnaire.creerDemande(equipe, securityContext.getUserPrincipal().getName());
        return Response.ok("Demande créee avec succès").build();
    }

    @Path("/equit")
    @POST
    @RolesAllowed("participant")
    public Response quitterEquipe(@Context SecurityContext securityContext) {
        participantGestionnaire.quitterEquipe(securityContext.getUserPrincipal().getName());
        return Response.ok("Equipe quittée avec succès").build();
    }

    @Path("/checkChef")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed("participant")
    public Response checkChef(@Context SecurityContext securityContext) {
        return Response.ok(participantGestionnaire.checkChef(securityContext.getUserPrincipal().getName())).build();
    }

}

