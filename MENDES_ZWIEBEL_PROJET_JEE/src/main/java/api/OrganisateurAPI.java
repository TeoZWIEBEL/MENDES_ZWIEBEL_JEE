package api;

import classes.Organisateur;
import gestionnaires.OrganisateurGestion;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;

/**
 * API en lien avec les {@link Organisateur}, utilise {@link OrganisateurGestion}.
 */
@Path("/organisateur")
public class OrganisateurAPI {

    @Inject
    OrganisateurGestion organisateurGestionnaire;

    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response tousOrganisateur() {
        ArrayList<Organisateur> organisateurs = organisateurGestionnaire.tousOrganisateurs();
        return Response.ok(organisateurs, MediaType.APPLICATION_JSON).build();
    }

    @Path("/{pseudo}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response consulterOrganisateur(@PathParam("pseudo") String pseudo) {
        return Response.ok(organisateurGestionnaire.rechercherOrganisateur(pseudo), MediaType.APPLICATION_JSON).build();
    }
}