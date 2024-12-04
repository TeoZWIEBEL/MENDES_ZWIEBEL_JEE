const MenuDefisComposant = () => {
    const [defis, setDefis] = React.useState([]);
    const [categorie, setCategorie] = React.useState("all");

    React.useEffect(() => {
      fetchDefis();
    }, []);
  
    const fetchDefis = () => {
      fetch('http://localhost:8080/defis/all', {
        method: 'GET',
      })
        .then(resp => {
          if (!resp.ok) {
            console.error("Erreur de récuperation des défis.");
          }
          return resp.json();
        })
        .then(data => {
          setDefis(data);
        })
    }

    const handleCategorieChange = (event) => {
        setCategorie(event.target.value);
    }

    return (
      <div className = "conteneurGenerique">
        <div className = "listeGenerique">
        <h1>Defis</h1>
        Catégorie:
            <select name="Categorie" value={categorie} onChange={handleCategorieChange}>
                <option value="all">Toutes</option>
                <option value="FUN">FUN</option>
                <option value="DIFFICILE">DIFFICILE</option>
                <option value="NORMAL">NORMAL</option>
                <option value="FACILE">FACILE</option>

            </select>
            <ul style={{ listStyleType: "none" }}>
            {defis.map((defi) => (
          (categorie === "all" || defi.categorie === categorie) && (
            <li key={defi.id}>
              <Link to={`/defi/${defi.titre}`}>{defi.titre}</Link>
            </li>
          )
        ))}
        </ul>
        </div> 
      </div>
      
    );
  }
  