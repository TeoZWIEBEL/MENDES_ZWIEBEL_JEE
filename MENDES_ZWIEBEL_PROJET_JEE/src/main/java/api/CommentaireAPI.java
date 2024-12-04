package api;

import gestionnaires.CommentaireGestion;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

/**
 * API en lien avec les {@link classes.Commentaire}, utilise {@link CommentaireGestion}.
 */
@Path("/commentaire")
public class CommentaireAPI {

    @Inject
    CommentaireGestion commentaireGestionnaire;

    @Path("/poster/{titre}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @RolesAllowed({"participant", "organisateur"})
    public Response posterCommentaire(@PathParam("titre") String titre, @FormParam("commentaire") String commentaire, @Context SecurityContext securityContext) {
        commentaireGestionnaire.creerCommentaire(commentaire, titre, securityContext.getUserPrincipal().getName());
        return Response.ok("Commentaire posté avec succès").build();
    }

    @Path("/{titre}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response consulterCommentairesCTF(@PathParam("titre") String titre) {
        return Response.ok(commentaireGestionnaire.consulterCommentairesCTF(titre), MediaType.APPLICATION_JSON).build();
    }
}
