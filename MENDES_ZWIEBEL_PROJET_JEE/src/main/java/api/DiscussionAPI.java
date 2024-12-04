package api;

import classes.Discussion;
import gestionnaires.DiscussionGestion;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.ArrayList;

/**
 * API en lien avec les {@link Discussion}, utilise {@link DiscussionGestion}.
 */
@RolesAllowed({"participant", "admin", "organisateur"})
@Path("/discussion")
public class DiscussionAPI {

    @Inject
    DiscussionGestion discussionGestionnaire;

    @Path("/all")
    @GET
    public Response mesDiscussions(@Context SecurityContext securityContext) {
        ArrayList<Discussion> mesDiscussions = discussionGestionnaire.mesDiscussions(securityContext.getUserPrincipal().getName());
        return Response.ok(mesDiscussions, MediaType.APPLICATION_JSON).build();
    }

    @Path("/{pseudo}")
    @GET
    public Response mesDiscussions(@Context SecurityContext securityContext, @PathParam("pseudo") String pseudo) {
        Discussion maDiscussion = discussionGestionnaire.recupererUneDiscussion(securityContext.getUserPrincipal().getName(), pseudo);
        return Response.ok(maDiscussion, MediaType.APPLICATION_JSON).build();
    }


    @Path("/initierDiscussion/{pseudo}")
    @POST
    public Response initierDiscussion(@Context SecurityContext securityContext, @PathParam("pseudo") String pseudo) throws IllegalAccessException {
        if (pseudo != securityContext.getUserPrincipal().getName()) {
            discussionGestionnaire.initierDiscussion(securityContext.getUserPrincipal().getName(), pseudo);
            return Response.ok("Discussion créée avec succès.").build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Vous ne pouvez pas parler a vous même.").build();
        }
    }

    @Path("/{pseudo}/envoyerMessage")
    @POST
    public Response envoyerMessage(@Context SecurityContext securityContext, @PathParam("pseudo") String pseudo, @FormParam("message") String message) {
        discussionGestionnaire.ajouterMessage(securityContext.getUserPrincipal().getName(), pseudo, message);
        return Response.ok("Message envoyé.").build();
    }
}
