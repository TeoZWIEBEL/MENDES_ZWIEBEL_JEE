const ConsulterCTFComposant = () => {
    const [ctf, setCtf] = React.useState('');
    const [participants, setParticipants] = React.useState([]);
    const [commentaires, setCommentaires] = React.useState([]);
    const [commentairePost, setCommentairePost] = React.useState("");
    const [chefEquipe, setChefEquipe] = React.useState(false);

    const [etatInscription, setetatInscription] = React.useState("");
    const { titreCTF: routeTitreCTF } = useParams();

    React.useEffect(() => {
        fetchCtf();
        fetchParticipantsCTF();
        fetchCommentaires();
        fetchChefEquipe();
    }, [routeTitreCTF]);

    const fetchCtf = () => {
        fetch(`http://localhost:8080/ctf/${routeTitreCTF}`, {
            method: 'GET',
        })
            .then(resp => {
                if (!resp.ok) {
                    console.error("Erreur de récupération du CTF.");
                }
                return resp.json();
            })
            .then(data => {
                setCtf(data);
            })
    }

    const fetchCommentaires = () => {
        fetch(`http://localhost:8080/commentaire/${routeTitreCTF}`, {
            method: 'GET',
        })
            .then(resp => {
                if (!resp.ok) {
                    console.error("Erreur de récupération des commentaires.")
                }
                return resp.json();
            })
            .then(data => {
                setCommentaires(data);
            })
    }

    const fetchParticipantsCTF = () => {
        fetch(`http://localhost:8080/ctf/${routeTitreCTF}/participants`, {
            method: "GET",
        })
            .then((resp) => {
                if (!resp.ok) {
                    console.error("Erreur de récupération des Participants.")
                }
                return resp.json();
            })
            .then((data) => {
                setParticipants(data);
            })
    };

    const fetchChefEquipe = () => {
        fetch(`http://localhost:8080/participant/checkChef`, {
            method: "GET",
            credentials: "include",
        })
            .then((resp) => {
                if (!resp.ok) {
                    console.error("Erreur de verification du chef.")
                }
                return resp.json();
            })
            .then((data) => {
                setChefEquipe(data);
            })
    };


    const inscrireEquipeCTF = () => {
        fetch(`http://localhost:8080/equipe/inscrire/${routeTitreCTF}`, {
            method: "POST",
            credentials: "include",
        })
            .then((response) => {
                if (response.ok) {
                    console.log("Inscription de l'équipe réussie.")
                    setetatInscription("Inscription de l'équipe réussie");
                    fetchParticipantsCTF();
                } else {
                    console.error("Erreur d'inscription de l'équipe.");
                    setetatInscription("L'équipe est déjà inscrite");
                }
            })
    }

    const inscrireParticipantCTF = () => {
        fetch(`http://localhost:8080/ctf/inscrire/${routeTitreCTF}`, {
            method: "POST",
            credentials: "include",
        })
            .then((response) => {
                if (response.ok) {
                    console.log("Inscription du participant réussie.")
                    setetatInscription("Inscription réussie");
                    fetchParticipantsCTF();
                } else {
                    console.error("Erreur d'inscription du participant.");
                    setetatInscription("Déjà inscrit");
                }
            })
    };

    const desinscrireParticipantCTF = () => {
        fetch(`http://localhost:8080/ctf/desinscrire/${routeTitreCTF}`, {
            method: "POST",
            credentials: "include",
        })
            .then((response) => {
                if (response.ok) {
                    console.log("Desinscription du participant réussie.")
                    setetatInscription("Desinscription réussie");
                    fetchParticipantsCTF();
                } else {
                    console.error("Erreur de desinscription.");
                    setetatInscription("Pas inscrit.");
                }
            })
    }

    const handleCommentairePostChange = (event) => {
        setCommentairePost(event.target.value);
    }

    const posterCommentaire = (event) => {
        //evite le rechargement
        event.preventDefault();

        const formData = new URLSearchParams();
        formData.append("commentaire", commentairePost);

        fetch(`http://localhost:8080/commentaire/poster/${routeTitreCTF}`, {
            method: "POST",
            body: formData,
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            credentials: 'include',
        })
            .then((response) => {
                if (response.status === 200) {
                    console.log("Commentaire posté.")
                    fetchCommentaires();
                } else {
                    console.error("Erreur lors de la création du commentaire.");
                }
            })
    }

    return (
        <div>
            <div className="conteneurGenerique">
                <div className="listeGenerique">
                    <h1>{ctf.titre}</h1>
                    <ul style={{ listStyleType: "none" }}>
                        <li>Description : {ctf.description}</li>
                        <li>Lieu : {ctf.lieu}</li>
                        <li>Contact : {ctf.contact ? ctf.contact.pseudo : "Non spécifié"}</li> 
                        <li>Vues : {ctf.vues}</li>
                    </ul>
                </div>
                <div className="listeGenerique">
                    <h1>Participants</h1>
                    <button className="simple-button" onClick={inscrireParticipantCTF}>S'inscrire</button>
                    {chefEquipe ? (<button className="simple-button" onClick={inscrireEquipeCTF}> Inscription equipe</button>) : ("")}
                    <button className="simple-button" onClick={desinscrireParticipantCTF}>Se désincrire</button>
                    <br />{etatInscription}
                    <ul style={{ listStyleType: "none" }}>
                        {Object.keys(participants).map((pseudo) => (
                            <li key={pseudo}>
                                <Link to={`/participant/${pseudo}`}>{pseudo}</Link> | Score: {participants[pseudo]}

                            </li>
                        ))}
                    </ul>
                </div>
            </div>
            <div className="listeGenerique">
                <h1>Commentaires</h1>
                <ul style={{ listStyleType: "none" }}>
                    {commentaires.map((commentaire) => (
                        <li key={commentaire.id}>
                            {commentaire.participant.pseudo} : {commentaire.commentaire}
                        </li>
                    ))}
                </ul>
            </div>
            <form onSubmit={posterCommentaire}>
                <label>
                    <input type="text" value={commentairePost} onChange={handleCommentairePostChange} />
                </label>
                <button type="submit">Poster</button>
            </form>
        </div>
    );
};
