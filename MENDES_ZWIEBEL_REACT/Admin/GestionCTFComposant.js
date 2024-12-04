const GestionCTFComposant = () => {
    const [ctfs, setCtfs] = React.useState([]);

    React.useEffect(() => {
        fetchCtf();
    }, []);

    const fetchCtf = () => {
        fetch(`http://localhost:8080/ctf/attentes`, {
            method: 'GET',
            credentials: "include"
        })
            .then(resp => {
                if (!resp.ok) {
                    console.error("Erreur de récupération des CTFs.");
                }
                return resp.json();
            })
            .then(data => {
                setCtfs(data);
            })
    }

    const refuserCTF = (ctf) => {
        fetch(`http://localhost:8080/ctf/refuser/${ctf}`, {
            method: "POST",
            credentials: "include",
        })
            .then((response) => {
                if (response.ok) {
                    console.log("Refus réussi.");
                    fetchCtf();
                } else {
                    console.error("Erreur lors du refus.");
                }
            })
        }

    const validerCTF = (ctf) => {
        fetch(`http://localhost:8080/ctf/valider/${ctf}`, {
            method: "POST",
            credentials: "include",
        })
            .then((response) => {
                if (response.ok) {
                    console.log("Validation réussie.");
                    fetchCtf();
                } else {
                    console.error("Erreur lors de la validation.");
                }
            })
    };


    return (
        <div>
            <div className="conteneurGenerique">
                <div className="listeGenerique">
                    {ctfs.map((ctf) => (
                        
                        <ul key={ctf.id} style={{ listStyleType: "none" }}>

                            <li>Titre : {ctf.titre} </li>
                            <li>Description : {ctf.description}</li>
                            <li>Lieu : {ctf.lieu}</li>
                            <li>Contact : {ctf.contact ? ctf.contact.pseudo : "Non spécifié"}</li>
                            <button className="button-refuse"  onClick={() => refuserCTF(ctf.titre)} >Refuser le CTF</button>
                            <button className="button-accept" onClick={() => validerCTF(ctf.titre)} >Valider CTF</button>
                        </ul>
                        
                    ))}
                </div>
            </div>
        </div>
    );





};
