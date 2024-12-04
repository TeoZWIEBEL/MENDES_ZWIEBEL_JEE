const ChefEquipeComposant = () => {
    const [equipe, setEquipe] = React.useState("");
    const [membres, setMembres] = React.useState([]);
    const [loading, setLoading] = React.useState(true); //https://stackoverflow.com/questions/58934643/fetching-data-in-reacts-useeffect-returns-undefined
    const [finish, setFinish] = React.useState(false);
    const { nomEquipe: routeNomEquipe } = useParams();

    React.useEffect(() => {
        fetchEquipe();
        fetchMembres();
    }, [routeNomEquipe]);

    const fetchEquipe = () => {
        setLoading(true);
        fetch(`http://localhost:8080/equipe/${routeNomEquipe}`, {
            method: 'GET',
        })
            .then(resp => {
                if (!resp.ok) {
                    console.error("Erreur de récuperation de l'équipe.")
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

    const transfererChef = (pseudo) => {
        fetch(`http://localhost:8080/equipe/transfererChef/${pseudo}`, {
            method: 'POST',
            credentials: 'include',
        })
            .then(resp => {
                if (resp.ok) {
                    console.log("Transfère réussi.");
                  } else {
                    console.error("Erreur de transfère.");
                  }
            })
            .finally(() => {
                setFinish(true); 
            });
    };

    if (loading) {
        return <p>Loading...</p>;
    }

    if (finish) {
        return <Redirect to={`/equipe/${routeNomEquipe}`} />;
    }

    return (
        <div>
            <div className="conteneurGenerique">
                <div className="listeGenerique">
                    <h1>{equipe.nom}</h1>
                    <ul style={{ listStyleType: "none" }}>
                        {membres.map(membre => (
                            <li key={membre.id}>
                                {equipe.chef && membre.pseudo === equipe.chef.pseudo ? ("") : (
                                    <span>
                                        <button className="button-pseudo" onClick={() => transfererChef(membre.pseudo)}>{membre.pseudo}</button>
                                    </span>
                                )}
                            </li>
                        ))}
                    </ul>
                </div>
            </div>
        </div>
    );
};
