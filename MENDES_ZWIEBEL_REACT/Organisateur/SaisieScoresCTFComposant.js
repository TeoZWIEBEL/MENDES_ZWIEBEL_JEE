const SaisieScoresCTFComposant = () => {
    const [participants, setParticipants] = React.useState([]);
    const [scores, setScores] = React.useState([]);
    const [erreur, setErreur] = React.useState("");
    const [redirigerAccueil, setRedirigerAccueil] = React.useState(false);
    const { CTFtitre: routeCTFtitre } = useParams();
    
    React.useEffect(() => {
        fetchParticipants();
    }, [routeCTFtitre]);

    // Change la valeur du tableau de score en ne modifiant que la valeur de l'élément modifié
    const handleScore = (event) => {
        setScores(prevScores => {
            // Copie du tableau initial
            var updateScore = [];
            for (let index = 0; index < prevScores.length; index++) {
                updateScore.push(prevScores[index]);
                
            }
            // Modification de la valeur correspondant au score modifié
            var _value = 0;
            try{
                _value =parseInt(event.target.value);
            }
            catch(error){
                console.log(error);
            }
            finally{
                updateScore[event.target.id] = _value;
            return updateScore;
            }
        });
    };

    const fetchParticipants = () => {
        fetch(`http://localhost:8080/ctf/${routeCTFtitre}/participants`, {
            method: 'GET',
            credentials: "include"
        })
            .then(resp => {
                if (!resp.ok) {
                    console.error("Erreur de récupération des participants du CTF.");
                }
                return resp.json();
            })
            .then(data => {
                // Tableau  avec tous les pseudos des participants
                setParticipants(Object.keys(data).map((pseudo) => pseudo));
                // Tableau avec tous les scores des participants
                setScores(Object.keys(data).map((pseudo) => data[pseudo]))
            })
    }

    const saisieScore = (event) => {
        //evite le rechargement
        event.preventDefault();
        // Création d'un tableau que l'on met sous format JSON
        const jsonData = JSON.stringify( Object.fromEntries(participants.map((key, index) => [key, scores[index]])));

        fetch(`http://localhost:8080/ctf/${routeCTFtitre}/saisirScore`, {
            method: "POST",
            body: jsonData,
            headers: {
                "Content-Type": "application/json",
            },
            credentials: 'include',
        })
            .then((response) => {
                if (response.status === 200) {
                    console.log("Score saisis avec succès.");
                    setRedirigerAccueil(true);
                    setErreur("Score saisis !")
                } else {
                    console.error("Erreur lors de la saisie.");
                    setErreur("Erreur lors de la saisie, vérifiez les champs.")
                }
            })
    }
    if(redirigerAccueil){
        return <Redirect to="/" />; 
    }
    return (
        <div>
            <div className="conteneurGenerique">
                <h1> Saisie des scores {routeCTFtitre}</h1>
            </div>
            <div className="conteneurGenerique">
                <h2>Liste des partipants du CTF</h2>
                <form onSubmit={saisieScore} className="formulaire">
                    {Object.keys(participants).map((index) => (
                        <ul key={index} style={{ listStyleType: "none" }}>
                        <li>{participants[index]} </li>
                        <label htmlFor="score"> Score </label>
                        <input type="text" id={index} name="score" value={scores[index]} onChange={(e) => handleScore(e)} />
                        </ul>                        
                    ))}
                    <br /><button type="submit">Appliquer</button>
                    <br />{erreur}
                </form>
            </div>
        </div>
    );
}