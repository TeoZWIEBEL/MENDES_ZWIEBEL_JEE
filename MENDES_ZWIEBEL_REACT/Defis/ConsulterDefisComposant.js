const ConsulterDefisComposant = () => {
    const [defi, setDefi] = React.useState("");
    const [erreur, setErreur] = React.useState("");

    const { titreDefi: routeTitreDefi } = useParams();

    React.useEffect(() => {
        fetchDefi();
    }, [routeTitreDefi]);

    const fetchDefi= () => {
        fetch(`http://localhost:8080/defis/${routeTitreDefi}`, {
            method: 'GET',
        })
            .then(resp => {
                if (!resp.ok) {
                    console.error("Erreur lors de la récupération du défi.");
                }
                return resp.json();
            })
            .then(data => {
                setDefi(data);
            })
    }

    const completerDefi = () => {
        fetch(`http://localhost:8080/defis/${routeTitreDefi}/completer`, {
            method: "POST",
            credentials: "include",
        })
            .then((response) => {
                if (response.ok) {
                    console.log("Défi complété");
                    setErreur("Défi complété");
                } else {
                    console.error("Erreur lors de la complétion");
                    setErreur("Erreur lors de la complétion");

                }
            })
    };

    return (
        <div className="conteneurGenerique">
            <div className="listeGenerique">
                <h1>{defi.titre}</h1>
                <ul style={{ listStyleType: "none" }}>
                    <li>Catégorie : {defi.categorie}</li>
                    <li>Points : {defi.points}</li>
                </ul>
                <button className="button-accept"  onClick={() => completerDefi()} >Completer le Défi</button>
                <br/>{erreur}
            </div>
     </div>
    );
}