const ConsulterDiscussionComposant = () => {
    const [discussion, setDiscussion] = React.useState("");
    const [message, setMessage] = React.useState("");
    const { pseudoInterlocuteur: routePseudoInterlocuteur } = useParams();
  
    React.useEffect(() => {
      fetchDiscussion();
    }, [routePseudoInterlocuteur]);
  
    const fetchDiscussion = () => {
      fetch(`http://localhost:8080/discussion/${routePseudoInterlocuteur}`, {
        method: 'GET',
        credentials: "include",
      })
        .then(resp => {
          if (!resp.ok) {
            console.error("Erreur de récupération de la discussion.")
          }
          return resp.json();
        })
        .then(data => {
          setDiscussion(data);
        })
    };
  
    const envoyerMessage = (event) => {
        event.preventDefault();
        const formData = new URLSearchParams();

        formData.append("message", message);

        fetch(`http://localhost:8080/discussion/${routePseudoInterlocuteur}/envoyerMessage`, {
              method: 'POST',
              body: formData,
              credentials: 'include',
            })
              .then(response => {
                if (response.ok) {
                  console.log("Message envoyé.");
                  fetchDiscussion();
                } else {
                  console.error("Erreur d'envoi du message.");
                }
              })
      }
 // https://stackoverflow.com/questions/54567693/array-is-printed-as-a-string-in-react-js 
    return (
      <div className="conteneurGenerique">
        <div className="listeGenerique">
        <ul style={{ listStyleType: "none" }}>
  {discussion.messages && discussion.messages.map((message, index) => (
    <li key={index.toString()}>
      {message}
    </li>
  ))}
</ul>
        </div>
        <form onSubmit={envoyerMessage} className="formulaire">
                <label>
                    Message:
                    <input type="text" value={message} onChange={(e) => setMessage(e.target.value)} />
                </label>
                <button type="submit">Envoyer</button>
            </form>
      </div>
    );
  };
  