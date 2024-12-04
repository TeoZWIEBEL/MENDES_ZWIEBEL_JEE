const CreerCTFComposant = () => {
    const [titre, setTitre] = React.useState("");
    const [description, setDescription] = React.useState("");
    const [lieu, setLieu] = React.useState("");
    const [erreur, setErreur] = React.useState("");


    const handleTitreChange = (event) => {
        setTitre(event.target.value);
    }

    const handleDescriptionChange = (event) => {
        setDescription(event.target.value);
    }

    const handleLieuChange = (event) => {
        setLieu(event.target.value);
    }

    const creerCTF = (event) => {
        //evite le rechargement
        event.preventDefault();
 
        const formData = new URLSearchParams();
 
        formData.append("titre", titre);
        formData.append("description", description);
        formData.append("lieu", lieu);
         
    fetch("http://localhost:8080/ctf/creer", {
        method: "POST",
        body: formData,
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        credentials: 'include',
    })
    .then((response) => {
        if (response.status === 200) {
            console.log("Creation de CTF réussie");
            setErreur("Creation de CTF réussie, attente de validation")
        } else {
            console.error("Erreur de création.");
            setErreur("Erreur lors de la création, vérifiez que le titre n'est pas nul ou que le CTF n'existe pas déjà.")
        }
    })
     }
  
      return (
        <div className="conteneurGenerique">
            <form onSubmit={creerCTF} className="formulaire">
                <label>
                    Titre:
                    <input type="text" value={titre} onChange={handleTitreChange} />
                </label>
                <br />
                <label>
                    Description:
                    <input type="text" value={description} onChange={handleDescriptionChange} />
                </label>
                <br />
                <label>
                    Lieu :
                    <input type="text" value={lieu} onChange={handleLieuChange} />
                </label>
                <br /><button type="submit">Creer</button>
                <br />{erreur}
            </form>
        </div>
    );
}