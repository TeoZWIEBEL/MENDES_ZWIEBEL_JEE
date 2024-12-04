const DeconnexionComposant = () => {

  React.useEffect(() => {
    deconnexion();
  }, []);

  // on se deconnecte en demandant un cookie expiré au serveur
  // on pourrait aussi simplement detruire le cookie que nous avons coté client.
  const deconnexion = () => {
    fetch('http://localhost:8080/auth/deconnexion', {
      method: 'POST',
      credentials: 'include',
    })
      .then(response => {
        if (response.ok) {
          console.log("Déconnexion réussie.");
        } else {
          console.error("Erreur de déconnexion.");
        }
      })
  }

  return <Redirect to="/" />; 
}
