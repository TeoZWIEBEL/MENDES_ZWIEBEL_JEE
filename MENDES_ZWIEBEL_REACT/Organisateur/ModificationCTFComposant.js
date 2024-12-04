const ModificationCTFComposant = () => {
    const [titre, setTitre] = React.useState("");
    const [description, setDescription] = React.useState("");
    const [lieu, setLieu] = React.useState("");
    const [erreur, setErreur] = React.useState("");
    const { CTFtitre: routeCTFtitre} = useParams();


    React.useEffect(() => {
      },[routeCTFtitre]);

    const handleTitreChange = (event) => {
        setTitre(event.target.value);
    }

    const handleDescriptionChange = (event) => {
        setDescription(event.target.value);
    }

    const handleLieuChange = (event) => {
        setLieu(event.target.value);
    }

    const modifierCTF = (event) => {
       //evite le rechargement
       event.preventDefault();
 
        const formData = new URLSearchParams();
 
        formData.append("titre", titre);
        formData.append("description", description);
        formData.append("lieu", lieu);
         
    fetch(`http://localhost:8080/ctf/${routeCTFtitre}/modifierCTF`, {
        method: "POST",
        body: formData,
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        credentials: 'include',
    })
    .then((response) => {
        if (response.status === 200) {
            console.log("Modification du CTF réussie.");
            setErreur("Modification du CTF réussie")
        } else {
            console.error("Erreur de modification du CTF.");
            setErreur("Erreur de modification, verifiez que le titre n'est pas nul.")
        }
    })
     }
  
      return (
        <div className="conteneurGenerique">
            { <form onSubmit={modifierCTF} className="formulaire">
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
                <br /><button type="submit">Modifier</button>
                <br />{erreur}
            </form> }
        </div>
    );
}