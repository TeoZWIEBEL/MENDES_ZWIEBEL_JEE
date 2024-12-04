package authentification;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.UniqueConstraint;

/**
 * Table/classe spéciale issue de la documentation de Quarkus sur la sécurité
 * à l'aide de la persistence. <a href="https://quarkus.io/guides/security-jpa">QUARKUS SECURITY WITH JAKARTA PERSISTENCE</a>
 */
@Entity
@Table(name = "compte", uniqueConstraints = @UniqueConstraint(columnNames = "pseudo"))
@UserDefinition
public class Compte extends PanacheEntity {

    @Username
    public String pseudo;
    @Password
    @JsonbTransient
    public String mdp;
    @Roles
    public String role;

    /**
     * Adds a new user in the database
     *
     * @param username the user name
     * @param password the unencrypted password (it will be encrypted with bcrypt)
     * @param role     the comma-separated roles
     */
    public static void add(String username, String password, String role) {
        Compte compte = new Compte();
        compte.pseudo = username;
        compte.mdp = BcryptUtil.bcryptHash(password);
        compte.role = role;
        compte.persist();
    }
}
