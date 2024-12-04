const GestionParticipantComposant = () => {
    const [participants, setParticipants] = React.useState([]);
    const [organisateurs, setOrganisateurs] = React.useState([]);

    React.useEffect(() => {
        fetchParticipants();
        fetchOrganisateurs();
    }, []);

    const fetchParticipants = () => {
        fetch(`http://localhost:8080/participant/all`, {
            method: 'GET',
            credentials: "include"
        })
            .then(resp => {
                if (!resp.ok) {
                    console.error("Erreur lors de la récupération des participants.");
                }
                return resp.json();
            })
            .then(data => {
                setParticipants(data);
            })
    }

    const fetchOrganisateurs = () => {
        fetch(`http://localhost:8080/organisateur/all`, {
            method: 'GET',
            credentials: "include"
        })
            .then(resp => {
                if (!resp.ok) {
                    console.error("Erreur lors de la récupération des organisateurs.");
                }
                return resp.json();
            })
            .then(data => {
                setOrganisateurs(data);
            })
    }

    const banUtilisateur = (utilisateur) => {
        fetch(`http://localhost:8080/compte/ban/${utilisateur}`, {
            method: "POST",
            credentials: "include",
        })
            .then(resp => {
                if (resp.ok) {
                    console.log("Bannissement réussi.");
                    fetchParticipants();
                    fetchOrganisateurs();
                } else {
                    console.error("Erreur lors du banissement.");
                }
            })
    };




    return (
        <div>
            <div className="conteneurGenerique">
                <div className="listeGenerique">
                    <h1> Liste des participants</h1>
                    {participants.map((participant) => (
                        <ul key={participant.id} style={{ listStyleType: "none" }}>
                            <li>Titre : {participant.pseudo} </li>
                            <button className="button-refuse" onClick={() => banUtilisateur(participant.pseudo)} > Bannir </button>
                        </ul>
                    ))}
                </div>
                <div className="listeGenerique">
                    <h1> Liste des organisateur</h1>
                    {organisateurs.map((organisateur) => (
                        <ul key={organisateur.id} style={{ listStyleType: "none" }}>
                            <li>Titre : {organisateur.pseudo} </li>
                            <button className="button-refuse" onClick={() => banUtilisateur(organisateur.pseudo)} > Bannir </button>
                        </ul>
                    ))}
                </div>
            </div>
        </div>
    );






};