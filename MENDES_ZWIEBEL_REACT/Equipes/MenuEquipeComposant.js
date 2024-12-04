const MenuEquipeComposant = () => {
  const [equipes, setEquipes] = React.useState([]);

  React.useEffect(() => {
    fetchEquipes();
  }, []);

  const fetchEquipes = () => {
    fetch('http://localhost:8080/equipe/all', {
      method: 'GET',
    })
      .then(resp => {
        if (!resp.ok) {
          console.error("Erreur lors de la recupération des équipes.");
        }
        return resp.json();
      })
      .then(data => {
        setEquipes(data);
      })
  }

  return (
    <div className="conteneurGenerique">
      <div className="listeGenerique">
        <h1>Equipes</h1>
        <Link to="/creerequipe"><button className="simple-button">Créer une équipe</button></Link>
        <ul style={{ listStyleType: "none" }}>
          {equipes.map(equipe => (
            <li key={equipe.id}>
              <Link to={`/equipe/${equipe.nom}`}>{equipe.nom}</Link> | Chef : {equipe.chef.pseudo}
            </li>
          ))}
        </ul>
      </div>
      
    </div>
  );
}