const MenuCTFComposant = () => {
  const [ctfs, setCtfs] = React.useState([]);
 
  React.useEffect(() => {
    fetchCtfs();
  }, []);

  const fetchCtfs = () => {
    fetch('http://localhost:8080/ctf/valides', {
      method: 'GET',
    })
      .then(resp => {
        if (!resp.ok) {
          console.error("Erreur lors de la récupération des CTFs.");
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
      <h1>Ctfs</h1>

      <ul style={{listStyleType: "none"}}>
        {ctfs.map(ctf => (
          <li key={ctf.id}>
            <Link to={`/ctf/${ctf.titre}`}>{ctf.titre}</Link> |  Organisateur : {ctf.contact.pseudo} | Vues : {ctf.vues}
          </li>
        ))}
      </ul>
      </div> 
    </div>
    
  );
}
