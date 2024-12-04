const CreationEquipeComposant = () => {
    const [nom, setNom] = React.useState("");
    const [description, setDescription] = React.useState("");
    const [erreur, setErreur] = React.useState("");
  
    const creerEquipe = (event) => {
        event.preventDefault();
        const formData = new URLSearchParams();
        formData.append("nom", nom);
        formData.append("description", description);


        fetch(`http://localhost:8080/equipe/creer`, {
              method: 'POST',
              body: formData,
              credentials: 'include',
            })
              .then(response => {
                if (response.ok) {
                  console.log("Equipe créee.");
                  setErreur("Equipe créée");
                } else {
                  console.error("Erreur lors de la création de l'équipe.");
                  setErreur("Erreur de création");
                }
              })
      }

      return (
        <div className = "conteneurGenerique">
            <form onSubmit={creerEquipe} className="formulaire">
                <label>
                    Equipe:
                    <input type="text" value={nom} onChange={(e) => setNom(e.target.value)} />
                </label>
                <br />
                <label>
                    Description :
                    <input type="text" value={description} onChange={(e) => setDescription(e.target.value)} />
                </label>
                <br />
                <button type="submit">Creer</button>
                <br />{erreur}

            </form>
        </div>   
    );
  }