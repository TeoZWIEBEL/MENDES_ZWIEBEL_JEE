// Composant utilisant une fonction pour etre défini
const AccueilComposant = () => {
  const [participants, setParticipants] = React.useState([]); // la notion d'état est similaire à celle d'un composant objet.
  const [ctfs, setCtfs] = React.useState([]);

  React.useEffect(() => {
    fetchParticipants();
    fetchCtfs();
  },[]); // Si les crochets ne sont pas mis, le useEffect() est tout le temps appelé

  //requete GET classique qui recupère aux format JSON les participants
  const fetchParticipants = () => {
    fetch('http://localhost:8080/participant/all', {
      method: 'GET',
    })
      .then(resp => {
        if (!resp.ok) {
          console.error("Erreur de récupération des participants.");
        }
        return resp.json();
      })
      .then(data => {
        setParticipants(data);
      })
  }

  const fetchCtfs = () => {
    fetch('http://localhost:8080/ctf/valides', {
      method: 'GET',
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

  return (
    <div className = "conteneurGenerique">
      <div className = "listeGenerique">
      <h1>Classement général</h1>
      <ol>
        {participants.map(participant => (
          <li key={participant.id}>
            <Link to={`/participant/${participant.pseudo}`}>{participant.pseudo}</Link> | {participant.score}
          </li>
        ))}
      </ol>
      </div>
      <div className = "listeGenerique">
        <h1>CTFs</h1>
        <ul>
          {ctfs.map(ctf => (
            <li key={ctf.id}>
              <Link to={`/ctf/${ctf.titre}`}>{ctf.titre}</Link> | {ctf.lieu}
            </li>
          ))}
        </ul>
      </div>  
    </div>
    
  );
}