package api;

import authentification.Compte;
import io.quarkus.security.identity.CurrentIdentityAssociation;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Instant;
import java.util.Date;

/**
 * API en lien avec l'authentification de l'{@link Compte}.
 */
@Path("/auth")
public class AuthentificationAPI {

    @ConfigProperty(name = "quarkus.http.auth.form.cookie-name")
    String cookieName;

    @Inject
    CurrentIdentityAssociation identity;

    /**
     * Renvoie un texte contenant le pseudo de l'utilisateur connecté.
     */
    @Path("/utilisateur")
    @GET
    public Response utilisateurCourant(@Context SecurityContext securityContext) {
        if (identity.getIdentity().isAnonymous())
            return Response.status(Response.Status.UNAUTHORIZED).entity("Non connecté").build();
        return Response.ok(securityContext.getUserPrincipal().getName(), MediaType.TEXT_PLAIN).build();
    }

    /**
     * Renvoie un texte contenant le role de l'utilisateur connecté.
     */
    @Path("/role")
    @GET
    public Response roleCourant(@Context SecurityContext securityContext) {
        if (identity.getIdentity().isAnonymous())
            return Response.status(Response.Status.UNAUTHORIZED).entity("Non connecté").build();
        return Response.ok(identity.getIdentity().getRoles(), MediaType.TEXT_PLAIN).build();
    }

    /**
     * Deconnexion de l'utilisateur depuis le serveur. On renvoie un cookie expiré.
     * On peut également se deconnecter depuis le front en supprimant le cookie tout simplement.
     * Exemple issu de <a href="https://quarkus.io/guides/security-authentication-mechanisms#form-auth">AUTHENTICATION MECHANISMS IN QUARKUS</a>.
     */
    @Path("/deconnexion")
    @POST
    @RolesAllowed({"participant", "admin", "organisateur"})
    public Response deconnexion() {
        final NewCookie removeCookie = new NewCookie.Builder(cookieName)
                .maxAge(0)
                .expiry(Date.from(Instant.EPOCH))
                .path("/")
                .build();
        return Response.noContent().cookie(removeCookie).build();
    }


}
