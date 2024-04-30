/**
 * Placez ici les scripts qui seront exécutés côté client pour rendre l'application côté client fonctionnelle.
 */
const baseUrl = "https://192.168.75.106/api/";


// <editor-fold desc="Gestion de l'affichage">
/**
 * Fait basculer la visibilité des éléments affichés quand le hash change.<br>
 * Passe l'élément actif en inactif et l'élément correspondant au hash en actif.
 * @param hash une chaîne de caractères (trouvée a priori dans le hash) contenant un sélecteur CSS indiquant un élément à rendre visible.
 */
function showSection(sectionId) {
    // Cacher toutes les sections
    var sections = document.querySelectorAll('section');
    sections.forEach(function(section) {
        section.classList.add('inactive');
    });

    // Afficher la section spécifique
    var sectionToShow = document.getElementById(sectionId);
    if (sectionToShow) {
        sectionToShow.classList.remove('inactive');
    }
} 
document.addEventListener('DOMContentLoaded', function() {

    // Ajouter des écouteurs d'événements pour les liens de la navbar
    document.getElementById('accueil1').addEventListener('click', function(event) {
        event.preventDefault(); // Empêche le comportement par défaut du lien (redirection)
        showSection('sectionAccueil'); // Affiche la section Accueil
    });

    document.getElementById('accueil2').addEventListener('click', function(event) {
        event.preventDefault();
        showSection('sectionAccueil'); 
    });

    document.getElementById('produits').addEventListener('click', function(event) {
        event.preventDefault();
        showSection('sectionProduits');
    });

    document.getElementById('compte').addEventListener('click', function(event) {
        event.preventDefault();
        showSection('sectionCompte');
    });

    document.getElementById('connexion1').addEventListener('click', function(event) {
        event.preventDefault();
        showSection('sectionConnexion');
    });

    document.getElementById('connexion2').addEventListener('click', function(event) {
        event.preventDefault();
        showSection('sectionConnexion');
    });

    document.getElementById('inscription').addEventListener('click', function(event) {
        event.preventDefault();
        showSection('sectionInscription');
    });

    document.getElementById('empreinte').addEventListener('click', function(event) {
        event.preventDefault();
        showSection('sectionEmpreinte');
    });

    document.getElementById('listes').addEventListener('click', () => {
        document.getElementById('secCompte2').classList.add('inactive');
        document.getElementById('secListe').classList.remove('inactive');
    });
    document.getElementById('compte2').addEventListener('click', () => {
        document.getElementById('secCompte2').classList.remove('inactive');
        document.getElementById('secListe').classList.add('inactive');
        
    });
    document.getElementById('deleteAccount').addEventListener('click', () => {
        document.getElementById('secCompte2').classList.add('inactive');
        document.getElementById('secListe').classList.add('inactive');
        document.getElementById('secDelete').classList.remove('inactive');
    });
    if(localStorage.getItem("token") !== null){
        isConnected();
    }

});


/**
 * Affiche pendant 10 secondes un message sur l'interface indiquant le résultat de la dernière opération.
 * @param text Le texte du message à afficher
 * @param cssClass La classe CSS dans laquelle afficher le message (défaut = alert-info)
 */
function displayRequestResult(text, cssClass = "alert-info") {
    const requestResultElement = document.getElementById("requestResult");
    requestResultElement.innerText = text;
    requestResultElement.classList.add(cssClass);
    setTimeout(
        () => {
            requestResultElement.classList.remove(cssClass);
            requestResultElement.innerText = "";
        }, 10000);
}

/**
 * Affiche ou cache les éléments de l'interface qui nécessitent une connexion.
 * @param isConnected un Booléen qui dit si l'utilisateur est connecté ou pas
 */
function displayConnected(isConnected) {
    const elementsRequiringConnection = document.getElementsByClassName("requiresConnection");
    const visibilityValue = isConnected ? "visible" : "collapse";
    for (const element of elementsRequiringConnection) {
        element.style.visibility = visibilityValue;
    }
   const visibilityValue2 = !isConnected ? "visible" : "collapse";
    document.getElementById("connexion1").parentNode.style.visibility = visibilityValue2;
//     const elementsNotRequiringConnection = document.getElementsByClassName("notRequiresConnection");
//     // const visibilityValue2 = isConnected ? "none" : "block";
//     for (const element of elementsNotRequiringConnection) {
//         element.style.visibility = visibilityValue2;
//         // element.style.display = visibilityValue2;
  //  }
}

function getProperties(url) {
    const headers = new Headers();
    headers.append("Accept", "application/json");
    const requestConfig = {
        method: "GET",
        headers: headers,
        mode: "cors"
    };
    
    return fetch(baseUrl +url, requestConfig)
        .then((response) => {
            if(response.ok) {
                return response.json();
            } else {
                throw new Error("Response is error (" + response.status + ") or does not contain JSON (" + response.headers.get("Content-Type") + ").");
            }
        })
        .catch((err) => {
            console.error("In getProperties: " + err);
        });
}

function renderListAliment(int) {
    getProperties("aliments").then( async (res) => {
        if(Array.isArray(res)) {
            document.getElementById("nbProduits").innerText = res.length;
            let aliments = []
            if (int>res.length) {
                int = res.length
            }
            for (var i = 0; i < int; i++) {
                let aliment = res[i];
                aliments.push(aliment);
            }
            const template = document.getElementById('list_aliments_template');
            if (!template){
                console.error("l'élément n'existe pas...");
                return;
            }
            const templ = template.innerText;
            const rendered = Mustache.render(templ, { aliments: aliments});
            const elem = document.getElementById('listAliments');
            if (!elem){
                console.error("l'élément n'existe pas...");
                return;
            }
            elem.innerHTML = rendered;          
        }
    }).catch((err) => {
        console.error("In renderListAliment: " + err);
    });
}


renderListAliment(10);

// Validation des Entrées Utilisateur
function validateForm() {
    const name = document.getElementById('inputName').value.trim();
    const email = document.getElementById('inputEmail').value.trim();
    const password = document.getElementById('inputPassword').value.trim();

    if (name === '' || email === '' || password === '') {
        alert('Veuillez remplir tous les champs.');
        return false;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        alert('Veuillez saisir une adresse e-mail valide.');
        return false;
    }

    if (password.length < 8) {
        alert('Le mot de passe doit contenir au moins 8 caractères.');
        return false;
    }

    return true;
}

function register() {
    console.log("fct register");
    const headers = new Headers();
    headers.append("Content-Type", "application/json");
    const body = {
        login : document.getElementById('inputLogin').value,
        name : document.getElementById('inputName').value,
        password : document.getElementById('inputPassword').value
    };
    const requestConfig = {
        method: "POST",
        headers: headers,
        body: JSON.stringify(body)
    };
    fetch(baseUrl+'users' , requestConfig)
        .then((response) => {
            if (response.status === 200) {
                console.log("Utilisateur créé.");
                //displayRequestResult("Utilisateur créé.", "alert-info");
            } else if (response.status === 400) {
                console.log("Paramètres de la requête non acceptables");
                //displayRequestResult("Paramètres de la requête non acceptables", "alert-warning");
            } else if (response.status === 409) {
                console.log("Un utilisateur avec ce login existe déjà");
                //displayRequestResult("Un utilisateur avec ce login existe déjà", "alert-warning");
            } else {
                console.log("Connexion refusée ou impossible, code erreur : ");
                //displayRequestResult("Connexion refusée ou impossible, code erreur : " + response.status, "alert-danger");
                throw new Error("Bad response code (" + response.status + ").");
            }
            return response.json();
        }).then(data => {
            // Récupérer le token de la réponse JSON
            var token = 'Bearer '+data.token;
            localStorage.setItem("token", token);
            localStorage.setItem("login", body.login);
            headers.append("Authorization", token);
            // Utiliser le token comme nécessaire
            console.log("Token récupéré :", token);
            console.log("Connexion réussie");
            //displayRequestResult("Connexion réussie", "alert-success");
            isConnected()
        })
        .catch((err) => {
            console.error("Error to register : " + err);
        })
}

function connect() {
    const headers = new Headers();
    headers.append("Content-Type", "application/json");
    headers.append("Accept", "application/json");
    const body = {
        login: document.getElementById("login").value,
        password: document.getElementById("password").value
    };
    const requestConfig = {
        method: "POST",
        headers: headers,
        body: JSON.stringify(body),
        mode: "cors" 
    };
    fetch(baseUrl+'users/login', requestConfig)
        .then(response => {
            // Vérifier si la requête a réussi (status 200-299)
            if (!response.ok) {
                console.log("Connexion refusée impossible");
                //displayRequestResult("Connexion refusée ou impossible", "alert-danger");
                throw new Error("Bad response code (" + response.status + ").");
            }
            // Extraire le token du corps de la réponse
            return response.json();
        })
        .then(data => {
            // Récupérer le token de la réponse JSON
            var token = 'Bearer '+data.token;
            localStorage.setItem("token", token);
            localStorage.setItem("login", body.login);
            headers.append("Authorization", token);
            // Utiliser le token comme nécessaire
            console.log("Token récupéré :", token);
            console.log("status : ", data.status);
            console.log("Connexion réussie");
            //displayRequestResult("Connexion réussie", "alert-success");
            isConnected()
        })
        .catch((err) => {
            console.error("In login: " + err);
        })
}

document.addEventListener("DOMContentLoaded", function() {
    const searchTermInput = document.getElementById("recherche");
    var element = document.getElementById("btnrecherche");
    var originalDisplayStyle = element.style.display;

    searchTermInput.addEventListener("keyup", function(event) {

        if (searchTermInput.value.trim() !== "") {
            element.style.display = "none";
        
            getProperties("aliments").then( async (res) => {
                if(Array.isArray(res)) {
                    let aliments = [];
    
                    for (var i = 0; i < res.length; i++) {
                        if (res[i].nomLegume.toLowerCase().startsWith(searchTermInput.value.trim().toLowerCase())) {
                            console.log("vrai");
                            let aliment = res[i];
                            aliments.push(aliment);
                        }
                    }
                    const template = document.getElementById('list_aliments_template');
                    const templ = template.innerText;
                    const rendered = Mustache.render(templ, { aliments: aliments});
                    const elem = document.getElementById('listAliments');
                    elem.innerHTML = rendered;          
                }
            }).catch((err) => {
                console.error("In renderListAliment: " + err);
            });
        }
        else{
            element.style.display = originalDisplayStyle;
            renderListAliment(10);
    
        }
    });
  

});

document.addEventListener("DOMContentLoaded", function() {
    btn = document.getElementById("btnrecherche");
    nb = 20
    btn.addEventListener("click", function(event) {
    renderListAliment(nb);
    nb = nb+10
    });
});

function getUserProperty() {
    const headers = new Headers();
    headers.append("Accept", "application/json");
    headers.append("Authorization", localStorage.getItem("token"));
    
    const requestConfig = {
        method: "GET",
        headers: headers,
        mode: "cors" // pour le cas où vous utilisez un serveur différent pour l'API et le client.
    };

    log = localStorage.getItem("login");

    fetch(baseUrl + "users/"+log, requestConfig)
        .then(async(response) => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error("Response is error (" + response.status + ")");
            }
        }).then(data=>{
            console.log(data);
            const template = document.getElementById('info-user-Template').innerHTML;
            const rendered = Mustache.render(template, data);
            document.getElementById('monCompte').innerHTML = rendered;
        })
        .catch((err) => {
            console.error("In getUser: " + err);
        });
}

function updateBody(){
    const nameInput = document.getElementById("name_update_input");
    const newPasswordInput = document.getElementById("new_password1");
    const newPasswordInput2 = document.getElementById("new_password2");

    const name = nameInput ? nameInput.value : "";
    const password = newPasswordInput ? newPasswordInput.value : "";
    const password2 = newPasswordInput2 ? newPasswordInput2.value : "";

    if (password !== password2) {
        alert("Les mots de passe ne correspondent pas");
        return;
    }

    const body = {
        login: localStorage.getItem("login"),
        name: name,
        password: password
    };

    return body;
}

function updateUser(){

    const headers = new Headers();
    headers.append("Content-Type", "application/json");
    headers.append("Authorization", localStorage.getItem("token"));

    console.log(localStorage.getItem("token"));
    const body = updateBody();
    console.log(body);
    
    const requestConfig = {
        method: "PUT",
        headers: headers,
        body: JSON.stringify(body), 
        mode: "cors"
    };
    log = localStorage.getItem("login");
    fetch(baseUrl + "users/"+log, requestConfig).then(async(res) =>{
        if(res.ok) {
            alert("Modification réussie");
        } else {
            //displayRequestResult("Modification de votre nom refusée ou impossible", "alert-danger");
            throw new Error("Bad response code (" + res.status + ").");
        }
    })
    .catch((err) => {
        console.error("In updatePropertiesUser: " + err);
    });
}


function deco() {
    console.log("j'essaie de me déco");
    const headers = new Headers();
    headers.append("Authorization", localStorage.getItem("token"));
    const requestConfig = {
        method: "POST",
        headers: headers,
        mode: "cors" 
    };
    fetch(baseUrl+'users/logout', requestConfig)
        .then(response => {
            // Vérifier si la requête a réussi (status 200-299)
            if (response.ok) {
                localStorage.removeItem('token'); 
                console.log("Vous êtes déconnecté");
                //displayRequestResult("Connexion refusée ou impossible", "alert-danger");
                //throw new Error("Bad response code (" + response.status + ").");
            } else {
                throw new Error("Bad response code (" + response.status + ").");
            }
            // Extraire le token du corps de la réponse
            //return response.json();
        })
        .catch((err) => {
            console.error("In logout: " + err);
        }).finally(()=>{
            localStorage.removeItem("login");
             localStorage.removeItem("token");
             displayConnected(false);
             showSection('sectionAccueil');
        });
}

function deleteAccount() {
    console.log("j'essaie de supprimer mon compte");
    const headers = new Headers();
    headers.append("Content-Type", "application/json");
    headers.append("Authorization", localStorage.getItem("token"));
    const requestConfig = {
        method: "DELETE",
        headers: headers,
        mode: "cors" 
    };
    fetch(baseUrl+'users/'+localStorage.getItem("login"), requestConfig)
        .then(response => {
            // Vérifier si la requête a réussi (status 200-299)
            if (response.ok) {
                console.log("Vous avez supprimé votre compte");
                displayConnected(false);
                showSection('sectionAccueil');
                //displayRequestResult("Connexion refusée ou impossible", "alert-danger");
                //throw new Error("Bad response code (" + response.status + ").");
            } else {
                throw new Error("Bad response code (" + response.status + ").");
            }
        })
        .catch((err) => {
            console.error("In deleteAccount: " + err);
        })
}
function isConnected(){
        showSection('sectionAccueil');
        getUserProperty();
        displayConnected(true);
}

function addProduct() {
    console.log("j'ajoute un aliment");
    const headers = new Headers();
    headers.append("Content-Type", "application/json");
    headers.append("Authorization", localStorage.getItem("token"));
    const body = {
        login: localStorage.getItem("login"),
        alimentId: document.getElementById("idProduit").innerText,
        quantity: document.getElementById("quantity").value,
        date: document.getElementById("date").value
    };
    const requestConfig = {
        method: "POST",
        headers: headers,
        body: JSON.stringify(body),
        mode: "cors" 
    };
    fetch(baseUrl+'users/aliments', requestConfig)
        .then(response => {
            // Vérifier si la requête a réussi (status 200-299)
            if (response.ok) {
                alert("Produit ajouté");
                //displayRequestResult("Connexion refusée ou impossible", "alert-danger");
                //throw new Error("Bad response code (" + response.status + ").");
            } else {
                throw new Error("Bad response code (" + response.status + ").");
            }
        })
        .catch((err) => {
            console.error("In addProduct: " + err);
        })
}

function renderListAlimentUser() {
    getProperties("aliments/"+localStorage.getItem("login")).then( async (res) => {
        if(Array.isArray(res)) {
            let produits = []
            for (var i = 0; i < int; i++) {
                let prod = res[i];
                produits.push(prod);
            }
            const template = document.getElementById('list_produit');
            if (!template){
                console.error("l'élément n'existe pas...");
                return;
            }
            const templ = template.innerText;
            const rendered = Mustache.render(templ, { produits: produits});
            const elem = document.getElementById('listProduits');
            if (!elem){
                console.error("l'élément n'existe pas...");
                return;
            }
            elem.innerHTML = rendered;          
        }
    }).catch((err) => {
        console.error("In renderListAlimentUser: " + err);
    });
}