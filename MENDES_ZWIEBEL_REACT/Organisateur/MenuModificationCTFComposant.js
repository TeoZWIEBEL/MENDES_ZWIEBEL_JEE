const MenuModificationCTFComposant = () => {
    const [ctfsValide, setCtfsValide] = React.useState([]);
    const [ctfsAttente, setCtfsAttente] = React.useState([]);
    const [titreCTF, setTitreCTF] = React.useState("");

    const [redirigerModification, setRedirigerModification] = React.useState(false);
    const [redirigerSaisie, setRedirigerSaisie] = React.useState(false);


    React.useEffect(() => {
        fetchCtfValide();
        fetchCtfAttente();
    }, []);

    const fetchCtfValide = () => {
        fetch(`http://localhost:8080/ctf/organisateur/valides`, {
            method: 'GET',
            credentials: "include"
        })
            .then(resp => {
                if (!resp.ok) {
                    console.error("Erreur lors de la récupération des CTFs validés.")
                }
                return resp.json();
            })
            .then(data => {
                setCtfsValide(data);
            })
    }

    const fetchCtfAttente = () => {
        fetch(`http://localhost:8080/ctf/organisateur/attentes`, {
            method: 'GET',
            credentials: "include"
        })
         .then(resp => {
                if (!resp.ok) {
                    console.error("Erreur lors de la récupération des CTFs en attente.");
                }
                return resp.json();
           })
            .then(data => {
               setCtfsAttente(data);
            })
    }

    const supprimerCTF = (ctf) => {
        fetch(`http://localhost:8080/ctf/${ctf.titre}/supprimer`, {
            method: "POST",
            credentials: "include",
        })
            .then((response) => {
                if (response.ok) {
                    console.log("Ctf supprimé.");
                    fetchCtfAttente();
                    fetchCtfValide();
                } else {
                    console.error("Erreur lors de la suppression du CTF.");
                }
            })
    };

    const modifierCTF = (ctf) => {
        setTitreCTF(ctf.titre);
        setRedirigerModification(true);
    };

    const saisirScoreCTF = (ctf) => {
        setTitreCTF(ctf.titre);
        setRedirigerSaisie(true);
    };

    if(redirigerModification){
        return <Redirect to={`/modificationCTF/${titreCTF}`} />;
    }

    if(redirigerSaisie){
        return <Redirect to={`/saisieScores/${titreCTF}`} />;
    }


    return (
        <div>
            <div className="conteneurGenerique">
                <div className="listeGenerique">
                    <h1> CTF validés</h1>
                    {ctfsValide.map((ctf) => (
                        <ul key={ctf.id} style={{ listStyleType: "none" }}>
                            <li>Titre : {ctf.titre} </li>
                            <li>Description : {ctf.description}</li>
                            <li>Lieu : {ctf.lieu}</li>
                            <li>Contact : {ctf.contact ? ctf.contact.pseudo : "Non spécifié"}</li>
                            <button className="simple-button" onClick={() => modifierCTF(ctf)}>Modifier CTF</button>
                            <button className="simple-button" onClick={() => supprimerCTF(ctf)}>Supprimer CTF</button>
                            <button className="simple-button" onClick={() => saisirScoreCTF(ctf)}>Saisir Score</button>
                        </ul>
                    ))}
                </div>
                <div className="listeGenerique">
                    <h1> CTF en attente</h1>
                    {ctfsAttente.map((ctf) => (
                        <ul key={ctf.id} style={{ listStyleType: "none" }}>
                            <li>Titre : {ctf.titre} </li>
                            <li>Description : {ctf.description}</li>
                            <li>Lieu : {ctf.lieu}</li>
                            <li>Contact : {ctf.contact ? ctf.contact.pseudo : "Non spécifié"}</li>
                            <button className="simple-button" onClick={() => modifierCTF(ctf)}>Modifier CTF</button>
                            <button  className="simple-button" onClick={() => supprimerCTF(ctf)}>Supprimer CTF</button>
                        </ul>
                    ))}
                </div>
            </div>
        </div>
    );





};
