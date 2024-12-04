const ConsulterEquipeComposant = () => {
  const [equipe, setEquipe] = React.useState("");
  const [membres, setMembres] = React.useState([]);
  const [demandes, setDemandes] = React.useState([]);
  const [loading, setLoading] = React.useState(true); //https://stackoverflow.com/questions/58934643/fetching-data-in-reacts-useeffect-returns-undefined
  const [redigiderTransferer, setredirigerTransferer] = React.useState(false);
  const { nomEquipe: routeNomEquipe } = useParams();

  React.useEffect(() => {
    fetchEquipe();
    fetchMembres();
    fetchDemande();
  }, [routeNomEquipe]);

  const fetchEquipe = () => {
    setLoading(true);
    fetch(`http://localhost:8080/equipe/${routeNomEquipe}`, {
      method: 'GET',
    })
      .then(resp => {
        if (!resp.ok) {
          console.log("Erreur de récupération de l'équipe.")
        }
        return resp.json();
      })
      .then(data => {
        setEquipe(data);
      })
      .finally(() => {
        setLoading(false); 
      });
  };

  const fetchMembres = () => {
    fetch(`http://localhost:8080/equipe/${routeNomEquipe}/membres`, {
      method: 'GET',
      credentials: "include",
    })
      .then(resp => {
        if (!resp.ok) {
          console.error("Erreur de récupération des membres de l'équipe.")
        }
        return resp.json();
      })
      .then(data => {
        setMembres(data);
      })
  };

  const fetchDemande = () => {
    fetch(`http://localhost:8080/equipe/demandes`, {
      method: 'GET',
      credentials: "include",
    })
      .then(resp => {
        if (!resp.ok) {
          console.error("Erreur de récupération des demandes.");
        }
        return resp.json();
      })
      .then(data => {
        setDemandes(data);
      })
  };

  const accepterDemande = (pseudo) => {
    fetch(`http://localhost:8080/equipe/demande/${pseudo}/accepter`, {
      method: 'POST',
      credentials: "include",
    })
      .then((response) => {
        if (response.ok) {
          console.log("Acceptation réussie.");
          fetchEquipe();
          fetchMembres();
          fetchDemande();
        } else {
          console.error("Erreur lors de l'acceptation de la demande.");
        }
      })
  };

  const refuserDemande = (pseudo) => {
    fetch(`http://localhost:8080/equipe/demande/${pseudo}/refuser`, {
      method: 'POST',
      credentials: "include",
    })
      .then((response) => {
        if (response.ok) {
          console.log("Refus réussi.");
          fetchEquipe();
          fetchDemande();
        } else {
          console.error("Erreur lors du refus de la demande.");

        }
      })
  };

  const transfererChef = () => {
    setredirigerTransferer(true);
  };

  const formulerDemande = () => {
    fetch(`http://localhost:8080/participant/demande/${routeNomEquipe}`, {
      method: 'POST',
      credentials: 'include',
    })
      .then(response => {
        if (response.ok) {
          console.log("Demande créée avec succès");
        } else {
          console.error("Erreur lors de la création de la demande.");
        }
      })
  };

  if (loading) {
    return <p>Loading...</p>;
  }
  else if (redigiderTransferer) {
    return <Redirect to={`/transfererChef/${routeNomEquipe}`} />;
  }

  return (
    <div>
      <div className="conteneurGenerique">
        <div className="listeGenerique">
          <h1>{equipe.nom}</h1>
          <ul style={{ listStyleType: "none" }}>
            {membres.map(membre => (
              <li key={membre.id}>
                {equipe.chef && membre.pseudo === equipe.chef.pseudo ? (
                  <span>
                    <b>{membre.pseudo} </b>| Score : {membre.score}
                  </span>
                ) : (
                  <span>
                    {membre.pseudo} | Score : {membre.score}
                  </span>
                )}
              </li>
            ))}
          </ul>
          {membres.some(user => user.pseudo === global_utilisateur) ? ("") : (<button className="simple-button" onClick={formulerDemande}>Rejoindre</button>)}
          {equipe.chef.pseudo === global_utilisateur ? (<button className="simple-button" onClick={transfererChef}>Transférer le chef d'équipe</button>) : ("")}
        </div>
        {equipe.chef.pseudo === global_utilisateur ? (
          <div className="petitelisteGenerique" >
            <h3>Demandes pour rejoindre l'équipe</h3>
            {demandes.map(demande => (
              <ul key={demande.participant.id} style={{ listStyleType: "none" }} >
                <li>Pseudo :{demande.participant.pseudo}</li>
                <li> Mail :{demande.participant.mail}</li>
                <li> Score : {demande.participant.score}</li>
                <button className="button-accept" onClick={() => accepterDemande(demande.participant.pseudo)}> Accepter la demande</button>
                <button className="button-refuse" onClick={() => refuserDemande(demande.participant.pseudo)}> Refuser la demande</button>
              </ul>
            ))}
          </div>
        ) : ("")}
      </div>
    </div>
  );
};
