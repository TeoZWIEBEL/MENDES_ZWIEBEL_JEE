package api;

import classes.Equipe;
import gestionnaires.DemandeGestion;
import gestionnaires.EquipeGestion;
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
 * API en lien avec les {@link Equipe}, utilise {@link EquipeGestion} et {@link DemandeGestion}.
 */
@Path("/equipe")
public class EquipeAPI {

    @Inject
    EquipeGestion equipeGestionnaire;

    @Inject
    DemandeGestion demandeGestionnaire;

    @Path("/creer")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @RolesAllowed("participant")
    public Response creerEquipe(@FormParam("nom") String nom, @FormParam("description") String description, @Context SecurityContext securityContext) {
        equipeGestionnaire.creerEquipe(nom, description, securityContext.getUserPrincipal().getName());
        return Response.ok("Equipe créée avec succès").build();
    }

    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response tousEquipes() {
        ArrayList<Equipe> equipes = equipeGestionnaire.toutesEquipe();
        return Response.ok(equipes, MediaType.APPLICATION_JSON).build();
    }

    @Path("/{nom}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response consulterEquipe(@PathParam("nom") String nom) {
        return Response.ok(equipeGestionnaire.rechercherEquipe(nom), MediaType.APPLICATION_JSON).build();
    }

    @Path("/{nom}/membres")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response consulterMembresEquipe(@PathParam("nom") String nom) {
        return Response.ok(equipeGestionnaire.rechercherMembresEquipe(nom), MediaType.APPLICATION_JSON).build();
    }


    @Path("/demande/{pseudo}/accepter")
    @POST
    @RolesAllowed("participant")
    public Response accepterDemande(@PathParam("pseudo") String pseudo, @Context SecurityContext securityContext) {
        demandeGestionnaire.accepterDemande(securityContext.getUserPrincipal().getName(), pseudo);
        return Response.ok("Demande acceptée avec succès").build();
    }

    @Path("/demande/{pseudo}/refuser")
    @POST
    @RolesAllowed("participant")
    public Response refuserDemande(@PathParam("pseudo") String pseudo, @Context SecurityContext securityContext) {
        demandeGestionnaire.refuserDemande(securityContext.getUserPrincipal().getName(), pseudo);
        return Response.ok("Demande refusée avec succès").build();
    }

    @Path("/inscrire/{titre}")
    @POST
    @RolesAllowed("participant")
    public Response inscrireEquipe(@PathParam("titre") String titre, @Context SecurityContext securityContext) {
        equipeGestionnaire.inscrireEquipe(securityContext.getUserPrincipal().getName(), titre);
        return Response.ok("Equipe inscrite avec succès").build();
    }

    @Path("/demandes")
    @GET
    @RolesAllowed("participant")
    public Response demandesEquipe(@Context SecurityContext securityContext) {
        return Response.ok(demandeGestionnaire.demandesEquipe(securityContext.getUserPrincipal().getName()), MediaType.APPLICATION_JSON).build();
    }

    @Path("/transfererChef/{pseudo}")
    @POST
    @RolesAllowed("participant")
    public Response transfererChef(@Context SecurityContext securityContext, @PathParam("pseudo") String pseudo) {
        equipeGestionnaire.transfererChef(securityContext.getUserPrincipal().getName(), pseudo);
        return Response.ok("Transfère réalisé avec succès.").build();
    }


}
