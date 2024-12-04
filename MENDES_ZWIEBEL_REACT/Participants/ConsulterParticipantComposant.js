const ConsulterParticipantComposant = () => {
    const [participant, setParticipant] = React.useState("");
    const [redirigerDiscussion, setRedirigerDiscussion] = React.useState(false);

    const { pseudoParticipant: routePseudoParticipant } = useParams();

   React.useEffect(() => {
        fetchParticipant();
    }, [routePseudoParticipant]);

    const fetchParticipant = () => {
        fetch(`http://localhost:8080/participant/${routePseudoParticipant}`, {
            method: "GET",
        })
            .then((resp) => {
                if (!resp.ok) {
                    console.error("Erreur de récupération du participant.")
                }
                return resp.json();
            })
            .then((data) => {
                setParticipant(data);
            })
    };

    const initierDiscussion = () => {
        fetch(`http://localhost:8080/discussion/initierDiscussion/${routePseudoParticipant}`, {
            method: "POST",
            credentials: "include", // on include les credential, l'utilisateur doit être vérifié et également role participant
        })
        .then((response) => {
            if (response.status === 200) {
                console.log("Discussion créée.");
                setRedirigerDiscussion(true);
            } else {
                console.error("Erreur de création de discussion, probablement existante.");
            }
        })
    };

    if (redirigerDiscussion === true) {
        return <Redirect to={`/discussion/${routePseudoParticipant}`} />;
      }
      
    return (
        <div className="conteneurGenerique">
            <div className="listeGenerique">
                <h1>{participant.pseudo}</h1><button onClick={initierDiscussion}>Initier une discussion</button>
                <ul style={{ listStyleType: "none" }}>
                    <li>Mail : {participant.mail}</li>
                    <li>Score : {participant.score}</li>
                </ul>
            </div>
        </div>
    );
};
