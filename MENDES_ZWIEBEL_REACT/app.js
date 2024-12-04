const Router = window.ReactRouterDOM.BrowserRouter;
const Route = window.ReactRouterDOM.Route;
const Link = window.ReactRouterDOM.Link;
const Prompt = window.ReactRouterDOM.Prompt;
const Switch = window.ReactRouterDOM.Switch;
const Redirect = window.ReactRouterDOM.Redirect;
const useParams = window.ReactRouterDOM.useParams;

// utilisateur global pour avoir le pseudo dans toute l'application.
var global_utilisateur = "";

// https://stackoverflow.com/a/56009907

class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = { utilisateur: "", role: "" };
    this.recupererUtilisateur = this.recupererUtilisateur.bind(this);
    this.recupererRole = this.recupererRole.bind(this);
  }

  recupererUtilisateur() {
    fetch("http://localhost:8080/auth/utilisateur", {
      method: "GET",
      credentials: "include",
    })
      .then((resp) => {
        if (!resp.ok) {
          console.error("Erreur de récupération de l'utilisateur.");
        }
        return resp.text();
      })
      .then((data) => {
        global_utilisateur = data;
        this.setState({ utilisateur: data });
      })
  }

  recupererRole() {
    fetch("http://localhost:8080/auth/role", {
      method: "GET",
      credentials: "include",
    })
      .then((resp) => {
        if (!resp.ok) {
          console.error("Erreur de récupération du rôle.");
        }
        return resp.text();
      })
      .then((data) => {
        this.setState({ role: data });
      })
  }

  // Nous n'avons pas réussi a faire en sorte de mettre à jour le role et l'utilisateur lors de la connexion
  // et de la deconnexion, cependant celà peut-être interessant de garder ce fonctionnement pour gérer une
  // eventuelle deconnexion liée à l'expiration d'un cookie.
  componentDidMount() {
    this.recupererUtilisateur();
    this.recupererRole();
    this.refreshInterval = setInterval(() => {
      this.recupererUtilisateur();
      this.recupererRole();
    }, 3000);
  }

  componentWillUnmount() {
    clearInterval(this.refreshInterval);
  }
  // Router permet d'avoir une barre de navigation permettant l'accès à des URL au moyen d'un clique.
  // Switch permet de définir quel composant sera affiché lors de l'accès a une URL.
  render() {
    return (
      <Router>
        <div>
          <nav className="barrenav">
            <h1 className="nomSite">CTFOignon</h1>
            <ul>
              <li>
                <Link to="/" className="lien">Accueil</Link>
              </li>
              <li>
                <Link to="/connexion" className="lien">Connexion</Link>
              </li>
              <li>
                <Link to="/inscription" className="lien">Inscription</Link>
              </li>
              <li>
                <Link to="/deconnexion" className="lien">Deconnexion</Link>
              </li>
            </ul>
            <ul>
              <li>
                <Link to="/equipes" className="lien">Equipes</Link>
              </li>
              <li>
                <Link to="/ctfs" className="lien">CTFs</Link>
              </li>
              <li>
                <Link to="/defis" className="lien">Défis</Link>
              </li>
              <li>
                <Link to="/discussions" className="lien">Discussions</Link>
              </li>
            </ul>
            {this.state.role == "[admin]" ? (
              <ul>
                <li>
                  <Link to="/adminPanel/CTF" className="lien-special">Gestion des ctf</Link>
                </li>
                <li>
                  <Link to="/adminPanel/participants" className="lien-special">Gestion des participants</Link>
                </li>
              </ul>) : ("")}
            {this.state.role == "[organisateur]" ? (
              <ul>
                <li>
                  <Link to="/creerCTF" className="lien-special">Création des CTF</Link>
                </li>
                <li>
                  <Link to="/modifCTF" className="lien-special">Modification des CTF</Link>
                </li>
              </ul>
            ) : ("")
            }
            <ul style={{ listStyleType: "none" }}>
              <li>Compte : {this.state.utilisateur}</li>
              <li>Vous êtes : {this.state.role}</li>
            </ul>
          </nav>
          <Switch>
            <Route exact path="/" component={AccueilComposant} />
            <Route path="/connexion" component={ConnexionComposant} />
            <Route path="/inscription" component={InscriptionComposant} />
            <Route path="/deconnexion" component={DeconnexionComposant} />

            <Route path="/equipes" component={MenuEquipeComposant} />
            <Route path="/equipe/:nomEquipe" component={ConsulterEquipeComposant} />
            <Route path="/creerequipe" component={CreationEquipeComposant} />
            <Route path="/transfererChef/:nomEquipe" component={ChefEquipeComposant}/>

            <Route path="/ctfs" component={MenuCTFComposant} />
            <Route path="/creerCTF" component={CreerCTFComposant} />
            <Route path="/ctf/:titreCTF" component={ConsulterCTFComposant} />

            <Route path="/participant/:pseudoParticipant" component={ConsulterParticipantComposant} />
            
            <Route path="/adminPanel/CTF" component={GestionCTFComposant} />
            <Route path="/adminPanel/participants" component={GestionParticipantComposant} />

            <Route path="/creerCTF" component={CreerCTFComposant} />
            <Route path="/modifCTF" component={MenuModificationCTFComposant} />
            <Route path="/modificationCTF/:CTFtitre" component={ModificationCTFComposant} />
            <Route path="/saisieScores/:CTFtitre" component={SaisieScoresCTFComposant} />

            <Route path="/discussions" component={MenuDiscussionsComposant} />
            <Route path="/discussion/:pseudoInterlocuteur" component={ConsulterDiscussionComposant} />

            <Route path="/defis" component={MenuDefisComposant} />
            <Route path="/defi/:titreDefi" component={ConsulterDefisComposant} />
          </Switch>
        </div>
      </Router>
    );
  }
}

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(<App />);
