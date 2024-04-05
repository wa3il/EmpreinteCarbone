/**
 * Placez ici les scripts qui seront exécutés côté client pour rendre l'application côté client fonctionnelle.
 */
const baseUrl = "https://192.168.75.67/api/v3/";

// <editor-fold desc="Gestion de l'affichage">
/**
 * Fait basculer la visibilité des éléments affichés quand le hash change.<br>
 * Passe l'élément actif en inactif et l'élément correspondant au hash en actif.
 * @param hash une chaîne de caractères (trouvée a priori dans le hash) contenant un sélecteur CSS indiquant un élément à rendre visible.
 */
function show(hash) {
    const oldActiveElement = document.querySelector(".active");
    oldActiveElement.classList.remove("active");
    oldActiveElement.classList.add("inactive");
    const newActiveElement = document.querySelector(hash);
    newActiveElement.classList.remove("inactive");
    newActiveElement.classList.add("active");
}

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
    const elementsNotRequiringConnection = document.getElementsByClassName("notRequiresConnection");
    const visibilityValue2 = isConnected ? "none" : "block";
    for (const element of elementsNotRequiringConnection) {
        element.style.display = visibilityValue2;
    }
}

window.addEventListener('hashchange', () => {
    show(window.location.hash);
});