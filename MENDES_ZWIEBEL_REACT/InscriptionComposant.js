const InscriptionComposant = () => {
    const [pseudo, setPseudo] = React.useState("");
    const [mdp, setMdp] = React.useState("");
    const [mail, setMail] = React.useState("");
    const [role, setRole] = React.useState("Participant");
    const [succesInscription, setSuccesInscription] = React.useState("");

    const [redirigerAccueil, setRedirigerAccueil] = React.useState(false);

    const handlePseudoChange = (event) => {
        setPseudo(event.target.value);
    }

    const handleMdpChange = (event) => {
        setMdp(event.target.value);
    }

    const handleMailChange = (event) => {
        setMail(event.target.value);
    }

    const handleRoleChange = (event) => {
        setRole(event.target.value);
    }

    const handleInscriptionFormSubmit = (event) => {
        //evite le rechargement
        event.preventDefault();
 
        const formData = new URLSearchParams();
 
        formData.append("pseudo", pseudo);
        formData.append("mdp", mdp);
        formData.append("mail", mail);
 
        if(role == "Participant") {
             var url = "http://localhost:8080/compte/creerparticipant"
        } else {
             var url = "http://localhost:8080/compte/creerorganisateur"
        }
         
    fetch(url, {
        method: "POST",
        body: formData,
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        credentials: 'include',
    })
    .then((response) => {
        if (response.status === 200) {
            console.log("Inscription réussie.");
            setRedirigerAccueil(true);
            
        } else {
            setSuccesInscription("Pseudonyme déjà utilisé.");
            console.error("Pseudonyme déjà utilisé.");
        }
    })
    }
  
    if (redirigerAccueil) {
        return <Redirect to="/" />;
      }

      return (
        <div className="conteneurGenerique">
            <form onSubmit={handleInscriptionFormSubmit} className="formulaire">
                <label>
                    Pseudo:
                    <input type="text" value={pseudo} onChange={handlePseudoChange} />
                </label>
                <br />
                <label>
                    Mot de passe:
                    <input type="password" value={mdp} onChange={handleMdpChange} />
                </label>
                <br />
                <label>
                    Mail :
                    <input type="text" value={mail} onChange={handleMailChange} />
                </label>
                <br />
                Vous etes:
                    <select name="Role" value={role} onChange={handleRoleChange}>
                        <option value="Participant">Participant</option>
                        <option value="Organisateur">Organisateur</option>
                    </select>
                <br />
                <button type="submit">S'inscrire</button>
                <br />{succesInscription}
            </form>
        </div>
    );
}