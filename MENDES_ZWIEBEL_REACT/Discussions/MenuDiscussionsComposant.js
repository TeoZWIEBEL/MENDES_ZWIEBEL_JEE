const MenuDiscussionsComposant = () => {
    const [discussions, setDiscussions] = React.useState([]);
  
    React.useEffect(() => {
      fetchDiscussions();
    }, []);
  
    const fetchDiscussions = () => {
      fetch('http://localhost:8080/discussion/all', {
        method: 'GET',
        credentials : 'include',
      })
        .then(resp => {
          if (!resp.ok) {
            console.error("Erreur de récupération des Discussions.")
          }
          return resp.json();
        })
        .then(data => {
          setDiscussions(data);
        })
    }
  
      return (
        <div className = "conteneurGenerique">
          <div className = "listeGenerique">
          <h1>Discussions</h1>
          <ul style={{ listStyleType: "none" }}>
          {discussions.map(discussion => (
            <li key={discussion.id}>
              {discussion.utilisateur1.pseudo === global_utilisateur ? (
                <span>
                  <Link to={`/discussion/${discussion.utilisateur2.pseudo}`}>{discussion.utilisateur2.pseudo}</Link>
                </span>
              ) : (
                <span>
                <Link to={`/discussion/${discussion.utilisateur1.pseudo}`}>{discussion.utilisateur1.pseudo}</Link>

                </span>
              )}
            </li>
          ))}
        </ul>
          </div> 
        </div>
        
      );
  }