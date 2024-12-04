package api;

import gestionnaires.DefiGestion;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.persistence.EntityExistsException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

/**
 * API en lien avec les {@link classes.Defis}, utilise {@link DefiGestion}.
 */
@Path("/defis")
public class DefisAPI {
    @Inject
    DefiGestion defiGestionnaire;

    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response tousDefis() {
        return Response.ok(defiGestionnaire.tousDefis(), MediaType.APPLICATION_JSON).build();
    }

    @Path("/{titre}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response recupererDefi(@PathParam("titre") String titre) {
        return Response.ok(defiGestionnaire.recupererDefi(titre), MediaType.APPLICATION_JSON).build();
    }

    @Path("/categorie/{categorie}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response recupererDefiCate(@PathParam("categorie") String categorie) {
        return Response.ok(defiGestionnaire.recupererDefisCate(categorie), MediaType.APPLICATION_JSON).build();
    }


    @Path("/{titre}/completer")
    @POST
    @RolesAllowed("participant")
    public Response completerDefi(@PathParam("titre") String titre, @Context SecurityContext securityContext) {
        try {
            defiGestionnaire.participantCompleteDefi(titre, securityContext.getUserPrincipal().getName());
            return Response.ok("Defi completé").build();
        } catch (EntityExistsException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Défi déjà complété par le participant.").build();
        }
    }

}