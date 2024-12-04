const ConnexionComposant = () => {
    const [pseudo, setPseudo] = React.useState("");
    const [mdp, setMdp] = React.useState("");
    const [erreur, setErreur] = React.useState("");
    const [redirigerAccueil, setRedirigerAccueil] = React.useState(false);

    const handlePseudoChange = (event) => {
        setPseudo(event.target.value);
    }

    const handleMdpChange = (event) => {
        setMdp(event.target.value);
    }

    // https://quarkus.io/guides/security-authentication-mechanisms#form-auth
    // On reprend le tutoriel de la documentation de Quarkus pour se connecter 
    // avec form based auth.
    const handleLoginFormSubmit = (event) => {
        // evite le rechargement
        event.preventDefault();

        const formData = new URLSearchParams();
        formData.append("j_username", pseudo);
        formData.append("j_password", mdp);

    fetch("http://localhost:8080/j_security_check", {
        method: "POST",
        body: formData,
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        credentials: 'include',
    })
    .then((response) => {
        if (response.status === 200) {
            setRedirigerAccueil(true);
            console.log("Authentification réussie.");
        } else {
            setErreur("Pseudo et/ou mot de passe erroné(s).")
            console.error("Pseudo et/ou mot de passe erroné(s).");
        }
    })
    }
  
    if (redirigerAccueil) {
        return <Redirect to="/" />;
      }

    return (
        <div className="conteneurGenerique">
            <form onSubmit={handleLoginFormSubmit} className = "formulaire">
                <label>
                    Pseudo:
                    <input type="text" value={pseudo} onChange={handlePseudoChange} />
                </label>
                <br />
                <label>
                    Mot de passe:
                    <input type="password" value={mdp} onChange={handleMdpChange} />
                </label>
                <br />
                <button type="submit">Se connecter</button>
                <br />{erreur}
            </form>
        </div>   
    );
}